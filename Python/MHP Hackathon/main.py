from datetime import date
from flask import Flask, abort, render_template, redirect, url_for, flash
from flask_bootstrap import Bootstrap5
from flask_ckeditor import CKEditor
from flask_gravatar import Gravatar
from flask_login import UserMixin, login_user, LoginManager, current_user, logout_user
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.orm import relationship, DeclarativeBase, Mapped, mapped_column
from sqlalchemy import Integer, String, Text
from functools import wraps
from werkzeug.security import generate_password_hash, check_password_hash
from sqlalchemy.orm import relationship
from forms import RegisterForm, LoginForm, BookForm, AiForm
from ai_func import room_oc_prob, table_oc_prob

app = Flask(__name__)
app.config['SECRET_KEY'] = '8BYkEfBA6O6donzWlSihBXox7C0sKR6b'
ckeditor = CKEditor(app)
Bootstrap5(app)

# Configure Flask-Login
login_manager = LoginManager()
login_manager.init_app(app)


@login_manager.user_loader
def load_user(user_id):
    return db.get_or_404(User, user_id)


class Base(DeclarativeBase):
    pass


app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///posts.db'
db = SQLAlchemy(model_class=Base)
db.init_app(app)


# CONFIGURE TABLES
class BlogPost(db.Model):
    __tablename__ = "blog_posts"
    id: Mapped[int] = mapped_column(Integer, primary_key=True)
    author_id: Mapped[int] = mapped_column(Integer, db.ForeignKey("users.id"))
    author = relationship("User", back_populates="posts")
    title: Mapped[str] = mapped_column(String(250), unique=True, nullable=False)
    subtitle: Mapped[str] = mapped_column(String(250), nullable=False)
    date: Mapped[str] = mapped_column(String(250), nullable=False)
    body: Mapped[str] = mapped_column(Text, nullable=False)
    img_url: Mapped[str] = mapped_column(String(250), nullable=False)
    comments = relationship("Comment", back_populates="parent_post")


class User(UserMixin, db.Model):
    __tablename__ = "users"
    id: Mapped[int] = mapped_column(Integer, primary_key=True)
    email: Mapped[str] = mapped_column(String(100), unique=True)
    password: Mapped[str] = mapped_column(String(100))
    name: Mapped[str] = mapped_column(String(100))
    posts = relationship("BlogPost", back_populates="author")
    comments = relationship("Comment", back_populates="comment_author")


class Comment(db.Model):
    __tablename__ = "comments"
    id: Mapped[int] = mapped_column(Integer, primary_key=True)
    text: Mapped[str] = mapped_column(Text, nullable=False)
    author_id: Mapped[int] = mapped_column(Integer, db.ForeignKey("users.id"))
    comment_author = relationship("User", back_populates="comments")
    post_id: Mapped[str] = mapped_column(Integer, db.ForeignKey("blog_posts.id"))
    parent_post = relationship("BlogPost", back_populates="comments")


class Seats(db.Model):
    __tablename__ = "reservations"
    id: Mapped[int] = mapped_column(Integer, primary_key=True)
    nr: Mapped[int] = mapped_column(Integer, nullable=False)
    booked: Mapped[int] = mapped_column(Integer, nullable=True)
    author_id: Mapped[int] = mapped_column(Integer, db.ForeignKey("users.id"))


with app.app_context():
    db.create_all()


def admin_only(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        # If id is not 1 then return abort with 403 error
        if current_user.id != 1:
            return abort(403)
        # Otherwise continue with the route function
        return f(*args, **kwargs)

    return decorated_function


# Register new users into the User database
@app.route('/register', methods=["GET", "POST"])
def register():
    form = RegisterForm()
    if form.validate_on_submit():

        # Check if user email is already present in the database.
        result = db.session.execute(db.select(User).where(User.email == form.email.data))
        user = result.scalar()
        if user:
            # User already exists
            flash("You've already signed up with that email, log in instead!")
            return redirect(url_for('login'))

        hash_and_salted_password = generate_password_hash(
            form.password.data,
            method='pbkdf2:sha256',
            salt_length=8
        )
        new_user = User(
            email=form.email.data,
            name=form.name.data,
            password=hash_and_salted_password,
        )
        db.session.add(new_user)
        db.session.commit()
        # Login
        login_user(new_user)
        return redirect(url_for("get_all_posts"))
    return render_template("register.html", form=form, current_user=current_user)


@app.route('/login', methods=["GET", "POST"])
def login():
    form = LoginForm()
    if form.validate_on_submit():
        password = form.password.data
        result = db.session.execute(db.select(User).where(User.email == form.email.data))
        user = result.scalar()
        # Email doesn't exist
        if not user:
            flash("That email does not exist, please try again.")
            return redirect(url_for('login'))
        # Password incorrect
        elif not check_password_hash(user.password, password):
            flash('Password incorrect, please try again.')
            return redirect(url_for('login'))
        else:
            login_user(user)
            return redirect(url_for('get_all_posts'))

    return render_template("login.html", form=form, current_user=current_user)


@app.route('/logout')
def logout():
    logout_user()
    return redirect(url_for('get_all_posts'))


@app.route('/')
def get_all_posts():
    result = db.session.execute(db.select(BlogPost))
    posts = result.scalars().all()
    return render_template("index.html", all_posts=posts, current_user=current_user)


@app.route("/about")
@admin_only
def about():
    return render_template("reservation.html", current_user=current_user)


@app.route("/book", methods=["GET", "POST"])
def book():
    form = AiForm()
    result = db.session.execute(db.select(Seats))
    seats = result.scalars().all()
    clear_seats = [seat.id for seat in seats if not seat.booked]
    users_who_reserved = [seat.author_id for seat in seats if seat.author_id != -1]
    if form.validate_on_submit():
        table_nr = int(form.table_name.data) // 4 + 1
        table_nr_seat = int(form.table_name.data) % 4 + 1
        table_name = "CLUJ_5_beta_" + str(table_nr) + "." + str(table_nr_seat)
        data = form.data.data
        period_of_day = form.part_of_day.data.lower() + "Half"

        rez = table_oc_prob(table_name, data, period_of_day)
        return render_template("confirmation.html", current_user=current_user,  clear_seats=clear_seats, seats=seats,
                                users_who_reserved=users_who_reserved, form=form, rez=rez)

    return render_template("confirmation.html", current_user=current_user,
                           clear_seats=clear_seats, seats=seats,
                           users_who_reserved=users_who_reserved, form=form, rez=0)


@app.route("/book/<int:btn_id>", methods=["GET", "POST"])
def reservation(btn_id):
    form = BookForm()
    if form.validate_on_submit():
        seat = db.get_or_404(Seats, btn_id)
        seat.booked = 1
        seat.author_id = current_user.id
        db.session.commit()
        return redirect(url_for('succes_res', seat_no=seat.id))
    return render_template("reservation.html", current_user=current_user, btn_id=btn_id, form=form)


@app.route("/succes/<int:seat_no>")
def succes_res(seat_no):
    return render_template("succes_reservation.html", seat_no=seat_no)


@app.route("/profile")
def profile():
    res = db.session.execute(db.select(Seats).where(Seats.author_id == current_user.id)).scalar()
    return render_template("profile.html", current_user=current_user, res=res)


@app.route("/cancel")
def cancel_res():
    res = db.session.execute(db.select(Seats).where(Seats.author_id == current_user.id)).scalar()
    res.booked = 0
    res.author_id = -1
    db.session.commit()
    return redirect(url_for("profile"))


if __name__ == "__main__":
    app.run(port=5001)

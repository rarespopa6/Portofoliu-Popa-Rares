from flask_wtf import FlaskForm
from wtforms import StringField, SubmitField, PasswordField
from wtforms.validators import DataRequired, URL
from flask_ckeditor import CKEditorField


class RegisterForm(FlaskForm):
    email = StringField("Email", validators=[DataRequired()])
    password = PasswordField("Password", validators=[DataRequired()])
    name = StringField("Name", validators=[DataRequired()])
    submit = SubmitField("Sign Up")


class LoginForm(FlaskForm):
    email = StringField("Email", validators=[DataRequired()])
    password = PasswordField("Password", validators=[DataRequired()])
    submit = SubmitField("Log In")


class BookForm(FlaskForm):
    submit = SubmitField("Confirm", validators=[DataRequired()])


class AiForm(FlaskForm):
    table_name = StringField("Number of the seat", validators=[DataRequired()])
    data = StringField("Date (DD/MM/YYYY)", validators=[DataRequired()])
    part_of_day = StringField("First/Second part of the day", validators=[DataRequired()])
    submit_f = SubmitField("Submit")

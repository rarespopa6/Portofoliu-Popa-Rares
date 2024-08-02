import express from "express";
import bodyParser from "body-parser";
import pg from "pg";
import bcrypt from "bcrypt";
import passport from "passport";
import { Strategy as LocalStrategy } from "passport-local";
import { Strategy as GoogleStrategy } from "passport-google-oauth20";
import session from "express-session";
import cors from "cors"; 
import dotenv from "dotenv";
import cron from 'node-cron';
import stripe from "stripe";

dotenv.config();

const app = express();
const port = process.env.PORT || 3001;
const saltRounds = 10;

app.use(cors({
  origin: "http://localhost:3000",
  credentials: true
}));

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

// Configurare Express
app.use(session({
  secret: process.env.SESSION_SECRET,
  resave: false,
  saveUninitialized: false,
  cookie: {
    secure: false, // true în producție, pentru HTTPS
    httpOnly: true,
    maxAge: 1000 * 60 * 60 * 24 // 1 zi
  }
}));

app.use(passport.initialize());
app.use(passport.session());

const db = new pg.Client({
  user: process.env.PG_USER,
  host: process.env.PG_HOST,
  database: process.env.PG_DATABASE,
  password: process.env.PG_PASSWORD,
  port: process.env.PG_PORT,
});
db.connect();

// Strategie locala
passport.use(new LocalStrategy(
  {
    usernameField: "email",
    passwordField: "password",
  },
  async (username, password, done) => {
    try {
      const result = await db.query("SELECT * FROM users WHERE email = $1", [username]);
      if (result.rows.length > 0) {
        const user = result.rows[0];
        const storedHashedPassword = user.password;
        bcrypt.compare(password, storedHashedPassword, (err, valid) => {
          if (err) {
            console.error("Eroare la compararea parolelor:", err);
            return done(err);
          } else {
            if (valid) {
              return done(null, user);
            } else {
              return done(null, false, { message: "Incorrect Password." });
            }
          }
        });
      } else {
        return done(null, false, { message: "User not found. Register first." });
      }
    } catch (err) {
      console.error(err);
      return done(err);
    }
  }
));

// Strategie Google
passport.use(new GoogleStrategy({
  clientID: process.env.GOOGLE_CLIENT_ID,
  clientSecret: process.env.GOOGLE_CLIENT_SECRET,
  callbackURL: "/auth/google/callback"
}, async (accessToken, refreshToken, profile, done) => {
  try {
    const result = await db.query("SELECT * FROM users WHERE email = $1", [profile.emails[0].value]);
    if (result.rows.length > 0) {
      return done(null, result.rows[0]);
    } else {
      const newUser = await db.query(
        "INSERT INTO users (lastName, firstName, email, password) VALUES ($1, $2, $3, $4) RETURNING *",
        [profile.name.givenName, profile.name.familyName, profile.emails[0].value, "google"]
      );
      return done(null, newUser.rows[0]);
    }
  } catch (err) {
    console.error(err);
    return done(err);
  }
}));

passport.serializeUser((user, done) => {
  done(null, user.id);
});

passport.deserializeUser(async (id, done) => {
  try {
    const result = await db.query("SELECT * FROM users WHERE id = $1", [id]);
    done(null, result.rows[0]);
  } catch (err) {
    done(err);
  }
});

// Endpoint pentru login local
app.post("/api/login", (req, res, next) => {
  passport.authenticate("local", (err, user, info) => {
    if (err) return next(err);
    if (!user) return res.status(400).json({ success: false, message: info.message });
    req.logIn(user, (err) => {
      if (err) return next(err);
      return res.json({ success: true, user });
    });
  })(req, res, next);
});

// Endpoint pentru înregistrare
app.post("/api/register", async (req, res) => {
  const { firstName, lastName, email, password } = req.body;

  try {
    const checkResult = await db.query("SELECT * FROM users WHERE email = $1", [email]);

    if (checkResult.rows.length > 0) {
      res.status(400).json({ error: "User already exists." });
    } else {
      bcrypt.hash(password, 10, async (err, hash) => {
        if (err) {
          console.error("Eroare la hash-ul parolei:", err);
          res.status(500).json({ error: "Eroare la server" });
        } else {
          const result = await db.query(
            "INSERT INTO users (lastName, firstName, email, password) VALUES ($1, $2, $3, $4) RETURNING *",
            [lastName, firstName, email, hash]
          );
          const newUser = result.rows[0];
          
          req.logIn(newUser, (err) => {
            if (err) {
              console.error("Eroare la autentificare după înregistrare:", err);
              res.status(500).json({ error: "Eroare la server" });
            } else {
              res.json({ success: true, user: newUser });
            }
          });
        }
      });
    }
  } catch (err) {
    console.error("Eroare la înregistrare:", err);
    res.status(500).json({ error: "Eroare la server" });
  }
});

// Endpoint pentru autentificare cu Google
app.get("/auth/google",
  passport.authenticate("google", { scope: ["profile", "email"] })
);

app.get("/auth/google/callback",
  passport.authenticate("google", { failureRedirect: "/login" }),
  (req, res) => {
    res.redirect("http://localhost:3000/"); 
  }
);

// Logout user
app.get("/api/logout", (req, res) => {
  req.logout((err) => {
    if (err) {
      console.error("Eroare la logout:", err);
      res.status(500).json({ error: "Eroare la server" });
    } else {
      res.json({ success: true });
    }
  });
});

// GET current user
app.get("/api/user", (req, res) => {
  if (req.isAuthenticated()) {
    res.json({ isAuthenticated: true, user: req.user });
  } else {
    res.json({ isAuthenticated: false });
  }
});

// Change password
app.patch('/api/user/change-password', async (req, res) => {
  const id = req.user.id;
  const { oldPassword, newPassword } = req.body;

  try {
    const result = await db.query("SELECT password FROM users WHERE id = $1", [id]);
    if (result.rows.length === 0) {
      return res.status(404).json({ error: 'Utilizatorul nu a fost găsit' });
    }

    const storedHashedPassword = result.rows[0].password;
    bcrypt.compare(oldPassword, storedHashedPassword, async (err, valid) => {
      if (err) {
        console.error("Eroare la compararea parolelor:", err);
        return res.status(500).json({ error: 'Eroare la server' });
      } else if (valid) {
        const hashedNewPassword = await bcrypt.hash(newPassword, 10); 

        const updateResult = await db.query("UPDATE users SET password = $1 WHERE id = $2", [hashedNewPassword, id]);
        if (updateResult.rowCount === 1) {
          return res.json({ message: 'Parola a fost actualizată cu succes' });
        } else {
          return res.status(500).json({ error: 'Eroare la actualizarea parolei' });
        }
      } else {
        return res.status(400).json({ error: 'Parola veche este incorectă' });
      }
    });
  } catch (error) {
    console.error("Eroare la actualizarea parolei:", error);
    return res.status(500).json({ error: 'Eroare la server' });
  }
});

// Toate produsele
app.get("/api/products", async (req, res) => {
  try {
    const result = await db.query("SELECT * FROM products");
    res.json(result.rows);
  } catch (error) {
    console.error("Eroare la preluarea produselor din baza de date:", error);
    res.status(500).json({ error: "Eroare la server" });
  }
});

// Produsul cu id specific
app.get("/api/products/id/:id", async (req, res) => {
  const { id } = req.params;
  try {
    const result = await db.query("SELECT * FROM products WHERE id = $1", [parseInt(id)]);
    if (result.rows.length === 0) {
      res.status(404).json({ error: "Produsul nu a fost găsit" });
    } else {
      res.json(result.rows[0]); 
    }
  } catch (error) {
    console.error("Eroare la preluarea produsului din baza de date:", error);
    res.status(500).json({ error: "Eroare la server" });
  }
});

// Produsele din categorie specifica
app.get("/api/products/category/:category", async (req, res) => {
  const { category } = req.params;
  try {
    const result = await db.query("SELECT * FROM products WHERE category = $1", [category]);
    res.json(result.rows);
  } catch (error) {
    console.error(`Eroare la preluarea produselor din categoria '${category}':`, error);
    res.status(500).json({ error: "Eroare la server" });
  }
});

// Cosul de cumparaturi al utilizatorului
app.get("/api/cart", async (req, res) => {
  const userId = req.user.id;
  try {
    const userResult = await db.query("SELECT cart FROM users WHERE id = $1", [userId]);
    let cart = userResult.rows[0]?.cart || [];

    if (typeof cart === 'string') {
      cart = JSON.parse(cart);
    }

    if (!Array.isArray(cart)) {
      cart = [];
    }

    if (cart.length === 0) {
      return res.json([]);
    }

    const productIds = cart.map(item => item.id);
    const itemsResult = await db.query(`
        SELECT id, name, price, image_urls, category, brand, gender
        FROM products
        WHERE id = ANY($1)
    `, [productIds]);

    const productMap = {};
    itemsResult.rows.forEach(item => {
      productMap[item.id] = {
        id: item.id,
        name: item.name,
        price: item.price,
        image_urls: item.image_urls,
        category: item.category,
        brand: item.brand,
        gender: item.gender,
      };
    });

    const items = cart.map(cartItem => {
      const product = productMap[cartItem.id];
      return {
        ...product,
        size: cartItem.size,
        quantity: cartItem.quantity
      };
    });

    res.json(items);
  } catch (error) {
    console.error("Error fetching cart items:", error);
    res.status(500).json({ error: "Server error" });
  }
});

// Adauga in cosul utilizatorului
app.post("/api/cart", async (req, res) => {
  const userId = req.user.id;
  const { productId, size } = req.body;
  const quantity = 1; 

  try {
    const userResult = await db.query("SELECT cart FROM users WHERE id = $1", [userId]);
    let cart = userResult.rows[0]?.cart || [];

    if (typeof cart === 'string') {
      cart = JSON.parse(cart);
    }

    if (!Array.isArray(cart)) {
      cart = [];
    }

    const existingItemIndex = cart.findIndex(item => (item.id == productId && item.size == size));
    if (existingItemIndex === -1) {
      cart.push({ id: productId, size, quantity });
    } else {
      cart[existingItemIndex].quantity += 1;
    }

    await db.query("UPDATE users SET cart = $1 WHERE id = $2", [JSON.stringify(cart), userId]);
    res.json({ message: "Product added to cart" });
  } catch (error) {
    console.error("Error adding to cart:", error);
    res.status(500).json({ error: "Server error" });
  }
});

// Sterge produsul cu marimea respectiva din cos
app.post("/api/cart/remove", async (req, res) => {
  const userId = req.user.id;
  const { productId, size } = req.body;

  try {
    const userResult = await db.query("SELECT cart FROM users WHERE id = $1", [userId]);
    let cart = userResult.rows[0].cart || [];

    if (typeof cart === 'string') {
      cart = JSON.parse(cart);
    }

    cart = cart.filter(item => !(item.id === productId && item.size === size));
    const updateResult = await db.query("UPDATE users SET cart = $1 WHERE id = $2", [JSON.stringify(cart), userId]);

    res.json({ message: "Product removed from cart" });
  } catch (error) {
    console.error("Error removing from cart:", error);
    res.status(500).json({ error: "Server error" });
  }
});

// Goleste cosul dupa plasarea comenzii
app.delete('/api/cart', async (req, res) => {
  const userId = req.user.id; 

  try {
    await db.query('UPDATE users SET cart = $1 WHERE id = $2', [[], userId]);
    res.json({ message: 'Conținutul coșului a fost șters cu succes.' });
  } catch (error) {
    console.error('Eroare la ștergerea conținutului coșului:', error);
    res.status(500).json({ error: 'Eroare la server' });
  }
});

// Favoritele utilizatorului
app.get("/api/favorites", async (req, res) => {
  const userId = req.user.id;
  try {
      const userResult = await db.query("SELECT fav_ids FROM users WHERE id = $1", [userId]);
      const favIds = userResult.rows[0].fav_ids || [];
      if (favIds.length === 0) {
          return res.json([]);
      }
      const itemsResult = await db.query(`
          SELECT id, name, price, image_urls, category, brand, quantity
          FROM products
          WHERE id = ANY($1)
      `, [favIds]);
      res.json(itemsResult.rows);
  } catch (error) {
      console.error("Eroare la preluarea produselor favorite:", error);
      res.status(500).json({ error: "Eroare la server" });
  }
});

// Adauga la favorite
app.post("/api/favorites", async (req, res) => {
  const userId = req.user.id;
  const { productId } = req.body;
  try {
      const userResult = await db.query("SELECT fav_ids FROM users WHERE id = $1", [userId]);
      let favIds = userResult.rows[0].fav_ids || [];
      if (!favIds.includes(productId)) {
          favIds.push(productId);
          await db.query("UPDATE users SET fav_ids = $1 WHERE id = $2", [favIds, userId]);
      }
      res.json({ message: "Produsul a fost adăugat la favorite" });
  } catch (error) {
      console.error("Eroare la adăugarea produsului la favorite:", error);
      res.status(500).json({ error: "Eroare la server" });
  }
});

// Sterge un produs de la favorite
app.delete("/api/favorites/:productId", async (req, res) => {
  const userId = req.user.id;
  const { productId } = req.params;
  try {
      const userResult = await db.query("SELECT fav_ids FROM users WHERE id = $1", [userId]);
      let favIds = userResult.rows[0].fav_ids || [];
      favIds = favIds.filter(id => id !== parseInt(productId, 10));
      await db.query("UPDATE users SET fav_ids = $1 WHERE id = $2", [favIds, userId]);
      res.json({ message: "Produsul a fost eliminat din favorite" });
  } catch (error) {
      console.error("Eroare la ștergerea produsului din favorite:", error);
      res.status(500).json({ error: "Eroare la server" });
  }
});

// Plaseaza o comanda noua
app.post("/api/orders", async (req, res) => {
  const {
    firstName,
    lastName,
    address,
    paymentMethod,
    items,
    totalPrice,
  } = req.body;

  const userId = req.user.id; 

  try {
    const orderNo = parseInt(new Date().getTime() / 1000) + userId;
    const insertOrderQuery = `
      INSERT INTO orders (order_no, user_id, product_ids, lastname, firstname, payment_method, address, total_price, products, time)
      VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, NOW())
      RETURNING id, order_no, user_id, product_ids, lastname, firstname, payment_method, address, total_price, products, time
    `;
    const orderValues = [orderNo, userId, items.map(item => item.productId), lastName, firstName, paymentMethod, address, totalPrice, JSON.stringify(items)];
    const insertedOrder = await db.query(insertOrderQuery, orderValues);
    const orderDetails = insertedOrder.rows[0];

    res.status(201).json({ message: 'Comanda a fost plasată cu succes!', orderDetails });
  } catch (error) {
    console.error('Eroare la plasarea comenzii:', error);
    res.status(500).json({ error: 'Eroare la server' });
  }
});

// Comenzile utilizatorului
app.get("/api/orders/user", async (req, res) => {
  const id = req.user.id;
  try {
    const result = await db.query("SELECT * FROM orders WHERE user_id = $1", [parseInt(id)]);
    if (result.rows.length === 0) {
      res.status(404).json({ error: "Nu au fost găsite comenzi pentru acest utilizator" });
    } else {
      res.json(result.rows);
    }
  } catch (error) {
    console.error("Eroare la preluarea comenzilor din baza de date:", error);
    res.status(500).json({ error: "Eroare la server" });
  }
});

// Comanda cu numarul: order_no
app.get("/api/orders/:order_no", async (req, res) => {
  const orderNo = req.params.order_no;

  try {
    const orderQuery = `
      SELECT * FROM orders WHERE order_no = $1
    `;
    const orderResult = await db.query(orderQuery, [orderNo]);

    if (orderResult.rows.length === 0) {
      return res.status(404).json({ error: "Nu au fost găsite comenzi pentru acest număr de comandă" });
    }

    const orderDetails = orderResult.rows[0];
    const items = orderDetails.products;

    const productIds = items.map(item => item.productId);
    const productsQuery = `
      SELECT id, name AS productName, category, image_urls, brand, price
      FROM products
      WHERE id = ANY($1)
    `;
    const productsResult = await db.query(productsQuery, [productIds]);

    const productLookup = productsResult.rows.reduce((lookup, product) => {
      lookup[product.id] = product;
      return lookup;
    }, {});

    const enrichedItems = items.map(item => ({
      ...item,
      ...productLookup[item.productId],
      imageUrl: productLookup[item.productId].image_urls[0] 
    }));

    res.json({
      ...orderDetails,
      items: enrichedItems, 
    });
  } catch (error) {
    console.error("Eroare la preluarea comenzii din baza de date:", error);
    res.status(500).json({ error: "Eroare la server" });
  }
});

// O data pe minut, verifica si actualizeaza, daca e nevoie, starea comenzilor
// La plasarea comenzii: Processing
// Dupa 1 minut: Confirmed
// Dupa 1 ora: On the way
// Dupa 2 zile: Delivered
cron.schedule('*/1 * * * *', async () => {
  const updateOrderStatusQuery = `
  UPDATE orders
  SET status = CASE
    WHEN status = 'Processing' AND NOW() - INTERVAL '1 minute' >= time THEN 'Confirmed'
    WHEN status = 'Confirmed' AND NOW() - INTERVAL '1 hour' >= time THEN 'On the way'
    WHEN status = 'On the way' AND NOW() - INTERVAL '2 days' >= time THEN 'Delivered'
    ELSE status
  END
  WHERE status IN ('Processing', 'Confirmed', 'On the way')
`;
  try {
    await db.query(updateOrderStatusQuery);
  } catch (error) {
    console.error('Eroare la actualizarea stării comenzilor:', error);
  }
});

// Toate comenzile, pentru admin (filtrate)
app.get('/api/admin/orders', async (req, res) => {
  try {
    const activeOrdersQuery = `
      SELECT * FROM orders
      WHERE status IN ('Processing', 'Confirmed')
      ORDER BY time DESC
    `;
    const activeOrders = await db.query(activeOrdersQuery);

    const sentOrdersQuery = `
      SELECT * FROM orders
      WHERE status IN ('On the way', 'Delivered', 'Canceled')
      ORDER BY time DESC
    `;
    const sentOrders = await db.query(sentOrdersQuery);

    res.json({ activeOrders: activeOrders.rows, sentOrders: sentOrders.rows });
  } catch (error) {
    console.error('Eroare la preluarea comenzilor pentru Admin Control Panel:', error);
    res.status(500).json({ error: 'Eroare la server' });
  }
});

// Modifica statusul comenzii - admin
app.patch('/api/admin/orders/:orderId', async (req, res) => {
  const { orderId } = req.params;
  const { status } = req.body;

  try {
    const updateOrderQuery = `
      UPDATE orders
      SET status = $1
      WHERE id = $2
      RETURNING *
    `;
    const result = await db.query(updateOrderQuery, [status, orderId]);

    if (result.rowCount === 0) {
      return res.status(404).json({ error: 'Order not found' });
    }

    res.json({ message: 'Order status updated successfully', updatedOrder: result.rows[0] });
  } catch (error) {
    console.error('Error updating order status:', error);
    res.status(500).json({ error: 'Server error' });
  }
});

// Adauga un produs nou - admin
app.post('/api/admin/add', async (req, res) => {
  const userId = req.user.id;
  const {
    name,
    description,
    price,
    category,
    image_urls,
    brand,
    quantity,
    size,
    gender
  } = req.body;

  if (userId !== 21) {
    return res.status(401).json({ error: 'Only online admins can make changes.' });
  }

  const allowedCategories = ['t-shirts', 'jeans', 'pants', 'jackets', 'shoes', 'shirts', 'watches', 'sunglasses'];
  if (!allowedCategories.includes(category)) {
    return res.status(400).json({ error: 'Invalid category' });
  }

  try {
    const imageUrlsArray = image_urls.split(',');

    const response = await db.query(
      "INSERT INTO products (name, description, price, category, image_urls, brand, quantity, size, gender) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9) RETURNING *",
      [name, description, price, category, imageUrlsArray, brand, quantity, size, gender]
    );
    const data = response.rows[0]; 

    res.status(201).json({ message: "Item added successfully", id: data.id });
  } catch (error) {
    console.error('Error adding new product', error);
    res.status(500).json({ error: 'Server error' });
  }
});

// Actualizeaza un produs existent - admin
app.patch('/api/admin/update/:id', async (req, res) => {
  const productId = req.params.id;
  const { updatedPart, change } = req.body;

  try {
    let updateQuery = '';
    let values = [];

    switch (updatedPart) {
      case 'name':
      case 'description':
      case 'category':
      case 'brand':
      case 'size':
      case 'gender':
        updateQuery = `UPDATE products SET ${updatedPart} = $1 WHERE id = $2 RETURNING *`;
        values = [change, productId];
        break;
      case 'price':
      case 'quantity':
        updateQuery = `UPDATE products SET ${updatedPart} = $1 WHERE id = $2 RETURNING *`;
        values = [parseFloat(change), productId];
        break;
      case 'image_urls':
        updateQuery = `UPDATE products SET ${updatedPart} = ARRAY[$1] WHERE id = $2 RETURNING *`;
        values = [change.split(','), productId];
        break;
      default:
        return res.status(400).json({ error: 'Invalid update part' });
    }

    const { rows } = await db.query(updateQuery, values);

    if (rows.length === 0) {
      return res.status(404).json({ error: `Product with ID ${productId} not found` });
    }

    res.json({ message: `Product with ID ${productId} updated successfully`, product: rows[0] });
  } catch (error) {
    console.error('Error updating product:', error);
    res.status(500).json({ error: 'Server error' });
  }
});

// Sterge un produs existent - admin
app.delete('/api/admin/delete/:id', async (req, res) => {
  const productId = req.params.id;

  try {
    const deleteQuery = 'DELETE FROM products WHERE id = $1 RETURNING *';
    const { rows } = await db.query(deleteQuery, [productId]);

    if (rows.length === 0) {
      return res.status(404).json({ error: `Product with ID ${productId} not found` });
    }

    res.json({ message: `Product with ID ${productId} deleted successfully` });
  } catch (error) {
    console.error('Error deleting product:', error);
    res.status(500).json({ error: 'Server error' });
  }
});

// Plata Stripe
app.post('/api/plata', async (req, res) => {
  const { paymentMethodId, amount, currency } = req.body;

  try {
    const paymentIntent = await stripe.paymentIntents.create({
      amount,
      currency,
      payment_method: paymentMethodId,
      confirm: true,
    });

    res.status(200).json({ success: true, paymentIntent });
  } catch (error) {
    console.error('Eroare la procesarea plății:', error);
    res.status(500).json({ error: 'Eroare la procesarea plății' });
  }
});

// Run server
app.listen(port, () => {
  console.log(`Serverul rulează pe portul ${port}`);
});
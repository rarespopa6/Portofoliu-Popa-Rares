import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, useLocation } from "react-router-dom";
import Header from "./Header";
import Footer from "./Footer";
import Login from "./Login";
import Register from "./Register";
import Product from "./Product";
import CreateArea from "./CreateArea";
import Navbar from "./Navbar";
import { Nav } from "react-bootstrap";
import Info from "./Info";
import Products from "./Products";
import ProductDetails from "./ProductDetails";
import AddToCartButton from "./AddToCartButton";
import MyCartPage from "./MyCartPage";
import FavoritesPage from "./FavoritesPage";
import ConfirmOrder from "./ConfirmOrder";
import OrderConfirmation from "./OrderConfirmation";
import MyOrders from "./MyOrders";
import Security from "./Security";
import Contact from './Contact';
import ReturnPolicy from "./ReturnPolicy";
import About from "./About";
import AdminControlPanel from "./AdminControlPanel";
import AddProduct from "./AddProduct";
import UpdateProductForm from "./UpdateProduct";
import DeleteProductForm from "./DeleteProduct";
import NotFound from "./NotFound";
import { loadStripe } from '@stripe/stripe-js';
import { Elements } from '@stripe/react-stripe-js';

const stripePromise = loadStripe('your_stripe_publishable_key');

function ScrollToTop() {
  const { pathname } = useLocation();

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [pathname]);

  return null;
}

function App() {
  const [authenticated, setAuthenticated] = useState(false);
  const [products, setProducts] = useState([]);
  const [userId, setUserId] = useState();

  useEffect(() => {
    const checkUser = async () => {
      try {
        const response = await fetch("http://localhost:3001/api/user", {
          credentials: "include"
        });
        const data = await response.json();
        if (data.isAuthenticated) {
          setAuthenticated(true);
          setUserId(data.user.id);
          fetchProducts(); 
        } else {
          setAuthenticated(false);
          setProducts([]); 
        }
      } catch (error) {
        console.error("Eroare la verificarea utilizatorului:", error);
      }
    };
  
    checkUser();
  }, []);

  const fetchProducts = async () => {
    try {
      const response = await fetch("http://localhost:3001/api/products", {
        credentials: "include"
      });
      const data = await response.json();
      setProducts(data);
      console.log(data);
    } catch (error) {
      console.error("Eroare la preluarea produselor:", error);
    }
  };

  const handleLogout = async () => {
    try {
      const response = await fetch("http://localhost:3001/api/logout", {
        method: "GET",
        credentials: "include",
      });

      const data = await response.json();
      if (data.success) {
        setAuthenticated(false);
        window.location.reload();
      }
    } catch (error) {
      console.error("Eroare la logout:", error);
    }
  };

  return (
    <Elements stripe={stripePromise}>
      <Router>
        <ScrollToTop />
          <div>
            <Header authenticated={authenticated} onLogout={handleLogout} userId={userId}/>
            <div className="content">
            <Navbar />
              <Routes>
                <Route path="/login" element={<Login onLogin={() => setAuthenticated(true)} />} />
                <Route path="/register" element={<Register />} />
                <Route path="/" element={
                  (
                    <>
                      <CreateArea />
                      <Info />
                    </>
                  ) 
                } />
                <Route path="/watches" element={ <Products category="watches" />} />
                <Route path="/sunglasses" element={ <Products category="sunglasses" />} />
                <Route path="/t-shirts" element={ <Products category="t-shirts" />} />
                <Route path="/jeans" element={ <Products category="jeans" />} />
                <Route path="/pants" element={ <Products category="pants" />} />
                <Route path="/jackets" element={ <Products category="jackets" />} />
                <Route path="/shoes" element={ <Products category="shoes" />} />
                <Route path="/shirts" element={ <Products category="shirts" />} />
                <Route path="/:category/:id_produs" element={<ProductDetails />} />
                <Route path="/cart" element={<MyCartPage />} />
                <Route path="/favorites" element={<FavoritesPage />} />
                <Route path="/confirm-order" element={<ConfirmOrder />} />
                <Route path="/order/:order_no" element={<OrderConfirmation />} />
                <Route path="/my-orders" element={<MyOrders />} />
                <Route path="/security" element={<Security />} />
                <Route path="/contact" element={<Contact />} />
                <Route path="/return-policy" element={<ReturnPolicy />} />
                <Route path="/about" element={<About />} />
                <Route path="/admin" element={<AdminControlPanel />} />
                <Route path="/admin/add" element={<AddProduct />} />
                <Route path="/admin/update" element={<UpdateProductForm />} />
                <Route path="/admin/delete" element={<DeleteProductForm />} />
                <Route path="*" element={<NotFound />} />
              </Routes>
            </div>
            <Footer />
          </div>
        </Router>
      </Elements>
  );
}

export default App;

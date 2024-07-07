import React, { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { FaCcVisa, FaCcMastercard } from "react-icons/fa";
import { TbTruckDelivery } from "react-icons/tb";
import { CardElement, useStripe, useElements } from '@stripe/react-stripe-js';
import { Elements } from '@stripe/react-stripe-js';
import { loadStripe } from '@stripe/stripe-js';
import PaymentForm from "./PaymentForm";

const stripePromise = loadStripe('your_stripe_publishable_key');

function ConfirmOrder() {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [address, setAddress] = useState("");
  const [paymentMethod, setPaymentMethod] = useState("");
  const [cartItems, setCartItems] = useState([]);
  const [totalPrice, setTotalPrice] = useState(0);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);
  const [deliveryPrice, setDeliveryPrice] = useState(0);

  const [authenticated, setAuthenticated] = useState(false);
  const [isCardSelected, setIsCardSelected] = useState(false);
  const [isDeliverySelected, setIsDeliverySelected] = useState(false);
  const navigate = useNavigate();
  const stripe = useStripe();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const userResponse = await fetch("http://localhost:3001/api/user", {
          credentials: "include",
        });
        const userData = await userResponse.json();
        setAuthenticated(userData.isAuthenticated);
        if (userData.isAuthenticated) {
          fetchCartItems();
        } else {
          navigate("/login");
        }
      } catch (error) {
        console.error("Eroare la verificarea utilizatorului:", error);
      }
    };

    const fetchCartItems = async () => {
      try {
        const response = await fetch("http://localhost:3001/api/cart", {
          credentials: "include",
        });
        const data = await response.json();
        if (response.ok) {
          setCartItems(data);
          const totalPrice = calculateTotalPrice(data); 
          setDeliveryPrice(totalPrice >= 200 ? 0 : 20);
          setTotalPrice(totalPrice);
          setLoading(false);
        } else {
          console.error("Eroare la preluarea coșului:", data.error);
        }
      } catch (error) {
        console.error("Eroare la preluarea coșului:", error);
      }
    };

    fetchData();
  }, [navigate]);

  useEffect(() => {
      if (!loading && totalPrice == 0 && authenticated){
        navigate("/");
        return;
      }
  });


  const handleFirstNameChange = (e) => {
    setFirstName(e.target.value);
  };

  const handleLastNameChange = (e) => {
    setLastName(e.target.value);
  };

  const handleAddressChange = (e) => {
    setAddress(e.target.value);
  };

  const handlePaymentMethodChange = (method) => {
    setPaymentMethod(method);
    if (method === "Card") {
      setIsCardSelected(true);
      setIsDeliverySelected(false);
    } else if (method === "Delivery") {
      setIsCardSelected(false);
      setIsDeliverySelected(true);
    }
  };

  const handleSubmitOrder = async () => {
    if (
      firstName.trim() === "" ||
      lastName.trim() === "" ||
      address.trim() === "" ||
      paymentMethod === "" ||
      cartItems.length === 0
    ) {
      setError("All fields are required, including the payment method.");
      window.scrollTo(0, 0);
      return;
    }
  
    try {
      const orderData = {
        firstName,
        lastName,
        address,
        paymentMethod,
        items: cartItems.map((item) => ({
          productId: item.id,
          quantity: item.quantity,
          price: item.price,
          size: item.size,
        })),
        deliveryPrice,
        totalPrice,
      };
  
      const response = await fetch("http://localhost:3001/api/orders", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(orderData),
        credentials: "include",
      });
  
      const data = await response.json();
  
      console.log('Order response:', data);
  
      if (response.ok) {
        const orderDetails = data.orderDetails;
        const itemsWithDetails = await Promise.all(
          orderDetails.products.map(async (item) => { 
            const productResponse = await fetch(`http://localhost:3001/api/products/id/${item.productId}`);
            const productData = await productResponse.json();
            return {
              ...item,
              productName: productData.name,
              brand: productData.brand,
              imageUrl: productData.image_urls[0],
              category: productData.category,
            };
          })
        );
  
        const deleteResponse = await fetch("http://localhost:3001/api/cart", {
          method: "DELETE",
          credentials: "include",
        });
  
        if (deleteResponse.ok) {
          console.log("Conținutul coșului a fost șters cu succes.");
        } else {
          console.error("Eroare la ștergerea conținutului coșului:", deleteResponse.statusText);
        }
  
        navigate(`/order/${orderDetails.order_no}`, { state: { orderDetails: { ...orderDetails, products: itemsWithDetails } } }); // Actualizează de la `items` la `products`
      } else {
        console.error("Eroare la plasarea comenzii:", data.error);
      }
    } catch (error) {
      console.error("Eroare la plasarea comenzii:", error);
    }
  };
  
  

  useEffect(() => {
    setTotalPrice(parseFloat(calculateTotalPrice(cartItems)) + parseFloat(deliveryPrice));
  }, [deliveryPrice, cartItems]);
  

  function calculateTotalPrice(items) {
    let total = 0;
    items.forEach((item) => {
      total += parseFloat(item.price) * parseFloat(item.quantity);
    });
    return total.toFixed(2); 
  }

  if (loading) {
    return <div>Loading...</div>; 
  }

  return (
    <div className="confirm-order">
      <h2>Confirm Order</h2>
      {error && <p className="error-message">{error}</p>}
      <div className="order-summary">
        <h3>Order Summary</h3>
        <ul>
          {cartItems.map((item) => (
            <div style={{display: 'flex', justifyContent: 'space-between'}}>
            <li key={item.id}>
              <p>
                {item.name} - {item.price} Lei
              </p>
              <p>Quantity: {item.quantity}</p>
              <p>{item.size ? (`Size: ${item.size}`) : ""}</p>
            </li>
            <img className="small-photo" src={`/imgs/${item.category}/${item.image_urls[0]}`} alt={item.productName} />
            </div>
          ))}
          <li>
          <p>Delivery cost: {(deliveryPrice == 0) ? "Free" : deliveryPrice + " Lei"}</p>
          </li>
        </ul>
        <div className="total-price">
          <p>Total Price: {totalPrice} Lei</p>
        </div>
      </div>
      <div className="billing-form">
        <h3>Billing Information</h3>
        <input
          type="text"
          placeholder="First Name"
          value={firstName}
          onChange={handleFirstNameChange}
        />
        <input
          type="text"
          placeholder="Last Name"
          value={lastName}
          onChange={handleLastNameChange}
        />
        <textarea
          placeholder="Address"
          value={address}
          onChange={handleAddressChange}
        ></textarea>
      </div>
      <div className="payment-options">
        <h3>Payment Options</h3>
        <button
          className={isCardSelected ? "selected" : ""}
          onClick={() => handlePaymentMethodChange("Card")}
        >
          Card Payment <FaCcVisa /> <FaCcMastercard /> 
        </button>
        <button
          className={isDeliverySelected ? "selected" : ""}
          onClick={() => handlePaymentMethodChange("Delivery")}
        >
          Delivery Payment <TbTruckDelivery />
        </button>
      </div>
      {isCardSelected && (
        <div className="card-payment-form">
          <h3>Card Payment Form</h3>
          <Elements stripe={stripePromise}>
            <PaymentForm amount={totalPrice}/>
          </Elements>
        </div>
      )}
      <div className="confirmation-buttons">
      <Link to="/cart" className="cancel-button">Back</Link>
        <button className="auth-button" onClick={handleSubmitOrder}>
          Send Order
        </button>
      </div>
    </div>
  );
}

export default ConfirmOrder;

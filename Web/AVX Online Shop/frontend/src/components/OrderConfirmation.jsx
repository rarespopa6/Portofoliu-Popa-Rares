import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';

function OrderConfirmation() {
  const { order_no } = useParams();
  const [orderDetails, setOrderDetails] = useState(null);
  const [authenticated, setAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const userResponse = await fetch("http://localhost:3001/api/user", {
          credentials: "include",
        });
        const userData = await userResponse.json();
        setAuthenticated(userData.isAuthenticated);
        if (!userData.isAuthenticated) {
          navigate("/login");
          return;
        }
      } catch (error) {
        console.error("Eroare la verificarea utilizatorului:", error);
      }
    };

    const fetchOrderDetails = async () => {
      try {
        if (!order_no) {
          setLoading(false);
          return;
        }

        const response = await fetch(`http://localhost:3001/api/orders/${order_no}`);
        if (!response.ok) {
          throw new Error('Failed to fetch order details');
        }
        const data = await response.json();
        setOrderDetails(data);
        setLoading(false);
      } catch (error) {
        console.error('Error fetching order details:', error);
        setLoading(false);
      }
    };

    fetchData();
    fetchOrderDetails();
  }, [order_no, navigate]);

  if (!authenticated) {
    return null; 
  }

  if (loading) {
    return <p>Loading...</p>; 
  }

  if (!orderDetails) {
    navigate("/"); 
    return null;
  }

  return (
    <div className="order-confirmation-container">
      <h1 className="order-confirmation-header">Thank you for your order!</h1>
      <h2 className="order-confirmation-subheader">Order details</h2>
      <div className="order-details">
        <p>Order number: <span>{orderDetails.order_no}</span></p>
        <p>Order status: <span>{orderDetails.status}</span></p>
        <p>Name: <span>{orderDetails.firstname} {orderDetails.lastname}</span></p>
        <p>Address: <span>{orderDetails.address}</span></p>
        <p>Payment method: <span>{orderDetails.payment_method}</span></p>
        <p>Total price: <span>{orderDetails.total_price} RON</span></p>
      </div>
      <h3 className="order-confirmation-subheader">Ordered items</h3>
      <div className="order-products">
        {orderDetails.items.map((item, index) => (
          <div key={index} className="order-product-item">
            <img src={`/imgs/${item.category}/${item.imageUrl}`} alt={item.productName} />
            <div className="product-name">{item.productName}</div>
            <div className="product-brand">{item.brand}</div>
            <div className="product-size" style={{ marginTop: !item.size ? '30px' : '0px' }}>{item.size}</div>
            <div className="product-quantity">{item.quantity}</div>
            <div className="product-price">{item.price} Lei</div>
          </div>
        ))}
      </div>
      <div className="thank-you-message">We hope to see you back soon!</div>
      <Link to="/" className='auth-button' style={{ margin: '25px' }}>Home</Link>
    </div>
  );
};

export default OrderConfirmation;

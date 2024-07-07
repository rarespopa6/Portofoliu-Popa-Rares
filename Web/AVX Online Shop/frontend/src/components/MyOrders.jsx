import React, { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { RiShoppingBag4Line } from "react-icons/ri";

function MyOrders() {
  const [authenticated, setAuthenticated] = useState(false);
  const [orders, setOrders] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const userResponse = await fetch("http://localhost:3001/api/user", {
          credentials: "include",
        });
        const userData = await userResponse.json();
        setAuthenticated(userData.isAuthenticated);
        if (userData.isAuthenticated) {
          fetchOrders();
        } else {
          navigate("/login");
        }
      } catch (error) {
        console.error("Eroare la verificarea utilizatorului:", error);
      }
    };

    const fetchOrders = async () => {
        try {
          const response = await fetch(`http://localhost:3001/api/orders/user`, {
            credentials: "include",
          });
          const data = await response.json();
          if (response.ok) {
            setOrders(data);
          } else {
            console.error("Eroare la preluarea comenzilor:", data.error);
          }
        } catch (error) {
          console.error("Eroare la preluarea comenzilor:", error);
        }
      };
      
    fetchData();
  }, [navigate]);

  const handleCancelOrder = async (orderId) => {
    try {
      const response = await fetch(`http://localhost:3001/api/admin/orders/${orderId}`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ status: 'Canceled' }),
      });
      if (!response.ok) {
        throw new Error('Failed to cancel order');
      }
      window.location.reload();
    } catch (error) {
      console.error('Error canceling order:', error);
    }
  };

  if (!authenticated){
    navigate("/");
    return null;
  }

  return (
    <div className="my-orders">
      <h2 className="big-title">My Orders</h2>
      {orders.length === 0 ? (
        <div className="empty-cart">
          <span role="img" aria-label="shopping cart" className="cart-emoji"><RiShoppingBag4Line /></span>
          <p className="empty-cart-message">You don't have any orders yet.</p>
      </div>
      ) : (
        <ul style={orders.length == 1 ? {marginBottom: '200px'} : {}}>
           {orders.slice().sort((a, b) => new Date(b.time) - new Date(a.time)).map((order) => (
            <li key={order.id}>
              <h3>Order No: {order.order_no}</h3>
              <p>Name: {order.firstname} {order.lastname}</p>
              <p>Address: {order.address}</p>
              <p>Payment Method: {order.payment_method}</p>
              <p>Total Price: {order.total_price} Lei</p>
              <p>Order Time: {new Date(order.time).toLocaleDateString('ro-RO')} {new Date(order.time).toLocaleTimeString('ro-RO')}</p>
              <p>Order Status: {order.status}</p>
              <div style={{display: 'flex', justifyContent: 'space-between'}}>
              <Link to={`/order/${order.order_no}`} className="order-details-button">Order details</Link>
              {order.status == 'Processing' || order.status == 'Confirmed' ? (
              <button className="cancel-order-button" onClick={() => handleCancelOrder(order.id)}>
                    Cancel Order
              </button>
              ) : <></>}
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default MyOrders;

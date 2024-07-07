import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { TbTruckDelivery } from "react-icons/tb";

function AdminControlPanel() {
  const [activeOrders, setActiveOrders] = useState([]);
  const [sentOrders, setSentOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [authenticated, setAuthenticated] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const userResponse = await fetch('http://localhost:3001/api/user', {
          credentials: 'include',
        });
        const userData = await userResponse.json();
        setAuthenticated(userData.isAuthenticated);
        if (!userData.isAuthenticated || userData.user.id != 21) {
          navigate('/login');
        }
      } catch (error) {
        console.error('Eroare la verificarea utilizatorului:', error);
      }
    };

    const fetchOrders = async () => {
      try {
        const response = await fetch('http://localhost:3001/api/admin/orders');
        if (!response.ok) {
          throw new Error('Failed to fetch orders');
        }
        const data = await response.json();
        setActiveOrders(data.activeOrders);
        setSentOrders(data.sentOrders);
        setLoading(false);
      } catch (error) {
        console.error('Error fetching orders for Admin Control Panel:', error);
      }
    };

    fetchData();
    fetchOrders();
  }, []);

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
      const updatedActiveOrders = activeOrders.map((order) => {
        if (order.id === orderId) {
          return { ...order, status: 'Canceled' };
        }
        return order;
      });
      setActiveOrders(updatedActiveOrders);
      window.location.reload();
    } catch (error) {
      console.error('Error canceling order:', error);
    }
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <Link className='auth-button' to="/admin/add">Add Products</Link>
      <Link className='auth-button' to="/admin/update">Update Products</Link>
      <Link className='auth-button' to="/admin/delete">Delete Products</Link>
    <div className="admin-control-panel">
      <div className="active-orders">
        <h2 style={{marginLeft: '10px'}}>Active Orders ({activeOrders.length})</h2>
        <ul>
          {activeOrders.map((order) => (
            <li key={order.id}>
              <p>Order No: <Link to={`/order/${order.order_no}`}>{order.order_no}</Link></p>
              <p>Name: {order.firstname} {order.lastname}</p>
              <p>Product IDs: {order.product_ids.join(', ')}</p>
              <p>Order Time: {new Date(order.time).toLocaleDateString('ro-RO')} {new Date(order.time).toLocaleTimeString('ro-RO')}</p>
              <p style={{color: (order.status=='Processing') ? '#8B8000' : 'orange'}}>Status: {order.status}</p>  
                {order.status !== 'Canceled' && (
                    <button className="cancel-button" onClick={() => handleCancelOrder(order.id)}>
                    Cancel Order
                    </button>
                )}
            </li>
          ))}
        </ul>
      </div>
      <div className="sent-orders">
        <h2>Sent/Canceled Orders ({sentOrders.length})</h2>
        <ul>
          {sentOrders.map((order) => (
            <li key={order.id}>
              <p>Order No: <Link to={`/order/${order.order_no}`}>{order.order_no}</Link></p>
              <p>Name: {order.firstname} {order.lastname}</p>
              <p>Product IDs: {order.product_ids.join(', ')}</p>
              <p>Order Time: {new Date(order.time).toLocaleDateString('ro-RO')} {new Date(order.time).toLocaleTimeString('ro-RO')}</p>
              <p style={{color: (order.status=='Canceled') ? 'red' : 'green'}}>Status: {order.status}</p>
              <button className="truck-button">
                 <TbTruckDelivery style={{ fontSize: '20px', color: 'white' }} />  
                </button>
            </li>
          ))}
        </ul>
      </div>
    </div>
    </div>
  );
}

export default AdminControlPanel;

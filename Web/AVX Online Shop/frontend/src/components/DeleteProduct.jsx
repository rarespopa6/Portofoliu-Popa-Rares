import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';

function DeleteProductForm(){
  const [productId, setProductId] = useState('');
  const [message, setMessage] = useState('');
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
    fetchData();
}, [navigate]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    
    try {
        const response = await fetch(`http://localhost:3001/api/admin/delete/${productId}`, {
          method: 'DELETE',
        });
  
        if (response.ok) {
          setMessage(`Product with ID ${productId} has been successfully deleted.`);
        } else {
          setMessage(`Product with ID ${productId} does not exist.`);
        }
      } catch (error) {
        console.error('Error deleting product:', error);
        setMessage(`Error deleting product with ID ${productId}. Please try again.`);
      }
  };

  const handleChange = (event) => {
    setProductId(event.target.value);
  };

  if (!authenticated){
    navigate("/");
    return null;
  }

  return (
    <div className="form-container" style={{marginTop: '100px', marginBottom: '100px'}}>
      <h2>Delete Product</h2>
      <form onSubmit={handleSubmit}>
        <label>Product ID:</label>
        <input type="text" value={productId} onChange={handleChange} required />
        <button type="submit">Delete Product</button>
      </form>
      <p>{message}</p>
      <Link to="/admin">Back</Link>
    </div>
  );
};

export default DeleteProductForm;

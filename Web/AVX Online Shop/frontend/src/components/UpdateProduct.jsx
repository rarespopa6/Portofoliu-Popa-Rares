import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';

function UpdateProductForm(){
  const [productId, setProductId] = useState('');
  const [updatedPart, setUpdatedPart] = useState('');
  const [change, setChange] = useState('');
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
      const response = await fetch(`http://localhost:3001/api/admin/update/${productId}`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          updatedPart: updatedPart,
          change: change,
        }),
      });
  
      const data = await response.json();
  
      if (response.ok) {
        setMessage(`Product with ID ${productId} has been successfully updated.`);
      } else {
        setMessage(`Product with ID ${productId} does not exist.`);
      }
    } catch (error) {
      console.error('Error updating product:', error);
      setMessage(`Error updating product with ID ${productId}. Please try again.`);
    }
  };
  
  

  const handlePartChange = (event) => {
    setUpdatedPart(event.target.value);
  };

  const handleChange = (event) => {
    setChange(event.target.value);
  };

  if (!authenticated){
    navigate("/");
    return null;
  }

  return (
    <div className="form-container">
      <h2>Update Product</h2>
      <form onSubmit={handleSubmit}>
        <label>Product ID:</label>
        <input type="text" value={productId} onChange={(e) => setProductId(e.target.value)} required />
        
        <label>Update Part:</label>
        <select value={updatedPart} onChange={handlePartChange} required>
          <option value="">Select part to update</option>
          <option value="name">Name</option>
          <option value="description">Description</option>
          <option value="price">Price</option>
          <option value="category">Category</option>
          <option value="image_urls">Image URLs</option>
          <option value="brand">Brand</option>
          <option value="quantity">Quantity</option>
          <option value="size">Size</option>
          <option value="gender">Gender</option>
        </select>
        
        <label>New Value:</label>
        <input type="text" value={change} onChange={handleChange} required />
        
        <button type="submit">Update Product</button>
      </form>
      <p>{message}</p>
      <Link to="/admin">Back</Link>
    </div>
  );
};

export default UpdateProductForm;

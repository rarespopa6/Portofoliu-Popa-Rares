import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';

function AddProduct(){
    const [authenticated, setAuthenticated] = useState(false);
    const [message, setMessage] = useState("");
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

  const [formData, setFormData] = useState({
    name: '',
    description: '',
    price: '',
    category: '',
    image_urls: '',
    brand: '',
    quantity: '',
    size: '',
    gender: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch('http://localhost:3001/api/admin/add', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData),
        credentials: 'include' 
      });


      if (!response.ok) {
        throw new Error('Failed to add item');
      }

      const data = await response.json();

      if (response.ok){
        setMessage(`Item with ID: ${data.id} was added with success.`);
        window.scrollTo(0, 0);
      }
      setFormData({
        name: '',
        description: '',
        price: '',
        category: '',
        image_urls: '',
        brand: '',
        quantity: '',
        size: '',
        gender: ''
      });
    } catch (error) {
      console.error('Error adding item:', error.message);
    }
  };

  if (!authenticated){
    navigate("/");
    return null;
  }

  return (
    <div className="admin-tools-container">
    <p>{message}</p>
    <h2>Add New Product</h2>
    <form onSubmit={handleSubmit}>
      <label>Name:</label>
      <input type="text" name="name" value={formData.name} onChange={handleChange} required />
      
      <label>Description:</label>
      <textarea name="description" value={formData.description} onChange={handleChange} required></textarea>
      
      <label>Price:</label>
      <input type="number" name="price" value={formData.price} onChange={handleChange} required />
      
      <label>Category:</label>
      <select name="category" value={formData.category} onChange={handleChange} required>
        <option value="">Select category</option>
        <option value="t-shirts">T-Shirts</option>
        <option value="jeans">Jeans</option>
        <option value="pants">Pants</option>
        <option value="jackets">Jackets</option>
        <option value="shoes">Shoes</option>
        <option value="shirts">Shirts</option>
        <option value="watches">Watches</option>
        <option value="sunglasses">Sunglasses</option>
      </select>
      
      <label>Image URLs (comma separated, ex: rolex_1.jpg,rolex_2.jpg):</label>
      <input type="text" name="image_urls" value={formData.image_urls} onChange={handleChange} required />
      
      <label>Brand:</label>
      <input type="text" name="brand" value={formData.brand} onChange={handleChange} required />
      
      <label>Quantity:</label>
      <input type="number" name="quantity" value={formData.quantity} onChange={handleChange} required />
      
      <label>Size:</label>
      <input type="text" name="size" value={formData.size} onChange={handleChange} required />
      
      <label>Gender:</label>
      <select name="gender" value={formData.gender} onChange={handleChange} required>
        <option value="">Select gender</option>
        <option value="Men">Men</option>
        <option value="Women">Women</option>
       </select>
      
      <button type="submit">Add Product</button>
    </form>
    <Link to="/admin">Back</Link>
  </div>
  );
};

export default AddProduct;

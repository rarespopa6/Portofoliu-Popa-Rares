import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import Zoom from 'react-medium-image-zoom';
import 'react-medium-image-zoom/dist/styles.css';
import AddToCartButton from "./AddToCartButton";
import AddToFavoriteButton from "./AddToFavoriteButton";
import SizeButton from "./SizeButton";

function ProductDetails() {
  const { category, id_produs } = useParams();
  const [authenticated, setAuthenticated] = useState(false);
  const [product, setProduct] = useState(null);
  const [selectedSize, setSelectedSize] = useState('');
  const [message, setMessage] = useState("");

  useEffect(() => {
      const fetchData = async () => {
        try {
          const userResponse = await fetch("http://localhost:3001/api/user", {
            credentials: "include",
          });
          const userData = await userResponse.json();
          if (userData.isAuthenticated) {
            setAuthenticated(true);
            fetchProduct();
          } else {
            setAuthenticated(false);
          }
        } catch (error) {
          console.error("Eroare la verificarea utilizatorului:", error);
        }
      };
  
      const fetchProduct = async () => {
        try {
          const response = await fetch(
            `http://localhost:3001/api/products/id/${id_produs}`,
            {
              credentials: "include",
            }
          );
          const data = await response.json();
          setProduct(data);
          console.log(data);
        } catch (error) {
          console.error("Eroare la preluarea produselor:", error);
        }
      };
  
      fetchData(); 
      fetchProduct();
  
    }, [id_produs]);

    if (!product) {
      return <p>Loading...</p>;
    }

    const img_urls = product.image_urls.map(url => `/imgs/${product.category}/${url}`);

return (
  <div className="product-descr-all">
    <div className="product-descr">
      <div className="product-descr-img-container">
        {img_urls.map((img_src, index) => (
                  <Zoom key={index}>
                    <img key={index} className="product-descr-img" src={img_src} alt={`Imagine ${index + 1}`} />
                  </Zoom>
        ))}
      </div>
      <div className="product-descr-right">
        <p className="product-descr-price" style={{color: '#555'}}>{product.gender}'s {product.category}</p>
        <p className="product-descr-title">{product.name}</p>
        <p className="product-descr-title">{product.brand}</p>
        <p className="product-descr-price">{product.price} lei</p>
        <div className="product-action-messages">
        {message && <p className="message success">{message}</p>}
      </div>
        {(product.category != 'watches' && product.category != 'accessories' && product.category != 'sunglasses') ? (
        <SizeButton onSizeSelect={setSelectedSize} category={product.category} />
        ) : <></> }
        <div className="product-action-buttons">
          <AddToCartButton 
            productId={product.id} 
            productName={product.name} 
            price={product.price} 
            imageUrl={product.image_urls[0]} 
            category={product.category}
            selectedSize={selectedSize}
            setMessage={setMessage}
          />
          <AddToFavoriteButton productId={product.id} size={product.selectedSize} setMessage={setMessage}/>
        </div>
      </div>
    </div>
    <p className="product-descr-description-title">Description</p>
    <p className="product-descr-description">{product.description}</p>
  </div>
);
}

export default ProductDetails;
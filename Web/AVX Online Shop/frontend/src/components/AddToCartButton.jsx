import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

function AddToCartButton(props){
  const [authenticated, setAuthenticated] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
      const fetchData = async () => {
        try {
          const userResponse = await fetch("http://localhost:3001/api/user", {
            credentials: "include",
          });
          const userData = await userResponse.json();
          if (userData.isAuthenticated) {
            setAuthenticated(true);
          } else {
            setAuthenticated(false);
          }
        } catch (error) {
          console.error("Eroare la verificarea utilizatorului:", error);
        }
      };
      fetchData(); 
  }, []);

  const handleAddToCart = async () => {
    if (!authenticated) {
        navigate("/login");
    } else if (!props.selectedSize && props.category != 'watches' && props.category != 'sunglasses') {
        props.setMessage("Please select a size before adding to cart.");
    } else {
        try {
            const productId = props.productId;
            const response = await fetch("http://localhost:3001/api/cart", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ productId, size: props.selectedSize }),
                credentials: "include",
            });
            const data = await response.json();
            if (response.ok) {
                props.setMessage("The product was added to cart!");
            } else {
                console.error("Eroare la adăugarea în coș:", data.error);
            }
        } catch (error) {
            console.error("Eroare la adăugarea în coș:", error);
        }
    }
};

  return (
      <div>
        <button className="addToCart" onClick={handleAddToCart}>
            Add to cart
        </button>
      </div>
  );
}

export default AddToCartButton;
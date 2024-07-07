import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { FaHeart, FaRegHeart } from "react-icons/fa";

function AddToFavoriteButton(props) {
    const [authenticated, setAuthenticated] = useState(false);
    const [isFavorite, setIsFavorite] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
      const fetchData = async () => {
          try {
              const userResponse = await fetch("http://localhost:3001/api/user", {
                  credentials: "include",
              });
              const userData = await userResponse.json();
              console.log("User data:", userData);
              if (userData.isAuthenticated) {
                  setAuthenticated(true);
  
                  const favoritesResponse = await fetch("http://localhost:3001/api/favorites", {
                      credentials: "include",
                  });
                  const favoriteProducts = await favoritesResponse.json();
                  console.log("Favorite products:", favoriteProducts);
  
                  setIsFavorite(favoriteProducts.some(product => product.id === props.productId));
              } else {
                  setAuthenticated(false);
              }
          } catch (error) {
              console.error("Eroare la verificarea utilizatorului:", error);
          }
      };
      fetchData();
  }, [props.productId]);
  

    const handleAddOrRemoveFromFav = async () => {
        if (!authenticated) {
            navigate("/login");
        } else {
            try {
                const productId = props.productId;
                const url = `http://localhost:3001/api/favorites${isFavorite ? `/${productId}` : ''}`;
                const method = isFavorite ? 'DELETE' : 'POST';

                const response = await fetch(url, {
                    method: method,
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({ productId }),
                    credentials: "include",
                });
                const data = await response.json();
                if (response.ok) {
                    setIsFavorite(!isFavorite);
                    props.setMessage(isFavorite ? "The product was deleted from favorites!" : "The product was added to favorites!");
                } else {
                    console.error("Eroare la modificarea stării favorite", data.error);
                }
            } catch (error) {
                console.error("Eroare la modificarea stării favorite:", error);
            }
        }
    };

    return (
        <button className="addToFav" onClick={handleAddOrRemoveFromFav}>
            <span className="emoji">
                {isFavorite ? <FaHeart /> : <FaRegHeart />}
            </span>
        </button>
    );
}

export default AddToFavoriteButton;

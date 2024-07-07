import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { BsBagHeart } from "react-icons/bs";

function FavoritesPage() {
    const [favItems, setFavItems] = useState([]);
    const [authenticated, setAuthenticated] = useState(false);
    const [message, setMessage] = useState("");
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
                    fetchFavItems();
                } else {
                    navigate("/login");
                }
            } catch (error) {
                console.error("Eroare la verificarea utilizatorului:", error);
            }
        };

        const fetchFavItems = async () => {
            try {
                const response = await fetch("http://localhost:3001/api/favorites", {
                    credentials: "include",
                });
                const data = await response.json();
                if (response.ok) {
                    setFavItems(data);
                } else {
                    console.error("Eroare la preluarea favoritelor:", data.error);
                }
            } catch (error) {
                console.error("Eroare la preluarea favoritelor:", error);
            }
        };

        fetchData();
    }, [navigate]);

    const handleRemoveFromFavorites = async (productId) => {
        try {
            const response = await fetch(`http://localhost:3001/api/favorites/${productId}`, {
                method: "DELETE",
                credentials: "include",
            });
            const data = await response.json();
            if (response.ok) {
                setFavItems(favItems.filter(item => item.id !== productId));
                setMessage("");
            } else {
                console.error("Eroare la ștergerea de la favorite:", data.error);
            }
        } catch (error) {
            console.error("Eroare la ștergerea de la favorite:", error);
        }
    };

    if (!authenticated) {
        return null;
    }

    async function handleAddToCart(productId){
        try {
            const response = await fetch("http://localhost:3001/api/cart", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ productId }),
                credentials: "include",
            });
            const data = await response.json();
            if (response.ok) {
                setMessage("The product was added to cart!");
            } else {
                console.error("Eroare la adăugarea în coș:", data.error);
            }
        } catch (error) {
            console.error("Eroare la adăugarea în coș:", error);
        }
    }
    

    return (
        <div className="my-cart">
            <p className="big-title">Favorites</p>
            <p style={{textAlign: 'center'}}>{message}</p>
            <div className="cart-items">
                {favItems.length === 0 ? (
                    <div className="empty-cart">
                        <span role="img" aria-label="shopping cart" className="cart-emoji"><BsBagHeart /></span>
                        <p className="empty-cart-message">You don't have any favorite items yet.</p>
                    </div>
                ) : (
                    <ul>
                        {favItems.map((item) => (
                            <li key={item.id} style={favItems.length == 1 ? {marginBottom: '200px'} : {}}>
                                <div>
                                    <img src={`/imgs/${item.category}/${item.image_urls[0]}`} alt={item.name} />
                                </div>
                                <div>
                                    <p className="product-title">{item.name}</p>
                                    <p className="product-brand">{item.brand}</p>
                                    <p className="product-quantity">Quantity: 1</p>
                                    <p className="product-price">{item.price} Lei</p>
                                    <button className="action-button" style={{backgroundColor: '#218838', marginRight: '10px'}} onClick={() => handleAddToCart(item.id)}>Add to Cart</button>
                                    <button onClick={() => handleRemoveFromFavorites(item.id)}>Remove</button>
                                </div>
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        </div>
    );
}

export default FavoritesPage;

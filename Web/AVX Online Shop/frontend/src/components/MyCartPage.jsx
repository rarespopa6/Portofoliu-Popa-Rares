import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { HiOutlineShoppingCart } from "react-icons/hi2";

function MyCartPage() {
    const [cartItems, setCartItems] = useState([]);
    const [authenticated, setAuthenticated] = useState(false);
    const [totalPrice, setTotalPrice] = useState(0);
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
                    fetchCartItems();
                } else {
                    navigate("/login");
                }
            } catch (error) {
                console.error("Eroare la verificarea utilizatorului:", error);
            }
        };

        const fetchCartItems = async () => {
            try {
                const response = await fetch("http://localhost:3001/api/cart", {
                    credentials: "include",
                });
                const data = await response.json();
                if (response.ok) {
                    setCartItems(data);
                    calculateTotalPrice(data);
                } else {
                    console.error("Eroare la preluarea coșului:", data.error);
                }
            } catch (error) {
                console.error("Eroare la preluarea coșului:", error);
            }
        };

        fetchData();
    }, [navigate]);
    const handleQuantityChange = (productId, size, newQuantity) => {
        const updatedCartItems = cartItems.map(item => {
            if (item.id === productId && item.size === size) {
                return { ...item, quantity: newQuantity };
            }
            return item;
        });
        setCartItems(updatedCartItems);
        calculateTotalPrice(updatedCartItems);
    };

    function calculateTotalPrice(items) {
        let total = 0;
        items.forEach(item => {
            total += parseFloat(item.price) * item.quantity;
        });
        setTotalPrice(total.toFixed(2));
    }

    const handleRemoveFromCart = async (productId, size) => {
        try {
            const response = await fetch(`http://localhost:3001/api/cart/remove`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ productId, size }),
                credentials: "include",
            });
            const data = await response.json();
            if (response.ok) {
                setCartItems(cartItems.filter(item => !(item.id == productId && item.size == size)));
                calculateTotalPrice(cartItems.filter(item => !(item.id == productId && item.size == size)));
            } else {
                console.error("Eroare la ștergerea din coș:", data.error);
            }
        } catch (error) {
            console.error("Eroare la ștergerea din coș:", error);
        }
    };
    
    if (!authenticated) {
        return null;
    }

    return (
        <div className="my-cart">
            <p className="big-title">My Cart</p>
            <div className="cart-items">
                {cartItems.length === 0 ? (
                    <div className="empty-cart">
                        <span role="img" aria-label="shopping cart" className="cart-emoji"><HiOutlineShoppingCart /></span>
                        <p className="empty-cart-message">Your cart is empty</p>
                    </div>
                ) : (
                    <>
                    <ul>
                        {cartItems.map((item) => (
                            <li key={item.id}>
                                <div>
                                    <img src={`/imgs/${item.category}/${item.image_urls[0]}`} alt={item.name} />
                                </div>
                                <div>
                                    <p className="product-title">{item.name}</p>
                                    <p className="product-brand">{item.brand}</p>
                                    <p className="product-price">{item.price} Lei</p>
                                    <p className="product-price">{item.size ? `Size: ${item.size}` : ""}</p>
                                    <p className="product-brand">Gender: {item.gender}</p>
                                    <p className="product-quantity">Quantity: {item.quantity}</p>
                                    <button onClick={() => handleRemoveFromCart(item.id, item.size)}>Remove</button>
                                </div>
                            </li>
                        ))}
                    </ul>
                    <div className="total-price" style={{border: 'none', width: '81%'}}>
                        <p>Total Price: {totalPrice} Lei</p>
                        <Link className="auth-button" to="/confirm-order">Billing Address and Payment</Link>
                    </div>
                    </>
                )}
            </div>
        </div>
    );
}

export default MyCartPage;

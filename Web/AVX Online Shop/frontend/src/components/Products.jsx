import React, { useState, useEffect } from "react";
import DeleteIcon from "@mui/icons-material/Delete";
import { Link } from "react-router-dom";
import Product from "./Product";
import Slider from "@mui/material/Slider";
import Typography from "@mui/material/Typography";
import { BsBagXFill } from "react-icons/bs";

function Products(props) {
  const productCategory = props.category;

  const [authenticated, setAuthenticated] = useState(false);
  const [products, setProducts] = useState([]);
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [genderFilter, setGenderFilter] = useState("");
  const [priceRange, setPriceRange] = useState([0, 20000]);
  const [sortOption, setSortOption] = useState("");

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

    const fetchProducts = async () => {
      try {
        const response = await fetch(
          `http://localhost:3001/api/products/category/${productCategory}`,
          {
            credentials: "include",
          }
        );
        const data = await response.json();
        setProducts(data);
        setFilteredProducts(data); 
        console.log(data);
      } catch (error) {
        console.error("Eroare la preluarea produselor:", error);
      }
    };

    setGenderFilter("");
    setSortOption("");
    setPriceRange([0, 20000]);

    fetchData();
    fetchProducts();
  }, [productCategory]);

  const titleProductCategory = productCategory
    .split(" ")
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
    .join(" ");

  const filterByGender = (gender) => {
    setGenderFilter(gender);
    const filtered = products.filter((product) => product.gender === gender);
    setFilteredProducts(filtered);
  };

  const sortByPrice = (option) => {
    setSortOption(option);
    let sortedProducts = [...filteredProducts];
    if (option === "lowToHigh") {
      sortedProducts.sort((a, b) => a.price - b.price);
    } else if (option === "highToLow") {
      sortedProducts.sort((a, b) => b.price - a.price);
    }
    setFilteredProducts(sortedProducts);
  };


  const handlePriceRangeChange = (event, newValue) => {
    setPriceRange(newValue); 
    const filtered = products.filter((product) => product.price >= newValue[0] && product.price <= newValue[1]);
    setFilteredProducts(filtered);
  };
  
  const clearGenderFilter = () => {
    setGenderFilter("");
    setFilteredProducts(products); 
  };

  return (
    <div className="products-container">
      <p className="big-title">{titleProductCategory}</p>
      <div className="filters">
        <div className="filter-section">
          <h3>Gender</h3>
          <button className="filter-btn" onClick={clearGenderFilter}>
            All
          </button>
          <button
            className={genderFilter === "Men" ? "active" : ""}
            onClick={() => filterByGender("Men")}
          >
            Men
          </button>
          <button
            className={genderFilter === "Women" ? "active" : ""}
            onClick={() => filterByGender("Women")}
          >
            Women
          </button>
        </div>
        <div className="filter-section">
          <h3>Price Range</h3>
          <Slider 
            value={priceRange}
            onChange={handlePriceRangeChange}
            valueLabelDisplay="auto"
            min={0}
            max={20000}
          />
        </div>
        <div className="filter-section filter-r">
          <div className="filter-r-items">
          <h3>Sort by Price</h3>
          <select onChange={(e) => sortByPrice(e.target.value)}>
            <option value="">Select</option>
            <option value="lowToHigh">Price: Low to High</option>
            <option value="highToLow">Price: High to Low</option>
          </select>
          </div>
        </div>
      </div>
      <div className="product-container">
      {filteredProducts.length === 0 ? (
            <div className="empty-cart">
                <span role="img" aria-label="shopping cart" className="cart-emoji"><BsBagXFill /></span>
                <p className="empty-cart-message">No items found.</p>
            </div>
        ) : (
        <div className="product-grid">
          {filteredProducts.map((product) => (
            <div key={product.id}>
              <Link
                to={`/${product.category}/${product.id}`}
                style={{ textDecoration: "none" }}
              >
                <Product
                  title={product.name}
                  price={product.price}
                  img={product.image_urls[0]}
                  category={product.category}
                  id={product.id}
                  brand={product.brand}
                />
              </Link>
            </div>
          ))}
        </div>
        )}
      </div>
    </div>
  );
}

export default Products;

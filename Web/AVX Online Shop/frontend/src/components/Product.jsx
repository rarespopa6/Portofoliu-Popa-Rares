import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";

function Product(props){
    const img_url = "/imgs/" + props.category + "/" + props.img;
    const productLink = `/${props.category}/${props.id}`;
    return (
        <div className="product-card">
                <img className="product-img" src={ img_url } alt={props.title}/>
                <p className="product-title" style={{color: 'black', marginTop: '5', fontWeight: 'bold'}}>{ props.brand }</p>
                <p className="product-title" style={{color: 'black', marginTop: '0', marginBottom: '5'}}>{ props.title }</p>
                <p className="product-price" style={{color: 'black'}}>{ props.price } Lei</p>
        </div>
    );
}

export default Product;
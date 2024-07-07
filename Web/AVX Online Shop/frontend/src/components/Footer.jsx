import React from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";

function Footer() {
  const year = new Date().getFullYear();
  return (
    <footer>
      <div className="ftr-bottom">
        <div className="ftr-btn">
          <Link to="/contact">Contact</Link>
        </div>
        <div className="ftr-btn">
          <Link to="/return-policy">Return Policy</Link>
        </div>
        <div className="ftr-btn">
          <Link to="/about">About</Link>
        </div>
      </div>
      <p>Copyright ⓒ {year}</p>
    </footer>
  );
}

export default Footer;

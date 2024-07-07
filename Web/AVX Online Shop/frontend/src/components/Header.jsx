import React, { useState, useRef } from "react";
import HighlightIcon from "@mui/icons-material/Highlight";
import { FaShopware, FaShoppingCart, FaLock, FaRegUser } from "react-icons/fa";
import { FiLogOut } from "react-icons/fi";
import { IoMdHeartEmpty } from "react-icons/io";
import { HiOutlineShoppingCart } from "react-icons/hi2";
import { MdAdminPanelSettings } from "react-icons/md";
import { Link } from "react-router-dom";

function Header({ authenticated, onLogout, userId }) {
  return (
    <div id="header">
      <div id="logo">
      <h1>
        <Link to="/" className="logo-link">
        <FaShopware />
          <span id="logo-btn">AVX</span>
        </Link>
      </h1>
      </div>
      <div id="auth-buttons">
        {!authenticated ? (
          <>
            <Link to="/login">
              <button className="auth-button">Login</button>
            </Link>
            <Link to="/register">
              <button className="auth-button">Register</button>
            </Link>
          </>
        ) : (
          <div className="header-right">
            <div className="profile-dropdown">
              <button className="profile-button" style={{fontSize: '22px'}}><FaRegUser /></button>
              <div className="dropdown-content">
                <Link to="/my-orders">Orders <FaShoppingCart /></Link>
                <Link to="/security">Security <FaLock /></Link>
                {userId == 21 ? (
                <Link to="/admin">Admin <MdAdminPanelSettings style={{fontSize: '24px'}}/></Link>
                ): <></>}
                <button className="auth-button" onClick={onLogout}>Logout <FiLogOut /></button>
              </div>
            </div>
            <div className="header-link-btn">
            <Link to="/cart" style={{color: 'black', fontSize: '22px'}}><HiOutlineShoppingCart /></Link>
            </div>
            <div className="header-link-btn">
            <Link to="/favorites" style={{color: 'black', fontSize: '22px'}}><IoMdHeartEmpty /></Link>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default Header;

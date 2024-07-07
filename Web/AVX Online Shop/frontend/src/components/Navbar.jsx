import React from "react";
import DeleteIcon from "@mui/icons-material/Delete";
import { Link } from "react-router-dom";
import { Collapse } from "bootstrap";

function Navbar(props) {
  // Funcție pentru a deschide meniul când se face clic pe buton
  const handleToggle = () => {
    const element = document.getElementById("navbarNav");
    const bsCollapse = new Collapse(element);
    bsCollapse.toggle();
  };

  return (
      <nav className="navbar navbar-expand-lg navbar-light bg-light">
        <div className="nvb">
          {/* Buton pentru colapsarea meniului pe dispozitive mobile */}
          <button className="navbar-toggler" type="button"  onClick={handleToggle}>
            <span className="navbar-toggler-icon" style={{backgroundColor: 'white'}}></span>
          </button>

          <div className="collapse navbar-collapse" id="navbarNav">
            {/* Listă pentru elementele din navbar */}
            <div style={{display: 'flex', width: '100%', justifyContent: 'space-around'}}>
            <ul className="navbar-nav mr-auto">
              <li className="nav-item">
                <div>
                  <Link className="nav-link" to="/" style={{ color: 'white' }}>Home</Link>
                </div>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/t-shirts" style={{ color: 'white' }}>T-Shirts</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/jeans" style={{ color: 'white' }}>Jeans</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/pants" style={{ color: 'white' }}>Pants</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/jackets" style={{ color: 'white' }}>Jackets</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/shirts" style={{ color: 'white' }}>Shirts</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/shoes" style={{ color: 'white' }}>Shoes</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/watches" style={{ color: 'white' }}>Watches</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/sunglasses" style={{ color: 'white' }}>Sunglasses</Link>
              </li>
            </ul>
            </div>
          </div>
        </div>
      </nav>

  );
}

export default Navbar;

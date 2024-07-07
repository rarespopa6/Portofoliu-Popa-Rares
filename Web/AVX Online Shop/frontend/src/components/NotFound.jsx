import React from "react";

function NotFound(){
    return (
         <div className="empty-cart" style={{marginBottom: '100px'}}>
            <p className="err404">404</p>
            <p className="empty-cart-message">Page not found.</p>
        </div>
    );
}

export default NotFound;
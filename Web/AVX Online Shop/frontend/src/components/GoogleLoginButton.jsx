import React from "react";
import { FcGoogle } from "react-icons/fc";

function GoogleLoginButton(props) {
  return (
    <div>
      <p style={{paddingTop: '10px'}}>------------- OR -------------</p>
      <div style={{width: '100%', display: 'flex', justifyContent: 'center'}}>
      <button className="google-login-button" onClick={props.handleGoogleLogin} style={{backgroundColor: '#D3D3D3', color: 'black'}}>
        Login with <span style={{marginLeft: '5px'}}><FcGoogle style={{ fontSize: "25px", verticalAlign: "middle" }} /></span>
      </button>
      </div>
    </div>
  );
}

export default GoogleLoginButton;

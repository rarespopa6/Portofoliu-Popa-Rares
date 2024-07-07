import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { FcGoogle } from "react-icons/fc";
import GoogleLoginButton from "./GoogleLoginButton";

function Login({ onLogin }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(""); 
  const navigate = useNavigate();
  const [authenticated, setAuthenticated] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
        try {
            const userResponse = await fetch("http://localhost:3001/api/user", {
                credentials: "include",
            });
            const userData = await userResponse.json();
            console.log("userData:", userData); 
            if (userData.isAuthenticated) {
                setAuthenticated(true);
                navigate("/");
            } else {
                setAuthenticated(false);
            }
        } catch (error) {
            console.error("Eroare la verificarea utilizatorului:", error);
        }
    };
    
    fetchData();
}, [navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(""); 

    if (!email || !password) {
      setError("All fields are required.");
      return;
    }

    try {
      const response = await fetch("http://localhost:3001/api/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
        credentials: "include",
      });
      const data = await response.json();
      if (data.success) {
        onLogin();
        navigate("/");
        window.location.reload();
      } else {
        setError(data.message); 
      }
    } catch (error) {
      console.error("Eroare la autentificare:", error);
      setError("Eroare la autentificare. Verificați consola pentru detalii.");
    }
  };

  const handleGoogleLogin = () => {
    window.location.href = "http://localhost:3001/auth/google";
  };

  return (
    <div className="auth-mid">
      <div className="login">
        <h2>Login</h2>
        {error && <div className="error">{error}</div>} 
        <form onSubmit={handleSubmit}>
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <button type="submit">Login</button>
        </form>
        <div style={{width: '100%', display: 'flex', justifyContent: 'center'}}>
          <GoogleLoginButton handleGoogleLogin={handleGoogleLogin} />
        </div>
      </div>
    </div>
  );
}

export default Login;

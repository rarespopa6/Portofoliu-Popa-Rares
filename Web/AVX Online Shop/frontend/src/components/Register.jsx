import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

function Register() {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
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
}, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(""); 

    if (!firstName || !lastName || !email || !password) {
      setError("All fields are required.");
      return;
    }

    if (password.length < 8){
      setError("Password must be at least 8 characters long.");
      return;
    }

    try {
      const response = await fetch("http://localhost:3001/api/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ firstName, lastName, email, password }),
        credentials: "include",
      });

      const data = await response.json();
      if (data.success) {
        navigate("/");
        window.location.reload();
      } else {
        setError(data.error); 
      }
    } catch (error) {
      console.error("Eroare la înregistrare:", error);
      setError("Eroare la înregistrare. Verificați consola pentru detalii.");
    }
  };

  return (
    <div className="auth-mid">
      <div className="login">
        <h2>Register</h2>
        {error && <div className="error">{error}</div>} 
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            placeholder="First Name"
            value={firstName}
            onChange={(e) => setFirstName(e.target.value)}
          />
          <input
            type="text"
            placeholder="Last Name"
            value={lastName}
            onChange={(e) => setLastName(e.target.value)}
          />
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
          <button type="submit">Register</button>
        </form>
      </div>
    </div>
  );
}

export default Register;

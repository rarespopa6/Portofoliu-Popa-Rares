import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

function Security() {
  const [authenticated, setAuthenticated] = useState(false);
  const [userDetails, setUserDetails] = useState({});
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const userResponse = await fetch("http://localhost:3001/api/user", {
          credentials: "include",
        });
        const userData = await userResponse.json();
        setAuthenticated(userData.isAuthenticated);
        if (!userData.isAuthenticated) {
          navigate("/login");
        } else {
          setUserDetails(userData.user);
        }
      } catch (error) {
        console.error("Eroare la verificarea utilizatorului:", error);
      }
    };

    fetchData();
  }, [navigate]);

  const handleChangePassword = async (e) => {
    e.preventDefault();
    try {
        if (newPassword.length < 8){
            setError("New password must be atleast 8 characters long.");
            return;
        }

      const response = await fetch(`http://localhost:3001/api/user/change-password`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          oldPassword,
          newPassword,
        }),
        credentials: "include",
      });

      const data = await response.json();
      if (response.ok) {
        console.log("Parola a fost schimbată cu succes!");
        setError("");
        setSuccessMessage(data.message);
        setError(""); 
        setOldPassword(""); 
        setNewPassword("");
      } else {
        setError("Incorrect Password.");
        setSuccessMessage("");
      }
    } catch (error) {
      console.error("Eroare la schimbarea parolei:", error);
      setError("Eroare la server");
    }
  };

  if (!authenticated) {
    return null;
  }

  return (
    <div className="security-container">
      <p className="big-title" style={{fontSize: '26px'}}>Security Settings</p>
      <h2>Profile Information</h2>
      <div>
        <p><span className="security-bold">Profile ID:</span> {userDetails.id} {userDetails.id === 21 ? "(Admin)" : ""}</p>
        <p><span className="security-bold">First Name:</span> {userDetails.firstname}</p>
        <p><span className="security-bold">Last Name:</span> {userDetails.lastname}</p>
        <p><span className="security-bold">Email: </span> {userDetails.email}</p>
      </div>
      {userDetails.password != 'google' ? (
      <form onSubmit={handleChangePassword}>
        <h2 style={{marginTop: '15px'}}>Change Password</h2>
        
        <div>
          <label htmlFor="oldPassword" className="security-bold">Current Password:</label>
          <input style={{marginLeft: '3px'}}
            type="password"
            id="oldPassword"
            value={oldPassword}
            placeholder="Enter your current password..."
            onChange={(e) => setOldPassword(e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor="newPassword" className="security-bold">New Password:</label>
          <input style={{marginLeft: '25px'}}
            type="password"
            id="newPassword"
            placeholder="Enter the new password..."
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            required
          />
        </div>
        {successMessage && <p style={{ color: "green" }}>{successMessage}</p>}
        {error && <p className="error-message">{error}</p>}
        <button type="submit">Change Password</button>
      </form>
      ) : <h2 style={{marginTop: '15px', marginBottom: '95px'}}>You are loged in with Google. You don't have to worry about passwords.</h2>}
    </div>
  );
}

export default Security;

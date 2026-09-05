import React, { useState } from "react";
import Login from "./Login";
import Signup from "./Signup"; // Assuming you have a Signup component
 

export default function Authentic() {
  const [showLogin, setShowLogin] = useState(true);

  const handleLoginClick = () => {
    console.log("login");
    setShowLogin(true);
  };

  const handleSignupClick = () => {
    console.log("sign up");
    setShowLogin(false);
  };

  return (
    <div className="dhoni">
      <div className="toggle-container">
        <input type="radio" name="slide" id="login" />
        <label
          htmlFor="login"
          onClick={handleLoginClick}
          className={`toggle-button ${showLogin ? "active" : ""}`}
        >
          Login
        </label>
        <input type="radio" name="slide" id="signup" />
        <label
          htmlFor="signup"
          onClick={handleSignupClick}
          className={`toggle-button ${!showLogin ? "active" : ""}`}
        >
          Signup
        </label>
      </div>
      <div>
        {showLogin ? <Login /> : <Signup />}
      </div>
    </div>
  );
}

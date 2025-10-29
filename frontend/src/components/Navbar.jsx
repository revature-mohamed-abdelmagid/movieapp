import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { FaFilm, FaUser, FaSignOutAlt } from "react-icons/fa";
import { useAuth } from '../context/AuthContext';
import '../styles/Navbar.css';

const Navbar = () => {
  const { user, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const handleSignOut = async () => {
    await logout();
    navigate('/');
  };

  return (
    <nav className="navbar">
      <Link to="/" className="navbar-logo">
        <FaFilm className="logo-icon" />
        <span>CineVerse</span>
      </Link>
      
      {/* <input type="text" placeholder="Search movies..." className="search-input" /> */}
      
      <div className="nav-links">
        <a href="#browse">Browse Movies</a>
        
        {isAuthenticated ? (
          <div className="user-menu">
            <div className="user-info">
              <FaUser className="user-icon" />
              <span className="username">{user?.username}</span>
            </div>
            <button className="sign-out" onClick={handleSignOut}>
              <FaSignOutAlt />
              Sign Out
            </button>
          </div>
        ) : (
          <Link to="/signin">
            <button className="sign-in">Sign In</button>
          </Link>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
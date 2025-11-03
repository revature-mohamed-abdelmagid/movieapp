import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { FaFilm, FaUserCog } from "react-icons/fa";
import { useAuth } from "../context/AuthContext";
import '../styles/Navbar.css';

const Navbar = () => {
  const { user, isAdmin, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogin = () => {
    navigate('/login');
  };

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="navbar">
      <Link to="/" className="navbar-logo">
        <FaFilm className="logo-icon" />
        <span>CineVerse</span>
      </Link>
      <div className="nav-links">
        <Link to="/" className="nav-link">Browse Movies</Link>
        {isAdmin() && (
          <Link to="/admin" className="nav-link admin-link">
            <FaUserCog className="admin-icon" />
            Admin Panel
          </Link>
        )}
        {user ? (
          <div className="user-menu">
            <span className="username">{user.username}</span>
            <button className="logout-btn" onClick={handleLogout}>
              Logout
            </button>
          </div>
        ) : (
          <button className="sign-in" onClick={handleLogin}>
            Sign In
          </button>
        )}
      </div>
    </nav>
  );
}

export default Navbar;
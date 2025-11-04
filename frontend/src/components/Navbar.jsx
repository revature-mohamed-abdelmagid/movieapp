import React, { useState, useRef, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FaFilm, FaUser, FaSignOutAlt, FaPlusCircle, FaAngleDown, FaSearch } from "react-icons/fa";
import { toast } from "react-toastify";
import { useAuth } from "../context/AuthContext";
import "../styles/Navbar.css";

const Navbar = () => {
  const { user, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const [showDropdown, setShowDropdown] = useState(false);
  const dropdownRef = useRef(null);
  const [wasAuthenticated, setWasAuthenticated] = useState(isAuthenticated);

  const toggleDropdown = () => setShowDropdown(!showDropdown);

  const handleSignOut = async () => {
    await logout();
    toast.success("👋 You’ve been signed out successfully", {
      position: "top-center",
      autoClose: 3000,
      style: {
        backgroundColor: "#1a1a1a", // deep dark background
        color: "white",           // CineVerse gold
        fontWeight: "500",
        border: "1px solid #ffd700",
      },
      icon: "🎬",
    });
    navigate("/");
    setShowDropdown(false);
  };

  // Detect when user signs in
  useEffect(() => {
    if (!wasAuthenticated && isAuthenticated) {
      toast.success(`🍿 Welcome back, ${user?.username || "Guest"}!`, {
        position: "top-center",
        autoClose: 3000,
        style: {
          backgroundColor: "#1a1a1a",
          color: "white",
          fontWeight: "500",
          border: "1px solid #ffd700",
        },
        icon: "⭐",
      });
    }
    setWasAuthenticated(isAuthenticated);
  }, [isAuthenticated, user, wasAuthenticated]);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setShowDropdown(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const isAdmin = user?.roles?.includes("ROLE_ADMIN");

  return (
    <nav className="navbar">
      <Link to="/" className="navbar-logo">
        <FaFilm className="logo-icon" />
        <span>CineVerse</span>
      </Link>

      <div className="nav-links">
        {/* <Link to="/movies" className="nav-link flex flex-row items-center justify-center gap-2"> */}
          {/* <FaSearch /> Browse Movies */}
        {/* </Link> */}

        {isAuthenticated ? (
          <div className="user-menu" ref={dropdownRef} style={{ position: "relative" }}>
            {isAdmin && (
              <Link to="/add-movie" className="add-movie-link">
                <button className="btn-add-movie">
                  <FaPlusCircle /> Add Movie
                </button>
              </Link>
            )}
            <div
              className="user-info"
              onClick={toggleDropdown}
              style={{ cursor: "pointer", display: "flex", alignItems: "center" }}
            >
              <FaUser className="user-icon" />
              <span className="username">{user?.username}</span>
              <FaAngleDown style={{ marginLeft: "8px" }} />
            </div>

            {showDropdown && (
              <div
                className="dropdown-menu mt-2 rounded bg-transparent"
                style={{
                  position: "absolute",
                  top: "100%",
                  right: 0,
                  border: "1px solid #929292ff",
                  padding: "10px",
                  zIndex: 1000,
                  minWidth: "150px",
                }}
              >
                <button
                  className="flex flex-row bg-black justify-center text-white border border-white hover:border-black items-center hover:bg-yellow-300 hover:text-black px-3 py-2 rounded w-full text-center"
                  onClick={handleSignOut}
                  style={{
                    display: "flex",
                    alignItems: "center",
                    
                    cursor: "pointer",
                    width: "100%",
                    justifyContent: "center",
                  }}
                >
                  <FaSignOutAlt style={{ marginRight: "8px" }} />
                  Sign Out
                </button>
              </div>
            )}
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

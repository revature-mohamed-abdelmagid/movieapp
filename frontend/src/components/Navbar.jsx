import React from "react";
import { FaFilm } from "react-icons/fa"; // how to install react-icons? -- npm install react-icons
import '../styles/Navbar.css';

const Navbar = () => {
  return (
    <nav className="navbar">
      <div className="navbar-logo">
        <FaFilm className="logo-icon" />
        <span>CineVerse</span>
        </div>
        {/* <input type="text" placeholder="Search movies..." className="search-input" /> */}
        <div className="nav-links">
            <a href="#browse">Browse Movies</a>
            <button className="sign-in">Sign In</button>
        </div>
        </nav>
    );
    }

export default Navbar;
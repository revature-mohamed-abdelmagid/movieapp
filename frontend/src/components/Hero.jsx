import React from "react";
import {FaFilm} from "react-icons/fa";
import "../styles/Hero.css";

const Hero = () => {
    return (
        <section className="hero">
            <FaFilm className="hero-icon" />
            <h1>Welcome to <span className="highlight"> CineVerse</span></h1>
            <p>Discover, review, and curate your favorite movies all in one place</p>
            <button className="explore-btn">Explore Movies →</button>
        </section>
    );
}

export default Hero;
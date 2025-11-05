import React from "react";
import { motion } from "framer-motion";
import { FaFilm } from "react-icons/fa";
import "../styles/Hero.css";

const Hero = () => {
  const handleExploreClick = () => {
    window.location.href = "/movies";
  };

  return (
    <section className="hero">
      <motion.div
        initial={{ scale: 0, rotate: -180, opacity: 0 }}
        animate={{ scale: 1, rotate: 0, opacity: 1 }}
        transition={{ type: "spring", stiffness: 120, damping: 12 }}
      >
        <FaFilm className="hero-icon" />
      </motion.div>

      <motion.h1
        initial={{ opacity: 0, y: -30 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.3, duration: 0.8 }}
      >
        Welcome to <span className="highlight"><span className="text-white" >Cine</span><span className="text-yellow-300">Verse</span></span>
      </motion.h1>

      <motion.p
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.6, duration: 0.8 }}
      >
        Discover, review, and watch the Trailers all in one place
      </motion.p>

      <motion.button
        onClick={handleExploreClick}
        className="explore-btn"
        initial={{ opacity: 0, scale: 0.9 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{
          delay: 1,
          duration: 0.6,
          type: "spring",
          stiffness: 150,
        }}
        whileHover={{
          scale: 1.1,
          backgroundColor: "#ff1744",
          boxShadow: "0 0 15px rgba(255, 23, 68, 0.4)",
        }}
      >
        Explore Movies →
      </motion.button>
    </section>
  );
};

export default Hero;

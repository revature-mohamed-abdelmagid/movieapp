import React from "react";
import { FaFilm, FaGithub, FaLinkedin, FaEnvelope } from "react-icons/fa";

const Footer = () => {
  return (
    <footer className="bg-[#121212] text-gray-300 border-t border-gray-800 py-8 mt-12">
      <div className="max-w-[1460px] mx-auto px-4 md:px-8 flex flex-col md:flex-row justify-center items-center gap-6">
        
        {/* Logo and Name */}
        <div className="flex items-center gap-2">
          <FaFilm className="text-yellow-400 text-2xl" />
          <h2 className="text-xl font-semibold text-white"><span className="text-white">Cine</span><span className="text-yellow-300">Verse</span></h2>
        </div>

        {/* Navigation Links */}
        <div className="flex flex-wrap justify-center gap-6 text-sm">
          <a href="/" className="hover:text-yellow-400 transition">Home</a>
          <a href="/movies" className="hover:text-yellow-400 transition">Movies</a>
          
          
        </div>


      </div>

      {/* Divider */}
      <div className="border-t border-gray-800 mt-6"></div>

      {/* Copyright */}
      <div className="text-center text-gray-500 text-sm mt-4">
        © {new Date().getFullYear()}<span> <span className="text-white">Cine</span><span className="text-yellow-300">Verse</span></span>.
        All rights reserved.
      </div>
    </footer>
  );
};

export default Footer;

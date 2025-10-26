// src/App.jsx
import React from 'react';
import MovieList from './components/MovieList';
import MovieCard from './components/MovieCard';
import FeaturedMovies from './components/FeaturedMovies';
import Navbar from './components/Navbar';
import './styles/App.css';
import Hero from './components/Hero';

function App() {
  return (
    <div className="App">
      <div className="hero-wrapper">
      <div className="container">
      <Navbar />
        <Hero />
        </div>
        </div>
        <div className="container">
        <FeaturedMovies />
        </div>
    </div>
    
  );
}

export default App;
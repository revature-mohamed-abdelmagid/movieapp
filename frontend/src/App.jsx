// src/App.jsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import MovieList from './components/MovieList';
import MovieCard from './components/MovieCard';
import FeaturedMovies from './components/FeaturedMovies';
import Navbar from './components/Navbar';
import Hero from './components/Hero';
import MovieDetails from './pages/MovieDetails';
import './styles/App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          {/* Home Page Route */}
          <Route path="/" element={
            <>
              <div className="hero-wrapper">
                <div className="container">
                  <Navbar />
                  <Hero />
                </div>
              </div>
              <div className="container">
                <FeaturedMovies />
              </div>
            </>
          } />
          
          {/* Movie Details Route */}
          <Route path="/movie/:id" element={<MovieDetails />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
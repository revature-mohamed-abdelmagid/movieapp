// src/App.jsx
import React from 'react';
import MovieList from './components/MovieList';
import Navbar from './components/Navbar';
import './styles/App.css';
import Hero from './components/Hero';

function App() {
  return (
    <div className="App">
      <Navbar />
        <Hero />
    </div>
  );
}

export default App;
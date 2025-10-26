// src/App.jsx
import React from 'react';
import MovieList from './components/MovieList';
import './styles/App.css';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <h1>Movie Review App</h1>
        <p>Discover, rate, and review your favorite movies</p>
      </header>
      <main>
        <MovieList />
      </main>
    </div>
  );
}

export default App;
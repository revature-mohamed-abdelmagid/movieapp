// src/components/MovieCard.jsx
import React from 'react';

const MovieCard = ({ movie }) => {
  return (
    <div className="movie-card">
      <h3>{movie.title}</h3>
      <p className="movie-description">{movie.description}</p>
      <div className="movie-details">
        <span>Release Date: {new Date(movie.releaseDate).toLocaleDateString()}</span>
        <span>Rating: {movie.rating}/10</span>
      </div>
      <div className="cast">
        <strong>Cast:</strong> {movie.cast?.join(', ')}
      </div>
      {movie.trailerUrl && (
        <a 
          href={movie.trailerUrl} 
          target="_blank" 
          rel="noopener noreferrer"
          className="trailer-link"
        >
          Watch Trailer
        </a>
      )}
    </div>
  );
};

export default MovieCard;
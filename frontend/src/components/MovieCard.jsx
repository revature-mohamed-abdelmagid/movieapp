import React from 'react';
import { FaStar, FaCalendar } from 'react-icons/fa';

const MovieCard = ({ movie, onClick }) => {
    const getDisplayPoster = (movie) => {
        return movie.posterUrl || `https://via.placeholder.com/200x300/4a5568/ffffff?text=${encodeURIComponent(movie.title)}`;
    };

    const handleImageError = (e, movie) => {
        if (e.target.dataset.fallbackAttempted === 'true') {
            e.target.removeAttribute('src');
            return;
        }
        
        e.target.dataset.fallbackAttempted = 'true';
        e.target.src = `https://via.placeholder.com/200x300/4a5568/ffffff?text=${encodeURIComponent(movie.title)}`;
    };
    
    
    return (
        <div 
            className='movie-card clickable' 
            onClick={onClick}
        >
            <div className='movie-poster'>
                <img 
                    src={getDisplayPoster(movie)} 
                    alt={movie.title}
                    onError={(e) => handleImageError(e, movie)}
                />
            </div>
            <div className='movie-info'>
                <h4 className='movie-title'>{movie.title}</h4>
                <div className='movie-details'>
                    <span className='movie-year'>
                        <FaCalendar className='icon' />
                        {movie.releaseYear}
                    </span>
                    {movie.avgRating && (
                        <div className='movie-rating'>
                            <FaStar className='star-icon' />
                            <span>{movie.avgRating.toFixed(1)}</span>
                        </div>
                    )}
                </div>
                
                {movie.description && (
                    <p className='movie-description'>
                        {movie.description.length > 100 
                            ? `${movie.description.substring(0, 100)}...` 
                            : movie.description
                        }
                    </p>
                )}
                
                {movie.genres && movie.genres.length > 0 && (
                    <div className='movie-genres'>
                        {movie.genres.map((genre, index) => (
                            <span key={index} className='genre-tag'>
                                {genre}
                            </span>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};


export default MovieCard;
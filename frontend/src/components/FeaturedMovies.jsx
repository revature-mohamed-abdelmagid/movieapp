import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaStar, FaCalendar, FaClock, FaExclamationTriangle } from 'react-icons/fa';
import { movieAPI } from '../../services/api.js';
import '../styles/FeaturedMovies.css';

const FeaturedMovies = () => {
    const navigate = useNavigate();
    const [movies, setMovies] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchMovies();
    }, []);

    const fetchMovies = async () => {
        try {
            setLoading(true);
            setError(null);
            console.log('Fetching movies with genres from API...');
            
            const response = await movieAPI.getAllMoviesWithGenres();
            console.log('Movies with genres fetched successfully:', response.data);
            
            // Limit to first 6 movies for featured section
            setMovies(response.data.slice(0, 6));
        } catch (err) {
            console.error('Error fetching movies:', err);
            setError('Failed to load movies. Please try again later.');
        } finally {
            setLoading(false);
        }
    };

    const getDisplayPoster = (movie) => {
        // Use the movie's poster URL if available, otherwise use a placeholder
        return movie.posterUrl || `https://via.placeholder.com/200x300/4a5568/ffffff?text=${encodeURIComponent(movie.title)}`;
    };

    const formatDuration = (minutes) => {
        if (!minutes) return '';
        const hours = Math.floor(minutes / 60);
        const mins = minutes % 60;
        return hours > 0 ? `${hours}h ${mins}m` : `${mins}m`;
    };

    const handleMovieClick = (movieId) => {
        navigate(`/movie/${movieId}`);
    };

    const renderMovieCard = (movie) => (
        <div 
            key={movie.movieId} 
            className='movie-card clickable' 
            onClick={() => handleMovieClick(movie.movieId)}
        >
            <div className='movie-poster'>
                <img 
                    src={getDisplayPoster(movie)} 
                    alt={movie.title}
                    onError={(e) => {
                        e.target.src = `https://via.placeholder.com/200x300/4a5568/ffffff?text=${encodeURIComponent(movie.title)}`;
                    }}
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
                
                {console.log(movie)}
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

    if (loading) {
        return (
            <section className='featured'>
                <div className='featured-header'>
                    <h2>Featured Movies</h2>
                </div>
                <div className='loading-container'>
                    <div className='loading-spinner'></div>
                    <p>Loading movies...</p>
                </div>
            </section>
        );
    }

    if (error) {
        return (
            <section className='featured'>
                <div className='featured-header'>
                    <h2>Featured Movies</h2>
                </div>
                <div className='error-container'>
                    <FaExclamationTriangle className='error-icon' />
                    <p className='error-message'>{error}</p>
                    <button onClick={fetchMovies} className='retry-button'>
                        Try Again
                    </button>
                </div>
            </section>
        );
    }

    return (
        <section className='featured'>
            <div className='featured-header'>
                <h2>Featured Movies</h2>
                <a href="#view-all">View All ({movies.length})</a>
            </div>
            
            {movies.length === 0 ? (
                <div className='no-movies'>
                    {console.log(movies)}
                    <p>No movies available at the moment.</p>
                    <p>Check back later for new releases!</p>
                </div>
            ) : (
                <div className='movie-grid'>
                    {movies.map(renderMovieCard)}
                </div>
            )}
        </section>
    );
};

export default FeaturedMovies;
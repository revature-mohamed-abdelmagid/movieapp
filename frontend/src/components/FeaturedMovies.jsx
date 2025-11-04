import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { FaExclamationTriangle } from 'react-icons/fa';
import { movieAPI } from '../../services/api.js';
import MovieCard from './MovieCard';
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
            const response = await movieAPI.getAllMoviesWithGenres();
            setMovies(response.data.slice(0, 6));
        } catch (err) {
            console.error('Error fetching movies:', err);
            setError('Failed to load movies. Please try again later.');
        } finally {
            setLoading(false);
        }
    };

    const handleMovieClick = (movieId) => {
        navigate(`/movie/${movieId}`);
    };

    const handleAllMovieClick = () => {
        navigate('/movies');
    };

    if (loading) {
        return (
            <section className="featured">
                <div className="featured-header">
                    <h2>Featured Movies</h2>
                </div>
                <div className="loading-container">
                    <div className="loading-spinner"></div>
                    <p>Loading movies...</p>
                </div>
            </section>
        );
    }

    if (error) {
        return (
            <section className="featured">
                <div className="featured-header">
                    <h2>Featured Movies</h2>
                </div>
                <div className="error-container">
                    <FaExclamationTriangle className="error-icon" />
                    <p className="error-message">{error}</p>
                    <button onClick={fetchMovies} className="retry-button">
                        Try Again
                    </button>
                </div>
            </section>
        );
    }

    return (
        <section className="featured">
            <div className="featured-header">
                <h2>Featured Movies</h2>
                <a onClick={handleAllMovieClick} className="cursor-pointer">View All ({movies.length})</a>
            </div>

            {movies.length === 0 ? (
                <div className="no-movies">
                    <p>No movies available at the moment.</p>
                    <p>Check back later for new releases!</p>
                </div>
            ) : (
                <div className="movie-grid ">
                    {movies.map((movie, index) => (
                        <motion.div
                            key={movie.movieId}
                            initial={{ opacity: 0, y: 50 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{
                                delay: index * 0.1,
                                duration: 0.5,
                                ease: "easeOut"
                            }}
                        >
                            <MovieCard 
                                movie={movie} 
                                onClick={() => handleMovieClick(movie.movieId)} 
                                
                            />
                        </motion.div>
                    ))}
                </div>
            )}
        </section>
    );
};

export default FeaturedMovies;

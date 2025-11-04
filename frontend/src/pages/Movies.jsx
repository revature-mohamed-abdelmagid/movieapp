import React, { useState, useEffect } from 'react';
import { FaSearch as Search } from 'react-icons/fa';
import Navbar  from '../components/Navbar';
import { useNavigate } from 'react-router-dom';
import MovieCard from '../components/MovieCard';
import Input from '../components/ui/Input.jsx';
import { movieAPI } from '../../services/api';
import { FaExclamationTriangle } from 'react-icons/fa';

const Movies = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [movies, setMovies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleMovieClick = (movieId) => {
      navigate(`/movie/${movieId}`);
  };

    
  useEffect(() => {
    const fetchMovies = async () => {
      try {
        setLoading(true);
        setError(null);
        const response = await movieAPI.getAllMoviesWithGenres();
        setMovies(response.data);
      } catch (err) {
        console.error('Error fetching movies:', err);
        setError('Failed to load movies. Please try again later.');
      } finally {
        setLoading(false);
      }
    };
    fetchMovies();
  }, []);

  const filteredMovies = movies.filter(movie =>
    movie.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
    (movie.genres?.some(g => g.toLowerCase().includes(searchQuery.toLowerCase())) || false) ||
    (movie.director?.some(d => d.name.toLowerCase().includes(searchQuery.toLowerCase())) || false)
  );

  
  if (loading) {
    return (
      <div className="min-h-screen bg-background py-16">
        
        <main className="container mx-auto px-4 py-8">
          <div className="loading-container">
            <div className="loading-spinner"></div>
            <p>Loading movies...</p>
          </div>
        </main>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-background py-16">
        
        <main className="container mx-auto px-4 py-8">
          <div className="error-container">
            <FaExclamationTriangle className="error-icon" />
            <p className="error-message">{error}</p>
          </div>
        </main>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background py-16">
      
      
      <main className="container mx-auto px-4 py-8">
        <div className="mb-8">
          <h1 className="text-4xl font-bold mb-6 text-left">Browse Movies</h1>
          <div className="relative max-w-md">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
            <Input
              type="text"
              placeholder="Search movies, genres, directors..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="pl-10 "
            />
          </div>
        </div>

        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
          {filteredMovies.map((movie) => (
            <MovieCard key={movie.movieId} movie={movie} onClick={() => handleMovieClick(movie.movieId)}/>
          ))}
        </div>

        {filteredMovies.length === 0 && (
          <div className="text-center py-20">
            <p className="text-xl text-muted-foreground">No movies found matching your search.</p>
          </div>
        )}
      </main>
    </div>
  );
};

export default Movies;
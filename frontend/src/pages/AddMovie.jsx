// src/pages/AddMovie.jsx
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { movieAPI, movieCastAPI, genreAPI } from '../../services/api';
import { useAuth } from '../context/AuthContext';
import CastCrewManager from '../components/CastCrewManager';
import '../styles/AddMovie.css';

const AddMovie = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const [formData, setFormData] = useState({
    title: '',
    releaseYear: '',
    duration: '',
    description: '',
    language: '',
    country: '',
    posterUrl: '',
    trailerUrl: ''
  });

  // Cast and crew state
  const [castCrew, setCastCrew] = useState([]);
  
  // Genres state
  const [availableGenres, setAvailableGenres] = useState([]);
  const [selectedGenres, setSelectedGenres] = useState([]);

  // Check if user is admin
  const isAdmin = user?.roles?.includes('ROLE_ADMIN');

  // Redirect if not admin
  React.useEffect(() => {
    if (!isAdmin) {
      navigate('/');
    }
  }, [isAdmin, navigate]);

  // Fetch available genres
  useEffect(() => {
    const fetchGenres = async () => {
      try {
        const response = await genreAPI.getAllGenres();
        setAvailableGenres(response.data);
      } catch (err) {
        console.error('Failed to fetch genres:', err);
      }
    };
    
    if (isAdmin) {
      fetchGenres();
    }
  }, [isAdmin]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleGenreToggle = (genreId) => {
    setSelectedGenres(prev => {
      if (prev.includes(genreId)) {
        return prev.filter(id => id !== genreId);
      } else {
        return [...prev, genreId];
      }
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      // Convert string values to numbers where needed
      const movieData = {
        title: formData.title.trim(),
        releaseYear: parseInt(formData.releaseYear),
        duration: formData.duration ? parseInt(formData.duration) : null,
        description: formData.description.trim() || null,
        language: formData.language.trim() || null,
        country: formData.country.trim() || null,
        posterUrl: formData.posterUrl.trim() || null,
        trailerUrl: formData.trailerUrl.trim() || null,
        avgRating: 0.0 // Default rating for new movies
      };

      // Step 1: Create the movie
      const movieResponse = await movieAPI.createMovie(movieData);
      const createdMovieId = movieResponse.data.movieId;

      // Step 2: Add genres if any selected
      if (selectedGenres.length > 0) {
        await movieAPI.addGenresToMovie(createdMovieId, selectedGenres);
      }

      // Step 3: Add cast and crew if any
      if (castCrew.length > 0) {
        const castCrewData = castCrew.map(member => ({
          personId: member.personId,
          roleId: member.roleId,
          characterName: member.characterName
        }));

        await movieCastAPI.addMultipleCastToMovie(createdMovieId, castCrewData);
      }

      setSuccess(true);
      
      // Reset form
      setFormData({
        title: '',
        releaseYear: '',
        duration: '',
        description: '',
        language: '',
        country: '',
        posterUrl: '',
        trailerUrl: ''
      });
      setCastCrew([]);
      setSelectedGenres([]);

      // Redirect to home after 2 seconds
      setTimeout(() => {
        navigate('/');
      }, 2000);

    } catch (err) {
      console.error('Failed to add movie:', err);
      const errorMsg = err.response?.data?.message || 
                       err.response?.data?.title ||
                       'Failed to add movie. Please check all fields and try again.';
      setError(errorMsg);
    } finally {
      setLoading(false);
    }
  };

  if (!isAdmin) {
    return null; // Will redirect
  }

  return (
    <div className="add-movie-page">
      <div className="add-movie-container">
        <div className="add-movie-header">
          <h1>Add New Movie</h1>
          <p>Fill in the details below to add a new movie to the database</p>
        </div>

        {error && (
          <div className="alert alert-error">
            <span className="alert-icon">⚠️</span>
            {error}
          </div>
        )}

        {success && (
          <div className="alert alert-success">
            <span className="alert-icon">✓</span>
            Movie added successfully! Redirecting...
          </div>
        )}

        <form onSubmit={handleSubmit} className="add-movie-form">
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="title">
                Title <span className="required">*</span>
              </label>
              <input
                type="text"
                id="title"
                name="title"
                value={formData.title}
                onChange={handleChange}
                placeholder="Enter movie title"
                required
                maxLength={50}
                disabled={loading}
              />
              <small>Maximum 50 characters</small>
            </div>

            <div className="form-group">
              <label htmlFor="releaseYear">
                Release Year <span className="required">*</span>
              </label>
              <input
                type="number"
                id="releaseYear"
                name="releaseYear"
                value={formData.releaseYear}
                onChange={handleChange}
                placeholder="e.g., 2024"
                required
                min="1888"
                max={new Date().getFullYear() + 5}
                disabled={loading}
              />
              <small>Must be 1888 or later</small>
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="duration">
                Duration (minutes)
              </label>
              <input
                type="number"
                id="duration"
                name="duration"
                value={formData.duration}
                onChange={handleChange}
                placeholder="e.g., 120"
                min="1"
                max="600"
                disabled={loading}
              />
              <small>Between 1 and 600 minutes</small>
            </div>

            <div className="form-group">
              <label htmlFor="language">
                Language
              </label>
              <input
                type="text"
                id="language"
                name="language"
                value={formData.language}
                onChange={handleChange}
                placeholder="e.g., English"
                maxLength={50}
                disabled={loading}
              />
              <small>Maximum 50 characters</small>
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="country">
              Country
            </label>
            <input
              type="text"
              id="country"
              name="country"
              value={formData.country}
              onChange={handleChange}
              placeholder="e.g., United States"
              maxLength={100}
              disabled={loading}
            />
            <small>Maximum 100 characters</small>
          </div>

          <div className="form-group">
            <label htmlFor="posterUrl">
              Poster URL
            </label>
            <input
              type="url"
              id="posterUrl"
              name="posterUrl"
              value={formData.posterUrl}
              onChange={handleChange}
              placeholder="https://example.com/poster.jpg"
              disabled={loading}
            />
            <small>Full URL to the movie poster image</small>
          </div>

          <div className="form-group">
            <label htmlFor="trailerUrl">
              Trailer URL
            </label>
            <input
              type="url"
              id="trailerUrl"
              name="trailerUrl"
              value={formData.trailerUrl}
              onChange={handleChange}
              placeholder="https://www.youtube.com/watch?v=..."
              disabled={loading}
            />
            <small>YouTube or other video URL for the movie trailer</small>
          </div>

          <div className="form-group">
            <label htmlFor="description">
              Description
            </label>
            <textarea
              id="description"
              name="description"
              value={formData.description}
              onChange={handleChange}
              placeholder="Enter a brief description of the movie"
              rows="5"
              maxLength={1000}
              disabled={loading}
            />
            <small>Maximum 1000 characters</small>
          </div>

          {/* Genres Section */}
          <div className="form-group">
            <label>Genres</label>
            <div className="genres-grid">
              {availableGenres.map(genre => (
                <label key={genre.genreId} className="genre-checkbox">
                  <input
                    type="checkbox"
                    checked={selectedGenres.includes(genre.genreId)}
                    onChange={() => handleGenreToggle(genre.genreId)}
                    disabled={loading}
                  />
                  <span>{genre.genreName}</span>
                </label>
              ))}
            </div>
            <small>Select one or more genres that apply to this movie</small>
          </div>

          {/* Cast & Crew Section */}
          <CastCrewManager castCrew={castCrew} setCastCrew={setCastCrew} />

          <div className="form-actions">
            <button
              type="button"
              className="btn-cancel"
              onClick={() => navigate('/')}
              disabled={loading}
            >
              Cancel
            </button>
            <button
              type="submit"
              className="btn-submit"
              disabled={loading}
            >
              {loading ? 'Adding Movie...' : 'Add Movie'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddMovie;


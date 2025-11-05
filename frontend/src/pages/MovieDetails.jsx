// src/pages/MovieDetails.jsx
import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import MovieInfo from '../components/MovieInfo';
import ReviewForm from '../components/ReviewForm';
import ReviewsList from '../components/ReviewsList';
import { movieAPI } from '../../services/api';
import '../styles/MovieDetails.css';

const MovieDetails = () => {
  const { id } = useParams();               // <-- movieId from URL
  const navigate = useNavigate();

  const [movie, setMovie] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // -------------------------------------------------
  //  FETCH MOVIE BY ID
  // -------------------------------------------------
  const fetchMovie = async () => {
    try {
      setLoading(true);
      setError(null);

      const response = await movieAPI.getMovieFullDetails(id); // <-- your API call
      setMovie(response.data);
      console.log(response.data);         // <-- expect { movieId, title, ... }
    } catch (err) {
      console.error('Failed to load movie', err);
      setError('Could not load movie details. Please try again later.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (id) fetchMovie();
  }, [id]);

  // Function to refresh movie data after a review is submitted
  const handleReviewSubmitted = () => {
    console.log('Review submitted, refreshing movie data...');
    fetchMovie();
  };

  // -------------------------------------------------
  //  RENDER STATES
  // -------------------------------------------------
  if (loading) {
    return (
      <div className="movie-details-page">
        <main className="loading">
          <div className="spinner"></div>
          <p>Loading movie details…</p>
        </main>
      </div>
    );
  }

  if (error || !movie) {
    return (
      <div className="movie-details-page">
        <main className="error">
          <p>{error || 'Movie not found.'}</p>
          <button onClick={() => navigate(-1)} className="btn-back">
            ← Go Back
          </button>
        </main>
      </div>
    );
  }

  // -------------------------------------------------
  //  SUCCESS – render the page
  // -------------------------------------------------
  return (
    <div className="movie-details-page">
      <main>
        {/* Pass the *real* movie object to your reusable component */}
        <MovieInfo movie={movie} />

        <section className="reviews">
          <h2>Reviews</h2>
          <ReviewForm 
            movieId={movie.movieId} 
            onReviewSubmitted={handleReviewSubmitted}
          />
          <ReviewsList 
            movie={movie} 
            onReviewUpdated={handleReviewSubmitted}
          />
        </section>
      </main>
    </div>
  );
};

export default MovieDetails;
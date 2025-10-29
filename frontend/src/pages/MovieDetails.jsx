import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom'; // If using Router; for Next.js use next/router
import Navbar from '../components/Navbar';
import MovieInfo from '../components/MovieInfo';
import '../styles/MovieDetails.css';

// import axios from 'axios'; // If fetching data

const MovieDetails = () => {
//   const { id } = useParams(); // Get movie ID from URL
//   const [movie, setMovie] = useState(null);
//   const [reviews, setReviews] = useState([]);

//   useEffect(() => {
//     // TODO: Fetch movie data from API/backend using id
//     // e.g., axios.get(`/api/movies/${id}`).then(res => setMovie(res.data));
//     // For now, hardcoded
//     setMovie(/* hardcoded object from Step 5 */);
//     // Fetch reviews similarly
//     setReviews([]);
//   }, [id]);

//   if (!movie) return <div>Loading...</div>;

const dummyMovie = {
  title: 'The Shawshank Redemption',
  year: '1994',
  rating: '9.3',
  duration: '142 min',
  genre: 'Drama',
  poster: 'https://images.unsplash.com/photo-1506748686214-e9df14d4d9d0?w=400&h=600&fit=crop' ,// Replace with actual URL or asset; based on screenshot, it's a neon cinema sign
  plot: 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.',
  director: { name: 'Frank Darabont', photo: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&h=150&fit=crop&crop=face' },
  cast: [
      { name: 'Christian Bale', photo: 'https://images.unsplash.com/photo-1560250097-0b93528c311a?w=150&h=150&fit=crop&crop=face' },
      { name: 'Heath Ledger', photo: 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150&h=150&fit=crop&crop=face' },
      { name: 'Aaron Eckhart', photo: 'https://images.unsplash.com/photo-1519345182560-3f2917c472ef?w=150&h=150&fit=crop&crop=face' }
    ],
};


  return (
    <div className="">
    <Navbar />
    
    <MovieInfo movie={dummyMovie} />
      
    </div>
  );
};

export default MovieDetails;
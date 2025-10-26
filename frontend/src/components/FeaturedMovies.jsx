import React from 'react';
import '../styles/FeaturedMovies.css';
// Sample data (replace with API fetch later if needed)

const movies = [
  {
    title: 'The Shawshank Redemption',
    year: 1994,
    rating: 9.3,
    genres: ['Drama'],
    poster: 'https://images.unsplash.com/photo-1604161546756-751af8e7912c?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=735'
  },
  {
    title: 'The Dark Knight',
    year: 2008,
    rating: 9,
    genres: ['Action', 'Crime'],
    poster: 'https://images.unsplash.com/photo-1506744038136-46273834b3fb?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=735'
  },
  // Add more for Inception, Pulp Fiction, The Matrix, Forrest Gump
  {
    title: 'Inception',
    year: 2010,
    rating: 8.8,
    genres: ['Action', 'Sci-Fi'],
    poster: 'https://via.placeholder.com/200x300?text=Inception'
  },
  {
    title: 'Pulp Fiction',
    year: 1994,
    rating: 8.9,
    genres: ['Crime', 'Drama'],
    poster: 'https://via.placeholder.com/200x300?text=Pulp+Fiction'
  },
  {
    title: 'The Matrix',
    year: 1999,
    rating: 8.7,
    genres: ['Action', 'Sci-Fi'],
    poster: 'https://via.placeholder.com/200x300?text=Matrix'
  },
  {
    title: 'Forrest Gump',
    year: 1994,
    rating: 8.8,
    genres: ['Drama', 'Romance'],
    poster: 'https://via.placeholder.com/200x300?text=Forrest+Gump'
  }
];

const FeaturedMovies = () => {
    return  (
        <section className='featured'>
            <div className='featured-header'>
                <h2>Featured Movies</h2>
                <a href="#view-all">View All</a>
            </div>
            <div className='movie-grid'>
                {/* Movie cards would go here */}
                {movies.map((movie, index) =>(
                <div key={index} className='movie-card'>
                    <img src={movie.poster} alt={movie.title} />
                    <h4>{movie.title}</h4>
                    <p>{movie.year} <span className='rating'>★ {movie.rating}  </span></p>
                    <p className='genres'>{movie.genres.join(' ')}</p>
                </div>
                ))}
            </div>

        </section>
    );
};


export default FeaturedMovies;
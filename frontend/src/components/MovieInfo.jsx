import React from 'react';
import { FaPlay, FaHeart, FaArrowLeft, FaStar } from 'react-icons/fa'; // For icons

const MovieInfo = ({ movie }) => {
  return (
    <div className="movieInfo mt-14">
        

        <div className='flex items-center justify-center'>

        <img src={movie.poster} alt={movie.title} className="movie-poster-large" />

    <div className='flex items-start flex-col gap-8 ml-8'>
        <button className="back-button cursor-pointer inline-block bg-yellow-300 px-4 py-2 rounded text-black" onClick={() => window.history.back()}>
            <span className='inline-block'><FaArrowLeft /></span> Back
        </button>
        
        <h2 className="movie-title-large text-3xl font-bold">{movie.title}</h2>
        <h3 > {movie.year} | <span className='text-yellow-300 text-md inline-block '> <FaStar /> </span> {movie.rating} | {movie.duration}</h3>
        <div className="inline-block bg-gray-800 text-white text-sm font-medium px-4 py-1.5 rounded-full shadow-sm">
            {movie.genre}
        </div>

        
    

    </div>

        </div>   

    </div>
  );
};

export default MovieInfo;
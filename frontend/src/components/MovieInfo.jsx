import React from "react";
import { FaPlay, FaHeart, FaArrowLeft, FaStar, FaCalendar, FaCalendarDay } from "react-icons/fa"; // For icons
import { formatDuration } from "../utils/formatDuration.js";
import { TrailerPlayer } from "./TrailerPlayer.jsx";
import { useState } from "react";


const MovieInfo = ({ movie }) => {

    
    const [trailerOpen, setTrailerOpen] = useState(false);


    console.log('Movie data received in MovieInfo component:', movie);

  return (
    <>
    <div className="movie-info">
      <div className=" main-content flex min-h-screen flex-col items-start justify-center p-4 md:flex-row md:p-8 lg:p-12  ">
        <div className="poster">
          <img src={movie.posterUrl} alt={movie.title} />
        </div>
       
        <div className="details flex flex-col gap-8 items-start">
           <button onClick={() => window.history.back()}
          className="
            px-3 py-1 
            flex flex-row justify-center items-center gap-2 
            rounded 
            bg-transparent text-white 
            hover:bg-yellow-500 hover:text-black
            transition duration-200
            cursor-pointer
          "
        >
          <FaArrowLeft /> Back
        </button>
          <h1 className="text-3xl font-bold">{movie.title}</h1>
          <div className="flex flex-row gap-6 items-center">
              {movie.releaseYear}  <span>  • </span> <div> <span className="inline-block flex flex-row justify-center text-yellow-300 text-md "> <FaStar className="pt-1" /></span> {movie.avgRating == 0 ? 'N/A' : movie.avgRating} </div>  <span> • </span> {formatDuration(movie.duration)}

          </div>
          <div className="genres flex flex-row gap-3">
          {movie.genres.map((genre, index) => (
          <p className="inline-block rounded bg-gray-800 px-2 py-1">{genre.name}</p>
          ))}
          </div>
          <div className="buttons flex flex-row gap-4">
            <button className="play flex flex-row justify-center items-center gap-2 rounded px-4 py-2"
              onClick={() => setTrailerOpen(true)}
            >
              <FaPlay className="h-5 w-5" />
              Play Trailer
            </button>
            <button className=" bg-black hover:bg-yellow-500 flex cursor-pointer hover:text-black border border-gray-700 flex-row justify-center items-center gap-2 rounded px-4 py-2">
              <FaHeart /> Add to Favorites
            </button>
          </div>
          <div>
            <h2 className="text-xl ">Plot</h2>
            <p className="plot max-w-3xl">{movie.description}</p>
          </div>
          <div className="director flex flex-col items-start gap-3">
            <h3 className="text-xl">Director</h3>
            <div className="person flex flex-col items-start  text-gray-200">
              {movie.directors.map((director, index) => (
                <div key={index} className="person flex flex-col items-start ">
                  <img src={director.profileUrl} alt={director.name} />
                  <span>{director.name}</span>
                </div>
              ))}
            </div>
          </div>
          <div className="cast flex flex-col items-start gap-3">
            <h3 className="text-xl">Cast</h3>
            <div className="cast-list flex flex-row gap-6 overflow-x-auto text-gray-200">
              {movie.cast.map((actor, index) => (
                <div key={index} className="person flex flex-col items-start justify-start gap-2 min-w-[80px]">
  <img src={actor.profileUrl} alt={actor.name} className="w-full h-auto object-cover" />
  <span className="text-left">{actor.name}</span>
</div>
              ))}
            </div>
          </div>
        </div>
      </div>
      
    </div>
    <TrailerPlayer movie={movie} open={trailerOpen} onOpenChange={setTrailerOpen} />
    </>
  );
};

export default MovieInfo;
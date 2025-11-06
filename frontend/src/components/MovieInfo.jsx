import React, { useState } from "react";
import {
  FaPlay,
  FaHeart,
  FaArrowLeft,
  FaStar,
} from "react-icons/fa";
import { formatDuration } from "../utils/formatDuration.js";
import { TrailerPlayer } from "./TrailerPlayer.jsx";

const MovieInfo = ({ movie }) => {
  const [trailerOpen, setTrailerOpen] = useState(false);

  return (
    <>
      <div className="movie-info bg-black text-white min-h-screen w-full overflow-hidden">
        {/* Main container */}
          <div className="max-w-[1460px] mx-auto px-4 ">

        <div className="main-content flex flex-col md:flex-row items-center md:items-start justify-center gap-8 p-4 md:p-8 lg:p-12">
          
          {/* Movie Poster */}
          <div className="poster w-full md:w-1/3 flex justify-center">
            <img
              src={movie.posterUrl}
              alt={movie.title}
              className="rounded-lg shadow-lg w-full max-w-sm md:max-w-full object-cover"
            />
          </div>

          {/* Details Section */}
          <div className="details flex flex-col gap-6 md:gap-8 items-start w-full md:w-2/3">
            {/* Back Button */}
            {/* <button
              onClick={() => window.history.back()}
              className="px-2 py-1  text-sm flex flex-row items-center gap-2 rounded bg-transparent text-white border border-gray-700 hover:bg-[#ffd700] hover:text-black transition duration-200 cursor-pointer"
            >
              <FaArrowLeft className="text-xs" /> Back
            </button> */}

            {/* Title */}
            <h1 className="text-2xl md:text-4xl font-bold leading-tight">
              {movie.title}
            </h1>

            {/* Rating & Info Row */}
            <div className="flex flex-wrap items-center gap-3 text-gray-300 text-sm md:text-base">
              <span>{movie.releaseYear}</span>
              <span>•</span>
              <span className="flex flex-row items-center gap-1 text-yellow-300">
                <FaStar />
                {movie.avgRating == 0 ? "N/A" : movie.avgRating.toFixed(1)}
              </span>
              <span>•</span>
              <span>{formatDuration(movie.duration)}</span>
            </div>

            {/* Genres */}
            <div className="genres flex flex-wrap gap-2">
              {movie.genres.map((genre, index) => (
                <p
                  key={index}
                  className="inline-block rounded bg-gray-700 px-2 py-1 text-sm "
                >
                  {genre.name}
                </p>
              ))}
            </div>

            {/* Buttons */}
            <div className="buttons flex flex-wrap gap-3">
              <button
                className="flex flex-row items-center gap-2 bg-[#ffd700] hover:bg-[#ffed4e] text-black font-semibold rounded px-4 py-2 transition duration-200 cursor-pointer"
                onClick={() => setTrailerOpen(true)}
              >
                <FaPlay /> Play Trailer
              </button>

              <button className="bg-transparent hover:bg-[#ffd700] hover:text-black border border-gray-700 flex flex-row items-center gap-2 rounded px-4 py-2 transition duration-200 cursor-pointer">
                <FaHeart /> Add to Favorites
              </button>
            </div>

            {/* Plot */}
            <div className="w-full">
              <h2 className="text-xl font-semibold mb-2">Plot</h2>
              <p className="plot max-w-3xl text-gray-400 leading-relaxed">
                {movie.description}
              </p>
            </div>

            {/* Director */}
            <div className="director w-full">
              <h3 className="text-xl font-semibold mb-2">Director</h3>
              <div className="flex flex-wrap gap-4">
                {movie.directors.map((director, index) => (
                  <div
                    key={index}
                    className="flex flex-col items-center text-gray-200"
                  >
                    <img
                      src={director.profileUrl}
                      alt={director.name}
                      className="w-16 h-16 object-cover rounded-full border border-gray-700"
                    />
                    <span className="mt-2 text-xs">{director.name}</span>
                  </div>
                ))}
              </div>
            </div>

            {/* Cast */}
            <div className="cast w-full">
              <h3 className="text-xl font-semibold mb-2">Cast</h3>
              <div className="flex flex-row gap-4 overflow-x-auto pb-2">
                {movie.cast.map((actor, index) => (
                  <div
                    key={index}
                    className="flex flex-col items-center min-w-[80px] text-gray-200"
                  >
                    <img
                      src={actor.profileUrl}
                      alt={actor.name}
                      className="w-16 h-16 object-cover rounded-full border border-gray-700"
                    />
                    <span className="mt-1 text-xs text-center">
                      {actor.name}
                    </span>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>

      </div>
      {/* Trailer Modal */}
      <TrailerPlayer
        movie={movie}
        open={trailerOpen}
        onOpenChange={setTrailerOpen}
      />
    </>
  );
};

export default MovieInfo;

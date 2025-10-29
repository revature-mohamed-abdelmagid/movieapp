package com.revature.movieapp.movieapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.revature.movieapp.movieapp.dto.MovieWithGenresDTO;
import com.revature.movieapp.movieapp.model.Movie;
import com.revature.movieapp.movieapp.model.MovieGenres;
import com.revature.movieapp.movieapp.repository.MovieGenresRepository;
import com.revature.movieapp.movieapp.repository.MovieRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MovieService {
    
    private final MovieRepository movieRepository;
    private final MovieGenresRepository movieGenresRepository;

    public MovieService(MovieRepository movieRepository, MovieGenresRepository movieGenresRepository) {
        this.movieRepository = movieRepository;
        this.movieGenresRepository = movieGenresRepository;
    }


    // get all movies
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // get all movies with genres
    public List<MovieWithGenresDTO> getAllMoviesWithGenres() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream().map(this::convertToMovieWithGenresDTO).collect(Collectors.toList());
    }

    // Convert Movie to MovieWithGenresDTO
    private MovieWithGenresDTO convertToMovieWithGenresDTO(Movie movie) {
        List<MovieGenres> movieGenres = movieGenresRepository.findByMovieId(movie.getMovieId());
        List<String> genreNames = movieGenres.stream()
                .map(mg -> mg.getGenre().getGenreName())
                .collect(Collectors.toList());

        return new MovieWithGenresDTO(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getReleaseYear(),
                movie.getDuration(),
                movie.getDescription(),
                movie.getLanguage(),
                movie.getCountry(),
                movie.getPosterUrl(),
                movie.getAvgRating(),
                genreNames
        );
    }

    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie getMovieById(Long id) {
        
        return movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    public Movie updateMovie(Long id, Movie movie) {
        
        Movie existingMovie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
        existingMovie.setTitle(movie.getTitle());
        existingMovie.setReleaseYear(movie.getReleaseYear());
        existingMovie.setDuration(movie.getDuration());
        existingMovie.setDescription(movie.getDescription());
        existingMovie.setLanguage(movie.getLanguage());
        existingMovie.setCountry(movie.getCountry());
        existingMovie.setPosterUrl(movie.getPosterUrl());
        existingMovie.setAvgRating(movie.getAvgRating());
        return movieRepository.save(existingMovie);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    
    }



}

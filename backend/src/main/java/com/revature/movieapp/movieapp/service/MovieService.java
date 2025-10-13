package com.revature.movieapp.movieapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.revature.movieapp.movieapp.model.Movie;
import com.revature.movieapp.movieapp.repository.MovieRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MovieService {
    
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }


    // get all movies
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
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

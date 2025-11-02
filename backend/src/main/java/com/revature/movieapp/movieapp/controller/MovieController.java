package com.revature.movieapp.movieapp.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.revature.movieapp.movieapp.dto.MovieFullDetailsDTO;
import com.revature.movieapp.movieapp.dto.MovieWithGenresDTO;
import com.revature.movieapp.movieapp.model.Movie;
import com.revature.movieapp.movieapp.service.MovieService;

import jakarta.validation.Valid;



@RestController
public class MovieController {
    
private final MovieService movieService;
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }
    

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/movies/with-genres")
    public ResponseEntity<List<MovieWithGenresDTO>> getAllMoviesWithGenres() {
        List<MovieWithGenresDTO> movies = movieService.getAllMoviesWithGenres();
        return ResponseEntity.ok(movies);
    }

    
    @PostMapping("/movies")
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody Movie movie) {
        Movie createdMovie = movieService.createMovie(movie);
        return ResponseEntity.ok(createdMovie);
    }

    // all the CRUD operations here

    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.getMovieById(id);
        return ResponseEntity.ok(movie);
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        Movie updatedMovie = movieService.updateMovie(id, movie);
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    // Get movie with full details including genres, cast, and crew
    @GetMapping("/movies/full-details/{id}")
    public ResponseEntity<MovieFullDetailsDTO> getMovieFullDetails(@PathVariable Long id) {
        try {
            MovieFullDetailsDTO movieDetails = movieService.getMovieFullDetails(id);
            return ResponseEntity.ok(movieDetails);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get all movies with full details
    @GetMapping("/movies/full-details")
    public ResponseEntity<List<MovieFullDetailsDTO>> getAllMoviesWithFullDetails() {
        try {
            List<MovieFullDetailsDTO> moviesDetails = movieService.getAllMoviesWithFullDetails();
            return ResponseEntity.ok(moviesDetails);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

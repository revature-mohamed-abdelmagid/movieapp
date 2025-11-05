package com.revature.movieapp.movieapp.service;

import com.revature.movieapp.movieapp.model.Genre;
import com.revature.movieapp.movieapp.model.Movie;
import com.revature.movieapp.movieapp.model.MovieGenres;
import com.revature.movieapp.movieapp.repository.GenreRepository;
import com.revature.movieapp.movieapp.repository.MovieGenresRepository;
import com.revature.movieapp.movieapp.repository.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class GenreService {

    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;
    private final MovieGenresRepository movieGenresRepository;

    public GenreService(GenreRepository genreRepository,
                       MovieRepository movieRepository,
                       MovieGenresRepository movieGenresRepository) {
        this.genreRepository = genreRepository;
        this.movieRepository = movieRepository;
        this.movieGenresRepository = movieGenresRepository;
    }

    /**
     * Get all genres
     */
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    /**
     * Add genre to a movie
     */
    public void addGenreToMovie(Long movieId, Long genreId) {
        // Validate movie exists
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + movieId));

        // Validate genre exists
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new RuntimeException("Genre not found with id: " + genreId));

        // Create MovieGenres relationship
        MovieGenres.MovieGenresId id = new MovieGenres.MovieGenresId(movieId, genreId);
        MovieGenres movieGenres = new MovieGenres();
        movieGenres.setId(id);
        movieGenres.setMovie(movie);
        movieGenres.setGenre(genre);

        movieGenresRepository.save(movieGenres);
    }

    /**
     * Add multiple genres to a movie
     */
    public void addGenresToMovie(Long movieId, List<Long> genreIds) {
        for (Long genreId : genreIds) {
            addGenreToMovie(movieId, genreId);
        }
    }

    /**
     * Remove genre from movie
     */
    public void removeGenreFromMovie(Long movieId, Long genreId) {
        MovieGenres.MovieGenresId id = new MovieGenres.MovieGenresId(movieId, genreId);
        movieGenresRepository.deleteById(id);
    }
}


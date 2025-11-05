package com.revature.movieapp.movieapp.controller;

import com.revature.movieapp.movieapp.model.Genre;
import com.revature.movieapp.movieapp.repository.GenreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    /**
     * Get all available genres
     */
    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        return ResponseEntity.ok(genres);
    }
}


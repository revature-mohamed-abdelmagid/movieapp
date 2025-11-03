package com.revature.movieapp.movieapp.controller;

import com.revature.movieapp.movieapp.dto.MovieCastCrewDTO;
import com.revature.movieapp.movieapp.model.MovieParticipation;
import com.revature.movieapp.movieapp.service.MovieCastService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieCastController {

    private final MovieCastService movieCastService;

    public MovieCastController(MovieCastService movieCastService) {
        this.movieCastService = movieCastService;
    }

    /**
     * Add a single cast/crew member to a movie
     * POST /api/movies/{movieId}/cast
     */
    @PostMapping("/{movieId}/cast")
    public ResponseEntity<String> addCastCrewToMovie(
            @PathVariable Long movieId,
            @Valid @RequestBody MovieCastCrewDTO castCrewDTO) {
        movieCastService.addCastCrewToMovie(movieId, castCrewDTO);
        return ResponseEntity.ok("Cast/crew member added successfully");
    }

    /**
     * Add multiple cast/crew members to a movie at once
     * POST /api/movies/{movieId}/cast/bulk
     */
    @PostMapping("/{movieId}/cast/bulk")
    public ResponseEntity<String> addMultipleCastCrewToMovie(
            @PathVariable Long movieId,
            @Valid @RequestBody List<MovieCastCrewDTO> castCrewList) {
        movieCastService.addMultipleCastCrewToMovie(movieId, castCrewList);
        return ResponseEntity.ok("Cast/crew members added successfully");
    }

    /**
     * Get all cast/crew for a movie
     * GET /api/movies/{movieId}/cast
     */
    @GetMapping("/{movieId}/cast")
    public ResponseEntity<List<MovieParticipation>> getCastCrewForMovie(@PathVariable Long movieId) {
        List<MovieParticipation> castCrew = movieCastService.getCastCrewForMovie(movieId);
        return ResponseEntity.ok(castCrew);
    }

    /**
     * Remove a cast/crew member from a movie
     * DELETE /api/movies/cast/{participationId}
     */
    @DeleteMapping("/cast/{participationId}")
    public ResponseEntity<Void> removeCastCrewFromMovie(@PathVariable Long participationId) {
        movieCastService.removeCastCrewFromMovie(participationId);
        return ResponseEntity.noContent().build();
    }
}


package com.revature.movieapp.movieapp.dto;

import com.revature.movieapp.movieapp.model.Movie;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for creating a movie with cast and crew in one request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieWithCastCrewDTO {
    
    @NotNull(message = "Movie details are required")
    @Valid
    private Movie movie;
    
    /**
     * List of cast and crew to add to the movie
     * Can be empty if adding cast/crew separately
     */
    private List<@Valid MovieCastCrewDTO> castCrew;
}


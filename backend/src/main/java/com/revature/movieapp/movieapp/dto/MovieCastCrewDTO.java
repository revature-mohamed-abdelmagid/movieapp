package com.revature.movieapp.movieapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for adding cast/crew to a movie
 * Represents a person's participation in a movie with their role
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieCastCrewDTO {
    
    @NotNull(message = "Person ID is required")
    private Long personId;
    
    @NotNull(message = "Role ID is required")
    private Long roleId;
    
    /**
     * Optional note about the participation
     * For actors: character name (e.g., "Bruce Wayne")
     * For directors/crew: can be empty or descriptive note
     */
    @Size(max = 500, message = "Note cannot exceed 500 characters")
    private String characterName;
}


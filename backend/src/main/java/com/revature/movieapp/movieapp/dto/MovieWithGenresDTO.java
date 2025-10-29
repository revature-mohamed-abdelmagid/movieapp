package com.revature.movieapp.movieapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for Movie with Genres
 * Contains movie information along with its associated genres
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieWithGenresDTO {
    
    private Long movieId;
    private String title;
    private Long releaseYear;
    private Long duration;
    private String description;
    private String language;
    private String country;
    private String posterUrl;
    private Double avgRating;
    private List<String> genres; // List of genre names
}
package com.revature.movieapp.movieapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


@Entity
public class Movie {

    // please add the necessary annotations and fields
    @Id
    private Long movieId;
    private String title;
    private Long releaseYear;
    private Long duration;
    private String description;
    private String language;
    private String country;
    private String posterUrl;
    private Double avgRating;

    

}

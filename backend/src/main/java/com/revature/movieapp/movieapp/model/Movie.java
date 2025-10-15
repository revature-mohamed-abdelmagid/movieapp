package com.revature.movieapp.movieapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "movies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    // please add the necessary annotations and fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;

    @Size(min = 1, max = 50)
    private String title;
    private Long releaseYear;
    private Long duration;
    private String description;
    private String language;
    private String country;
    private String posterUrl;
    private Double avgRating;


    

}

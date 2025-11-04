package com.revature.movieapp.movieapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.revature.movieapp.movieapp.model.Review;

/**
 * Comprehensive DTO for movie with complete details including genres, cast, and crew
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieFullDetailsDTO {
    
    // Movie basic information
    private Long movieId;
    private String title;
    private Long releaseYear;
    private Long duration;
    private String description;
    private String language;
    private String country;
    private String posterUrl;
    private String trailerUrl;
    private Double avgRating;
    
    // Genres
    private List<GenreDTO> genres;
    
    // Cast and crew
    private List<PersonParticipationDTO> cast;
    private List<PersonParticipationDTO> directors;
    private List<PersonParticipationDTO> producers;
    private List<PersonParticipationDTO> writers;

    private List<ReviewDTO> reviews;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GenreDTO {
        private Long genreId;
        private String name;
        private String description;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PersonParticipationDTO {
        private Long personId;
        private String name;
        private LocalDate birthDate;
        private String bio;
        private String profileUrl;
        private List<RoleDTO> roles;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RoleDTO {
        private Long roleId;
        private String roleName;
        private String roleDescription;
        private String note; // Additional note about the role (e.g., "lead", "cameo")
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReviewDTO {
        private Long reviewId;
        private Long userId;
        private Long movieId;
        private Long rating;
        private String reviewText;
        private Integer helpfulCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String userName;
    }
}
package com.revature.movieapp.movieapp.service;
import com.revature.movieapp.movieapp.model.Movie;
import com.revature.movieapp.movieapp.model.Review;
import com.revature.movieapp.movieapp.model.User;
import com.revature.movieapp.movieapp.repository.MovieRepository;
import com.revature.movieapp.movieapp.repository.ReviewRepository;
import com.revature.movieapp.movieapp.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;



/**
 * Service providing common operations for Review entities.
 * Assumes Review has relations to Movie and User via getMovie() and getAuthor().
 */
@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         MovieRepository movieRepository,
                         UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create and persist a new review. If the review references a movie or user by id,
     * this method verifies those exist and attaches managed entities.
     */

    public Review createReview(Review review) {
        Objects.requireNonNull(review, "review must not be null");

        // attach existing movie if provided
        if (review.getMovieId() != null) {
            Movie movie = movieRepository.findById(review.getMovieId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Movie not found with id: " + review.getMovieId()));

            if (movie.getAvgRating() == 0) {
                movie.setAvgRating(Double.valueOf(review.getRating()));
            } else {
                movie.setAvgRating((movie.getAvgRating() + Double.valueOf(review.getRating()))/2);
            }
            
            review.setMovieId(movie.getMovieId());
        }

        // attach existing user if provided
        if (review.getUserId() != null) {
            User user = userRepository.findById(review.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "User not found with id: " + review.getUserId()));
            review.setUserId(user.getId());
        }


        review.setHelpfulCount(0);

        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    /**
     * Find a review by id.
     */
    @Transactional(readOnly = true)
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
    }

    /**
     * Return all reviews.
     */
    @Transactional(readOnly = true)
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Get the username along with review for movie details

    /**
     * Return all reviews for a given movie id.
     */
    @Transactional(readOnly = true)
    public List<Review> getReviewsByMovieId(Long movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new ResourceNotFoundException("Movie not found with id: " + movieId);
        }
        // Assumes ReviewRepository has a method findByMovieId(Long movieId).
        return reviewRepository.findByMovieId(movieId);
    }

    /**
     * Return all reviews by a given user id.
     */
    @Transactional(readOnly = true)
    public List<Review> getReviewsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        // Assumes ReviewRepository has a method findByUserId(Long userId).
        return reviewRepository.findByUserId(userId);
    }

    /**
     * Update an existing review. Only non-null fields from updatedReview are applied.
     */
    public Optional<Review> updateReview(Long id, Review updatedReview) {
        Review existing = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));

        if (updatedReview.getRating() != null) {
            existing.setRating(updatedReview.getRating());
            Movie movie = movieRepository.findById(existing.getMovieId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Movie not found with id: " + existing.getMovieId()));
            movie.setAvgRating((movie.getAvgRating() + updatedReview.getRating())/2);
        }
        if (updatedReview.getReviewText() != null) {
            existing.setReviewText(updatedReview.getReviewText());
        }

        existing.setUpdatedAt(LocalDateTime.now());
        return Optional.of(reviewRepository.save(existing));
    }

    /**
     * Delete a review by id.
     */
    public boolean deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Review not found with id: " + id);
        }
        Review review = reviewRepository.findById(id).get();
        if (review.getMovieId() != null) {
            Movie movie = movieRepository.findById(review.getMovieId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Movie not found with id: " + review.getMovieId()));
            // Adjust average rating - simplistic approach update the average rating by removing this review's rating
            if((movie.getAvgRating() * reviewRepository.findByMovieId(movie.getMovieId()).size() - review.getRating()) <= 0){
                movie.setAvgRating(0.0);
            }
            else {
                movie.setAvgRating((movie.getAvgRating() * reviewRepository.findByMovieId(movie.getMovieId()).size()
                        - review.getRating())
                        / (reviewRepository.findByMovieId(movie.getMovieId()).size() - 1));
            }
            
            

        }
    
        reviewRepository.deleteById(id);

        return true;
    }

    /**
     * Simple runtime exception used when required resources are missing.
     */
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    public Optional<Review> patch(Long id, Review partial) {

        Review existing = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));


        if (partial.getRating() != null) {
            existing.setRating(partial.getRating());
            Movie movie = movieRepository.findById(existing.getMovieId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Movie not found with id: " + existing.getMovieId()));
            movie.setAvgRating((movie.getAvgRating() + partial.getRating())/2); 
        }
        if (partial.getReviewText() != null) {
            existing.setReviewText(partial.getReviewText());
        }

        existing.setUpdatedAt(LocalDateTime.now());
        return Optional.of(reviewRepository.save(existing));
    }

    /**
     * Check if a user (by username) owns a specific review
     */
    public boolean isReviewOwner(Long reviewId, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        
        return review.getUserId().equals(user.getId());
    }
}
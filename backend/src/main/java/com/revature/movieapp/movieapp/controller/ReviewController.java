package com.revature.movieapp.movieapp.controller;
import com.revature.movieapp.movieapp.model.Review;
import com.revature.movieapp.movieapp.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;





@RestController
@RequestMapping("/api/reviews")
@Validated
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Helper method to get the currently authenticated username
     */
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return null;
    }

    /**
     * Helper method to check if current user can modify a review
     * Returns true if user is the owner or an admin
     */
    private boolean canModifyReview(Review review) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // Check if user is admin
        boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        if (isAdmin) {
            return true;
        }

        // Check if user owns the review
        String currentUsername = getCurrentUsername();
        if (currentUsername != null) {
            return reviewService.isReviewOwner(review.getReviewId(), currentUsername);
        }

        return false;
    }

    // Get all reviews
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    // Get review by id
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        Review review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    // Create a new review
    @PostMapping
    public ResponseEntity<Review> createReview(@Valid @RequestBody Review review) {
        Review saved = reviewService.createReview(review);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getReviewId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    // Update an existing review (full replace)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Long id, @Valid @RequestBody Review review) {
        // Get the existing review
        Review existingReview = reviewService.getReviewById(id);
        
        // Check if current user can modify this review
        if (!canModifyReview(existingReview)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("You do not have permission to update this review");
        }

        Optional<Review> updated = reviewService.updateReview(id, review);
        return updated.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Partial update (e.g., rating or comment)
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchReview(@PathVariable Long id, @RequestBody Review partial) {
        // Get the existing review
        Review existingReview = reviewService.getReviewById(id);
        
        // Check if current user can modify this review
        if (!canModifyReview(existingReview)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("You do not have permission to update this review");
        }

        Optional<Review> updated = reviewService.patch(id, partial);
        return updated.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete review
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        System.out.println("=== DELETE REVIEW REQUEST RECEIVED ===");
        System.out.println("Review ID: " + id);
        
        // Check authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + authentication);
        System.out.println("Is Authenticated: " + (authentication != null && authentication.isAuthenticated()));
        if (authentication != null) {
            System.out.println("Principal: " + authentication.getPrincipal());
            System.out.println("Authorities: " + authentication.getAuthorities());
        }
        
        // Get the existing review
        Review existingReview = reviewService.getReviewById(id);
        System.out.println("Review found - User ID: " + existingReview.getUserId());
        
        // Check if current user can modify this review
        if (!canModifyReview(existingReview)) {
            System.out.println("Authorization FAILED - User cannot modify this review");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("You do not have permission to delete this review");
        }

        System.out.println("Authorization PASSED - Proceeding with deletion");
        boolean deleted = reviewService.deleteReview(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Get reviews for a specific movie
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Review>> getReviewsByMovie(@PathVariable Long movieId) {
        List<Review> reviews = reviewService.getReviewsByMovieId(movieId);
        return ResponseEntity.ok(reviews);
    }

    // Get reviews by a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUser(@PathVariable Long userId) {
        List<Review> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }

    // Test endpoint to verify authentication works
    @GetMapping("/test-auth")
    public ResponseEntity<?> testAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok("Authenticated as: " + authentication.getName() + 
                                    " with authorities: " + authentication.getAuthorities());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
    }

}
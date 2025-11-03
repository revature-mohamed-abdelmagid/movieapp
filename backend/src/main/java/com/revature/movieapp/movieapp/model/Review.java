package com.revature.movieapp.movieapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Review entity represents a user's review for a movie.
 *
 * Design decisions:
 * - Keep a simple, decoupled model (store movieId and userId as primitives) so the
 *   entity doesn't depend on other entities that may not yet be implemented in the repo.
 * - Include rating (1-5), a free-text comment, and created/updated timestamps.
 */
@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id")
	private Long reviewId;

	/** The id of the user who created the review. */
	@Column(name = "user_id", nullable = false)
	private Long userId;

	/** The id of the movie this review refers to. */
	@Column(name = "movie_id", nullable = false)
	private Long movieId;

	/** Rating on a 1-5 scale. */
	@Min(1)
	@Max(5)
	@Column(nullable = false)
	private Long rating;

	/** Free-text review content. */
	@Size(max = 5000)
	@Column(name = "review_text", columnDefinition = "TEXT", length = 5000)
	private String reviewText;

	/** Number of helpful votes for this review. */
	@Column(name = "helpful_count", nullable = false)
	@Builder.Default
	private Integer helpfulCount = 0;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		if (this.helpfulCount == null) this.helpfulCount = 0;
		this.createdAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

}

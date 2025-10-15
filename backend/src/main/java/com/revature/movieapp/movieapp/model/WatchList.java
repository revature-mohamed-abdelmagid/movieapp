package com.revature.movieapp.movieapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "watchlists")
public class WatchList {
/*
 * 4. Watchlist
Represents a user’s list of movies they want to watch (not explicitly detailed in the user stories but implied as a common feature for movie apps).
Attributes:

watchlist_id: Unique identifier for the watchlist entry (e.g., UUID or integer).
user_id: Foreign key referencing the user who owns the watchlist.
watchlist_name: Name of the watchlist (e.g., "My Favorites", "To Watch").
movie_id: Foreign key referencing the movie added to the watchlist.
added_at: Timestamp when the movie was added to the watchlist.
 */

 // please create the watchlist entity class with the above attributes and necessary annotations

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long watchlistId;
    // I wanna have this userid as a foreign key to the user table
    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;
    private String watchlistName;
    @JoinColumn(name = "movie_id", nullable = false)
    private Long movieId;
    private String addedAt;
    

}

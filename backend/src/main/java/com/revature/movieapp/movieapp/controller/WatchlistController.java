package com.revature.movieapp.movieapp.controller;

import com.revature.movieapp.movieapp.dto.MessageResponse;
import com.revature.movieapp.movieapp.model.Movie;
import com.revature.movieapp.movieapp.model.User;
import com.revature.movieapp.movieapp.model.Watchlist;
import com.revature.movieapp.movieapp.model.WatchlistItem;
import com.revature.movieapp.movieapp.service.UserService;
import com.revature.movieapp.movieapp.service.WatchlistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST Controller for Watchlist operations
 * Handles CRUD operations for watchlists and watchlist items
 * All endpoints require authentication
 */
@RestController
@RequestMapping("/api/watchlists")
public class WatchlistController {
    
    private final WatchlistService watchlistService;
    private final UserService userService;

    public WatchlistController(WatchlistService watchlistService, UserService userService) {
        this.watchlistService = watchlistService;
        this.userService = userService;
    }

    /**
     * Get the current authenticated user
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new RuntimeException("User not authenticated");
        }
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userService.getUserByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Get all watchlists for the current user
     * GET /api/watchlists
     */
    @GetMapping
    public ResponseEntity<?> getUserWatchlists() {
        try {
            User currentUser = getCurrentUser();
            List<Watchlist> watchlists = watchlistService.getUserWatchlists(currentUser.getId());
            return ResponseEntity.ok(watchlists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error retrieving watchlists: " + e.getMessage()));
        }
    }

    /**
     * Get a specific watchlist by ID
     * GET /api/watchlists/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getWatchlistById(@PathVariable Long id) {
        try {
            User currentUser = getCurrentUser();
            Optional<Watchlist> watchlist = watchlistService.getWatchlistById(id);
            
            if (watchlist.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            // Check if user owns this watchlist
            if (!watchlist.get().getUserId().equals(currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Access denied: You don't own this watchlist"));
            }
            
            return ResponseEntity.ok(watchlist.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error retrieving watchlist: " + e.getMessage()));
        }
    }

    /**
     * Create a new watchlist
     * POST /api/watchlists
     * Body: {"name": "My Favorites", "description": "Movies I love"}
     */
    @PostMapping
    public ResponseEntity<?> createWatchlist(@Valid @RequestBody Watchlist watchlist) {
        try {
            User currentUser = getCurrentUser();
            
            // Set the user ID for the watchlist
            watchlist.setUserId(currentUser.getId());
            
            Watchlist createdWatchlist = watchlistService.createWatchlist(watchlist);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdWatchlist);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error creating watchlist: " + e.getMessage()));
        }
    }

    /**
     * Update an existing watchlist
     * PUT /api/watchlists/{id}
     * Body: {"name": "Updated Name", "description": "Updated description"}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWatchlist(@PathVariable Long id, @Valid @RequestBody Watchlist watchlist) {
        try {
            User currentUser = getCurrentUser();
            
            // Check if watchlist exists and user owns it
            if (!watchlistService.isWatchlistOwnedByUser(id, currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Access denied: You don't own this watchlist"));
            }
            
            Watchlist updatedWatchlist = watchlistService.updateWatchlist(id, watchlist);
            return ResponseEntity.ok(updatedWatchlist);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error updating watchlist: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error updating watchlist: " + e.getMessage()));
        }
    }

    /**
     * Delete a watchlist
     * DELETE /api/watchlists/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWatchlist(@PathVariable Long id) {
        try {
            User currentUser = getCurrentUser();
            
            // Check if watchlist exists and user owns it
            if (!watchlistService.isWatchlistOwnedByUser(id, currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Access denied: You don't own this watchlist"));
            }
            
            watchlistService.deleteWatchlist(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error deleting watchlist: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error deleting watchlist: " + e.getMessage()));
        }
    }

    /**
     * Get all items (movies) in a watchlist
     * GET /api/watchlists/{id}/items
     */
    @GetMapping("/{id}/items")
    public ResponseEntity<?> getWatchlistItems(@PathVariable Long id) {
        try {
            User currentUser = getCurrentUser();
            
            // Check if watchlist exists and user owns it
            if (!watchlistService.isWatchlistOwnedByUser(id, currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Access denied: You don't own this watchlist"));
            }
            
            List<WatchlistItem> items = watchlistService.getWatchlistItems(id);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error retrieving watchlist items: " + e.getMessage()));
        }
    }

    /**
     * Get all movies in a watchlist with full movie details
     * GET /api/watchlists/{id}/movies
     */
    @GetMapping("/{id}/movies")
    public ResponseEntity<?> getWatchlistMovies(@PathVariable Long id) {
        try {
            User currentUser = getCurrentUser();
            
            // Check if watchlist exists and user owns it
            if (!watchlistService.isWatchlistOwnedByUser(id, currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Access denied: You don't own this watchlist"));
            }
            
            List<Movie> movies = watchlistService.getWatchlistMovies(id);
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error retrieving watchlist movies: " + e.getMessage()));
        }
    }

    /**
     * Add a movie to a watchlist
     * POST /api/watchlists/{id}/items
     * Body: {"movieId": 123}
     */
    @PostMapping("/{id}/items")
    public ResponseEntity<?> addMovieToWatchlist(@PathVariable Long id, @RequestBody Map<String, Long> request) {
        try {
            User currentUser = getCurrentUser();
            
            // Check if watchlist exists and user owns it
            if (!watchlistService.isWatchlistOwnedByUser(id, currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Access denied: You don't own this watchlist"));
            }
            
            Long movieId = request.get("movieId");
            if (movieId == null) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("movieId is required"));
            }
            
            WatchlistItem item = watchlistService.addMovieToWatchlist(id, movieId);
            return ResponseEntity.status(HttpStatus.CREATED).body(item);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse(e.getMessage()));
            } else if (e.getMessage().contains("already in")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new MessageResponse(e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error adding movie to watchlist: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error adding movie to watchlist: " + e.getMessage()));
        }
    }

    /**
     * Remove a movie from a watchlist
     * DELETE /api/watchlists/{watchlistId}/items/{movieId}
     */
    @DeleteMapping("/{watchlistId}/items/{movieId}")
    public ResponseEntity<?> removeMovieFromWatchlist(@PathVariable Long watchlistId, @PathVariable Long movieId) {
        try {
            User currentUser = getCurrentUser();
            
            // Check if watchlist exists and user owns it
            if (!watchlistService.isWatchlistOwnedByUser(watchlistId, currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Access denied: You don't own this watchlist"));
            }
            
            watchlistService.removeMovieFromWatchlist(watchlistId, movieId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error removing movie from watchlist: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error removing movie from watchlist: " + e.getMessage()));
        }
    }
}

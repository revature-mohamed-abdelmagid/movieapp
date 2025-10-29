package com.revature.movieapp.movieapp.service;

import com.revature.movieapp.movieapp.model.Movie;
import com.revature.movieapp.movieapp.model.Watchlist;
import com.revature.movieapp.movieapp.model.WatchlistItem;
import com.revature.movieapp.movieapp.repository.MovieRepository;
import com.revature.movieapp.movieapp.repository.WatchlistItemRepository;
import com.revature.movieapp.movieapp.repository.WatchlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for Watchlist operations
 * Handles business logic for watchlists and watchlist items
 */
@Service
@Transactional
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final WatchlistItemRepository watchlistItemRepository;
    private final MovieRepository movieRepository;

    public WatchlistService(WatchlistRepository watchlistRepository,
                           WatchlistItemRepository watchlistItemRepository,
                           MovieRepository movieRepository) {
        this.watchlistRepository = watchlistRepository;
        this.watchlistItemRepository = watchlistItemRepository;
        this.movieRepository = movieRepository;
    }

    /**
     * Get all watchlists for a specific user
     */
    public List<Watchlist> getUserWatchlists(Long userId) {
        return watchlistRepository.findByUserId(userId);
    }

    /**
     * Get a specific watchlist by ID
     */
    public Optional<Watchlist> getWatchlistById(Long watchlistId) {
        return watchlistRepository.findById(watchlistId);
    }

    /**
     * Create a new watchlist for a user
     */
    public Watchlist createWatchlist(Watchlist watchlist) {
        return watchlistRepository.save(watchlist);
    }

    /**
     * Update an existing watchlist
     */
    public Watchlist updateWatchlist(Long watchlistId, Watchlist updatedWatchlist) {
        return watchlistRepository.findById(watchlistId)
                .map(existingWatchlist -> {
                    existingWatchlist.setName(updatedWatchlist.getName());
                    existingWatchlist.setDescription(updatedWatchlist.getDescription());
                    return watchlistRepository.save(existingWatchlist);
                })
                .orElseThrow(() -> new RuntimeException("Watchlist not found with id: " + watchlistId));
    }

    /**
     * Delete a watchlist and all its items
     */
    public void deleteWatchlist(Long watchlistId) {
        if (!watchlistRepository.existsById(watchlistId)) {
            throw new RuntimeException("Watchlist not found with id: " + watchlistId);
        }
        
        // Delete all watchlist items first
        List<WatchlistItem> items = watchlistItemRepository.findByWatchlistId(watchlistId);
        watchlistItemRepository.deleteAll(items);
        
        // Then delete the watchlist
        watchlistRepository.deleteById(watchlistId);
    }

    /**
     * Get all items in a watchlist
     */
    public List<WatchlistItem> getWatchlistItems(Long watchlistId) {
        return watchlistItemRepository.findByWatchlistId(watchlistId);
    }

    /**
     * Add a movie to a watchlist
     */
    public WatchlistItem addMovieToWatchlist(Long watchlistId, Long movieId) {
        // Verify watchlist exists
        if (!watchlistRepository.existsById(watchlistId)) {
            throw new RuntimeException("Watchlist not found with id: " + watchlistId);
        }

        // Verify movie exists
        if (!movieRepository.existsById(movieId)) {
            throw new RuntimeException("Movie not found with id: " + movieId);
        }

        // Check if movie is already in the watchlist
        List<WatchlistItem> existingItems = watchlistItemRepository.findByWatchlistId(watchlistId);
        boolean movieAlreadyExists = existingItems.stream()
                .anyMatch(item -> item.getMovieId().equals(movieId));

        if (movieAlreadyExists) {
            throw new RuntimeException("Movie is already in this watchlist");
        }

        // Create and save the watchlist item
        WatchlistItem item = WatchlistItem.builder()
                .watchlistId(watchlistId)
                .movieId(movieId)
                .build();

        return watchlistItemRepository.save(item);
    }

    /**
     * Remove a movie from a watchlist
     */
    public void removeMovieFromWatchlist(Long watchlistId, Long movieId) {
        List<WatchlistItem> items = watchlistItemRepository.findByWatchlistId(watchlistId);
        
        WatchlistItem itemToRemove = items.stream()
                .filter(item -> item.getMovieId().equals(movieId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Movie not found in this watchlist"));

        watchlistItemRepository.delete(itemToRemove);
    }

    /**
     * Check if a user owns a specific watchlist
     */
    public boolean isWatchlistOwnedByUser(Long watchlistId, Long userId) {
        return watchlistRepository.findById(watchlistId)
                .map(watchlist -> watchlist.getUserId().equals(userId))
                .orElse(false);
    }

    /**
     * Get movies in a watchlist with full movie details
     */
    public List<Movie> getWatchlistMovies(Long watchlistId) {
        List<WatchlistItem> items = watchlistItemRepository.findByWatchlistId(watchlistId);
        return items.stream()
                .map(item -> movieRepository.findById(item.getMovieId()))
                .filter(Optional::isPresent) //what does this line do? It filters out any Optional objects that are empty, ensuring only present values are processed.
                .map(Optional::get) // Retrieves the Movie object from the Optional
                .toList();
    }

}
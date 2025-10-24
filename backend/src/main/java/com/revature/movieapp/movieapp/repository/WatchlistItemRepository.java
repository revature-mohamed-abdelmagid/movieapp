package com.revature.movieapp.movieapp.repository;

import com.revature.movieapp.movieapp.model.WatchlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchlistItemRepository extends JpaRepository<WatchlistItem, Long> {

    List<WatchlistItem> findByWatchlistId(Long watchlistId);

    List<WatchlistItem> findByMovieId(Long movieId);

}

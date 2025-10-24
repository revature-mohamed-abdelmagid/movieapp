package com.revature.movieapp.movieapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "watchlist_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "watchlist_item_id")
    private Long watchlistItemId;

    @Column(name = "watchlist_id", nullable = false)
    private Long watchlistId;

    @Column(name = "movie_id", nullable = false)
    private Long movieId;

    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt;

    @PrePersist
    protected void onAdd() {
        this.addedAt = LocalDateTime.now();
    }

}

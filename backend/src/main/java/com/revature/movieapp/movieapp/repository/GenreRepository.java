package com.revature.movieapp.movieapp.repository;

import com.revature.movieapp.movieapp.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    
    Optional<Genre> findByGenreName(String genreName);
}
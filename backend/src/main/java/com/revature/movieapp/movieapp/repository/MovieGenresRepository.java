package com.revature.movieapp.movieapp.repository;

import com.revature.movieapp.movieapp.model.MovieGenres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieGenresRepository extends JpaRepository<MovieGenres, MovieGenres.MovieGenresId> {
    
    @Query("SELECT mg FROM MovieGenres mg WHERE mg.id.movieId = :movieId")
    List<MovieGenres> findByMovieId(@Param("movieId") Long movieId);
    
    @Query("SELECT mg FROM MovieGenres mg WHERE mg.id.genreId = :genreId")
    List<MovieGenres> findByGenreId(@Param("genreId") Long genreId);
}
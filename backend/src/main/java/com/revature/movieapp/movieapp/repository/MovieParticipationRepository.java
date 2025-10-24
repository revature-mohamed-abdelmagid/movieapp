package com.revature.movieapp.movieapp.repository;

import com.revature.movieapp.movieapp.model.MovieParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieParticipationRepository extends JpaRepository<MovieParticipation, Long> {

    List<MovieParticipation> findByMovieId(Long movieId);

    List<MovieParticipation> findByPersonId(Long personId);

}

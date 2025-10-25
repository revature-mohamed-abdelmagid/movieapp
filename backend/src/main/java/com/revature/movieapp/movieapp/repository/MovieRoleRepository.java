package com.revature.movieapp.movieapp.repository;

import com.revature.movieapp.movieapp.model.MovieRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRoleRepository extends JpaRepository<MovieRole, Long> {

    Optional<MovieRole> findByName(String name);

}

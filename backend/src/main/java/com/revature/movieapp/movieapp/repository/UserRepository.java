package com.revature.movieapp.movieapp.repository;

import com.revature.movieapp.movieapp.model.User;

public interface UserRepository extends org.springframework.data.jpa.repository.JpaRepository<User, Long> {
    // Custom query method to find a user by email
    java.util.Optional<User> findByEmail(String email);
}

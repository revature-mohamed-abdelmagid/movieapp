package com.revature.movieapp.movieapp.controller;

import com.revature.movieapp.movieapp.model.MovieRole;
import com.revature.movieapp.movieapp.repository.MovieRoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class MovieRoleController {

    private final MovieRoleRepository roleRepository;

    public MovieRoleController(MovieRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Get all available roles (Actor, Director, Producer, etc.)
     */
    @GetMapping
    public ResponseEntity<List<MovieRole>> getAllRoles() {
        List<MovieRole> roles = roleRepository.findAll();
        return ResponseEntity.ok(roles);
    }
}


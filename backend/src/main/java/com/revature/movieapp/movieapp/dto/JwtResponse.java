package com.revature.movieapp.movieapp.dto;

import com.revature.movieapp.movieapp.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * DTO for JWT login response
 * Contains the JWT token and user information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    private Set<Role> roles;
    
    // Constructor without type (defaults to "Bearer")
    public JwtResponse(String token, String username, String email, Set<Role> roles) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}


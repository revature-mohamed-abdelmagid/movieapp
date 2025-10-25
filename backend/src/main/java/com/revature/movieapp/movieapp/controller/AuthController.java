package com.revature.movieapp.movieapp.controller;

import com.revature.movieapp.movieapp.dto.JwtResponse;
import com.revature.movieapp.movieapp.dto.LoginRequest;
import com.revature.movieapp.movieapp.dto.MessageResponse;
import com.revature.movieapp.movieapp.model.User;
import com.revature.movieapp.movieapp.security.JwtUtil;
import com.revature.movieapp.movieapp.service.TokenBlacklistService;
import com.revature.movieapp.movieapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Authentication Controller - JWT Based
 * Handles user registration, login, and logout
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthController(UserService userService, 
                         AuthenticationManager authenticationManager,
                         JwtUtil jwtUtil,
                         TokenBlacklistService tokenBlacklistService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    /**
     * Register a new user - PUBLIC ENDPOINT
     * 
     * Example request body:
     * {
     *   "username": "john_doe",
     *   "email": "john@example.com",
     *   "password": "mypassword123"
     * }
     * 
     * @param user the user to register
     * @return response with success message and user info (without password)
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        try {
            // Check if username already exists
            if (userService.getUserByUsername(user.getUsername()).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Username already exists"));
            }

            // Create the user (password will be automatically encoded in UserService)
            User createdUser = userService.createUser(user);

            // Return success response (without exposing the password)
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("userId", createdUser.getId());
            response.put("username", createdUser.getUsername());
            response.put("email", createdUser.getEmail());
            response.put("roles", createdUser.getRoles());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error during registration: " + e.getMessage()));
        }
    }

    /**
     * Login endpoint - PUBLIC ENDPOINT
     * Authenticates user and returns JWT token
     * 
     * Example request body:
     * {
     *   "username": "john_doe",
     *   "password": "mypassword123"
     * }
     * 
     * @param loginRequest username and password
     * @return JWT token and user information
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate user with username and password
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            // Get user details
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // Generate JWT token
            String jwt = jwtUtil.generateToken(userDetails);
            
            // Get full user information
            User user = userService.getUserByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Return JWT token and user info
            JwtResponse response = new JwtResponse(
                jwt,
                user.getUsername(),
                user.getEmail(),
                user.getRoles()
            );

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Invalid username or password"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error during login: " + e.getMessage()));
        }
    }

    /**
     * Logout endpoint - PUBLIC ENDPOINT
     * Invalidates the JWT token by adding it to blacklist
     * 
     * Send token in Authorization header: Bearer <token>
     * 
     * @param authHeader Authorization header containing JWT token
     * @return success message
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            // Extract token from "Bearer <token>"
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                
                // Add token to blacklist
                tokenBlacklistService.blacklistToken(token);
                
                return ResponseEntity.ok(new MessageResponse("Logged out successfully"));
            } else {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Invalid Authorization header"));
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error during logout: " + e.getMessage()));
        }
    }
}


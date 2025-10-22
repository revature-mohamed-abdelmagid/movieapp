package com.revature.movieapp.movieapp.controller;

import com.revature.movieapp.movieapp.model.User;
import com.revature.movieapp.movieapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Authentication Controller
 * Handles user registration
 * Login is handled automatically by Spring Security via HTTP Basic Auth
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register a new user
     * This endpoint is PUBLIC (configured in SecurityConfig)
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
                        .body(createErrorResponse("Username already exists"));
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
                    .body(createErrorResponse("Error during registration: " + e.getMessage()));
        }
    }

    /**
     * Login endpoint info
     * This is just an informational endpoint
     * Actual login is handled by Spring Security HTTP Basic Auth
     * 
     * To login, clients should send requests with Authorization header:
     * Authorization: Basic <base64(username:password)>
     */
    @GetMapping("/login-info")
    public ResponseEntity<?> loginInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("message", "Use HTTP Basic Authentication");
        info.put("header", "Authorization: Basic <base64(username:password)>");
        info.put("example", "For testing in Postman: Go to Authorization tab, select 'Basic Auth', enter username and password");
        return ResponseEntity.ok(info);
    }

    /**
     * Helper method to create error response
     */
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}


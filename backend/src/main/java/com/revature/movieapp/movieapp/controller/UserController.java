package com.revature.movieapp.movieapp.controller;

import com.revature.movieapp.movieapp.model.User;
import com.revature.movieapp.movieapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;




/**
 * User Management Controller - ADMIN ONLY
 * 
 * This controller is for administrative user management.
 * All endpoints require ADMIN role (configured in SecurityConfig).
 * 
 * For public user registration, use AuthController (/api/auth/register)
 * 
 * Use Cases:
 * - View all users (admin dashboard)
 * - View specific user details
 * - Update user information (support/moderation)
 * - Delete problematic users (moderation)
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get all users - ADMIN ONLY
     * Useful for admin dashboard or user management interface
     * 
     * @return List of all users in the system
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Get user by ID - ADMIN ONLY
     * Useful for viewing specific user details
     * 
     * @param id the user ID
     * @return User details if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Update user - ADMIN ONLY
     * Useful for:
     * - User support (fixing user data)
     * - Adding/removing roles
     * - Updating user information
     * 
     * NOTE: Password will be re-encrypted if changed
     * 
     * @param id the user ID to update
     * @param user the updated user data
     * @return Updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        return userService.getUserById(id)
                .map(existing -> {
                    User updated = userService.updateUser(id, user);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete user - ADMIN ONLY
     * Useful for:
     * - Removing spam accounts
     * - User moderation
     * - GDPR user deletion requests
     * 
     * @param id the user ID to delete
     * @return 204 No Content if successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(existing -> {
                    userService.deleteUser(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
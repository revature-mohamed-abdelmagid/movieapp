package com.revature.movieapp.movieapp.service;

import com.revature.movieapp.movieapp.model.Role;
import com.revature.movieapp.movieapp.model.User;
import com.revature.movieapp.movieapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service layer for User operations
 * Handles business logic and password encryption
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create a new user with encrypted password
     * Automatically assigns ROLE_USER if no roles are specified
     * @param user the user to create
     * @return the created user
     */
    public User createUser(User user) {
        // Encrypt the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // If no roles are set, assign ROLE_USER by default
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Set<Role> defaultRoles = new HashSet<>();
            defaultRoles.add(Role.ROLE_USER);
            user.setRoles(defaultRoles);
        }
        
        // Set creation timestamp
        if (user.getCreatedat() == null) {
            user.setCreatedat(System.currentTimeMillis());
        }
        
        return userRepository.save(user);
    }

    /**
     * Get user by ID
     * @param id the user ID
     * @return Optional containing the user if found
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Get user by username
     * @param username the username
     * @return Optional containing the user if found
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Get all users
     * @return list of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Update an existing user
     * If password is changed, it will be encrypted
     * @param id the user ID
     * @param updatedUser the updated user data
     * @return the updated user
     */
    public User updateUser(Long id, User updatedUser) {
        updatedUser.setId(id);
        
        // Check if password was changed and needs to be re-encoded
        // If the password doesn't start with $2a$ (BCrypt prefix), it needs encoding
        if (updatedUser.getPassword() != null && 
            !updatedUser.getPassword().startsWith("$2a$")) {
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        
        return userRepository.save(updatedUser);
    }

    /**
     * Delete a user by ID
     * @param id the user ID
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}



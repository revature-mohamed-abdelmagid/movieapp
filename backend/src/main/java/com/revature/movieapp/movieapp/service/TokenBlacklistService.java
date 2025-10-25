package com.revature.movieapp.movieapp.service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Token Blacklist Service
 * Stores invalidated JWT tokens (used for logout)
 * 
 * Simple in-memory implementation
 * For production, consider using Redis for distributed systems
 */
@Service
public class TokenBlacklistService {

    // Thread-safe set to store blacklisted tokens
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    /**
     * Add token to blacklist
     * Called when user logs out
     * @param token JWT token to blacklist
     */
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    /**
     * Check if token is blacklisted
     * Called during authentication to verify token is still valid
     * @param token JWT token to check
     * @return true if blacklisted, false otherwise
     */
    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    /**
     * Remove token from blacklist
     * Useful for cleanup or testing
     * @param token JWT token to remove
     */
    public void removeFromBlacklist(String token) {
        blacklistedTokens.remove(token);
    }

    /**
     * Clear all blacklisted tokens
     * Useful for testing or manual cleanup
     */
    public void clearBlacklist() {
        blacklistedTokens.clear();
    }

    /**
     * Get count of blacklisted tokens
     * @return number of blacklisted tokens
     */
    public int getBlacklistSize() {
        return blacklistedTokens.size();
    }
}


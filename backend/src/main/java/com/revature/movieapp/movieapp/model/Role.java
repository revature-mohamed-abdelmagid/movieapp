package com.revature.movieapp.movieapp.model;

/**
 * User Roles for Authorization
 * These roles are used by Spring Security to control access to endpoints
 * 
 * NOTE: Spring Security automatically strips the "ROLE_" prefix when using hasRole()
 * So hasRole("ADMIN") matches ROLE_ADMIN
 */
public enum Role {
    ROLE_USER,          // Regular user - can create/update movies
    ROLE_ADMIN,         // Administrator - can delete movies and manage users
    ROLE_MODERATOR,     // Moderator - can be used for content moderation
    ROLE_PREMIUM_USER   // Premium user - can be used for premium features
}

package com.revature.movieapp.movieapp.model;

/**
 * User Roles for Authorization
 * These roles are used by Spring Security to control access to endpoints
 *
 * NOTE: Spring Security automatically strips the "ROLE_" prefix when using hasRole()
 * So hasRole("ADMIN") matches ROLE_ADMIN
 */
public enum Role {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_MODERATOR,
    ROLE_PREMIUM_USER
}

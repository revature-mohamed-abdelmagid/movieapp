package com.revature.movieapp.movieapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security Configuration - JWT Based
 * This class configures JWT authentication and authorization
 * 
 * Key changes from Basic Auth:
 * - Uses JWT tokens instead of username:password
 * - Stateless sessions (no server-side session storage)
 * - JWT filter validates token on each request
 */
@Configuration
@EnableWebSecurity // Enables Spring Security's web security support
@EnableMethodSecurity // Enables @PreAuthorize, @Secured annotations on methods
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Password Encoder Bean
     * BCrypt is a strong hashing algorithm for passwords
     * It automatically handles salt generation and is resistant to rainbow table attacks
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication Manager Bean
     * This is used to authenticate users during login
     * Used in AuthController to validate username/password
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Security Filter Chain - Main Security Configuration
     * This defines which endpoints are public vs protected
     * and configures JWT authentication
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF (not needed for JWT - tokens in headers, not cookies)
            .csrf(csrf -> csrf.disable())
            
            // Configure session management - STATELESS for JWT
            // Server doesn't maintain session state - everything in JWT token
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Configure authorization rules
            .authorizeHttpRequests(auth -> auth
                // PUBLIC ENDPOINTS - Anyone can access these without authentication
                .requestMatchers("/api/auth/**").permitAll() // Registration, login, logout
                .requestMatchers(HttpMethod.GET, "/movies/**").permitAll() // Anyone can view movies
                
                // ADMIN ONLY ENDPOINTS - Only users with ROLE_ADMIN can access
                .requestMatchers(HttpMethod.DELETE, "/movies/**").hasRole("ADMIN")
                .requestMatchers("/api/users/**").hasRole("ADMIN") // Only admins can manage users
                
                // AUTHENTICATED ENDPOINTS - Any logged-in user can access
                .requestMatchers(HttpMethod.POST, "/movies/**").authenticated() // Create movies
                .requestMatchers(HttpMethod.PUT, "/movies/**").authenticated() // Update movies
                
                // ALL OTHER ENDPOINTS - Require authentication
                .anyRequest().authenticated()
            )
            
            // Add JWT filter before Spring Security's authentication filter
            // This intercepts requests and validates JWT tokens
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}


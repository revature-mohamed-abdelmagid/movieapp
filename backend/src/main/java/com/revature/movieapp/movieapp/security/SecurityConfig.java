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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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
     * CORS Configuration Bean
     * Allows frontend to make requests to backend from different origins
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:5173", "http://cineverse-frontend-bucket.s3-website.us-east-2.amazonaws.com")); // React dev server ports
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Security Filter Chain - Main Security Configuration
     * This defines which endpoints are public vs protected
     * and configures JWT authentication
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Enable CORS with our custom configuration
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
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
                .requestMatchers(HttpMethod.GET, "/api/persons/**").permitAll() // Anyone can view persons
                .requestMatchers(HttpMethod.GET, "/api/roles/**").permitAll() // Anyone can view roles
                
                // ADMIN ONLY ENDPOINTS - Only users with ROLE_ADMIN can access
                .requestMatchers(HttpMethod.POST, "/movies/**").hasRole("ADMIN") // Create movies - admin only
                .requestMatchers(HttpMethod.PUT, "/movies/**").hasRole("ADMIN") // Update movies - admin only
                .requestMatchers(HttpMethod.DELETE, "/movies/**").hasRole("ADMIN") // Delete movies - admin only
                .requestMatchers(HttpMethod.POST, "/api/persons/**").hasRole("ADMIN") // Create persons - admin only
                .requestMatchers(HttpMethod.PUT, "/api/persons/**").hasRole("ADMIN") // Update persons - admin only
                .requestMatchers(HttpMethod.DELETE, "/api/persons/**").hasRole("ADMIN") // Delete persons - admin only
                .requestMatchers("/api/users/**").hasRole("ADMIN") // Only admins can manage users
                
                // Reviews endpoints permit all for viewing, authenticated for creating/updating/deleting
                .requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll() // View reviews
                .requestMatchers(HttpMethod.POST, "/api/reviews/**").authenticated() // Create reviews
                .requestMatchers(HttpMethod.PUT, "/api/reviews/**").authenticated() // Update reviews
                .requestMatchers(HttpMethod.DELETE, "/api/reviews/**").authenticated() // Delete reviews

                // ALL OTHER ENDPOINTS - Require authentication
                .anyRequest().authenticated()
            )
            
            // Add JWT filter before Spring Security's authentication filter
            // This intercepts requests and validates JWT tokens
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}


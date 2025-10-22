package com.revature.movieapp.movieapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security Configuration
 * This class configures authentication and authorization for the application
 * 
 * Note: CustomUserDetailsService is automatically used by Spring Security
 * because it's annotated with @Service and implements UserDetailsService
 */
@Configuration
@EnableWebSecurity // Enables Spring Security's web security support
@EnableMethodSecurity // Enables @PreAuthorize, @Secured annotations on methods
public class SecurityConfig {

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
     * This is used to authenticate users
     * Spring Security uses this during login
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Security Filter Chain - Main Security Configuration
     * This defines which endpoints are public vs protected
     * and what authentication method to use
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for simplicity (you'd enable this in production with proper frontend)
            .csrf(csrf -> csrf.disable())
            
            // Configure authorization rules
            .authorizeHttpRequests(auth -> auth
                // PUBLIC ENDPOINTS - Anyone can access these without authentication
                .requestMatchers("/api/auth/**").permitAll() // Registration and login
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
            
            // Use HTTP Basic Authentication (simple - username:password in header)
            // For production, you'd want to use JWT or OAuth2
            .httpBasic(basic -> {});

        return http.build();
    }
}


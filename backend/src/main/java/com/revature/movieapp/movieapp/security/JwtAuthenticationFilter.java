package com.revature.movieapp.movieapp.security;

import com.revature.movieapp.movieapp.service.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter
 * Intercepts every HTTP request to validate JWT token
 * Executes once per request
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    /**
     * Filter logic executed for each request
     * 1. Extract JWT token from Authorization header
     * 2. Validate token
     * 3. Load user details
     * 4. Set authentication in SecurityContext
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        // Extract Authorization header
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Check if header contains Bearer token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extract token (remove "Bearer " prefix)
            jwt = authorizationHeader.substring(7);
            
            try {
                // Extract username from token
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                // Token is invalid or expired - log and continue
                logger.warn("JWT Token extraction failed: " + e.getMessage());
            }
        }

        // If we have a username and user is not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Check if token is blacklisted (user logged out)
            if (tokenBlacklistService.isBlacklisted(jwt)) {
                logger.warn("Token is blacklisted (user logged out)");
                filterChain.doFilter(request, response);
                return;
            }

            // Load user details from database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Validate token
            if (jwtUtil.validateToken(jwt, userDetails)) {
                // Create authentication object
                UsernamePasswordAuthenticationToken authenticationToken = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails, 
                        null, 
                        userDetails.getAuthorities()
                    );
                
                // Set authentication details
                authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                
                // Set authentication in SecurityContext
                // Now Spring Security knows user is authenticated
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                
                logger.debug("JWT Token validated successfully for user: " + username);
            }
        }

        // Continue filter chain
        filterChain.doFilter(request, response);
    }
}


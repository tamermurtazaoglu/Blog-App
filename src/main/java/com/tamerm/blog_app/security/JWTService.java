package com.tamerm.blog_app.security;

import com.tamerm.blog_app.model.User;

/**
 * Service for generating and validating JWT tokens.
 */
public interface JWTService {
    String generateToken(User user);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
    boolean hasActiveToken();
}
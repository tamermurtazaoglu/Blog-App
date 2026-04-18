package com.tamerm.blog_service.security;

public interface JWTService {
    String extractUsername(String token);
    Long extractUserId(String token);
    boolean isTokenValid(String token);
}

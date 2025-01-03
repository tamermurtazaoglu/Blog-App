package com.tamerm.blog_app.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link GlobalExceptionHandler}.
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
    }

    /**
     * Tests handling of ResourceNotFoundException.
     */
    @Test
    void handleResourceNotFoundException_ShouldReturnNotFoundResponse() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");
        when(webRequest.getDescription(false)).thenReturn("uri=/test");

        ResponseEntity<?> response = globalExceptionHandler.handleResourceNotFoundException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> expectedBody = new HashMap<>();
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        expectedBody.put("timestamp", responseBody.get("timestamp"));
        expectedBody.put("message", "Resource not found");
        expectedBody.put("details", "uri=/test");
        assertEquals(expectedBody, response.getBody());
    }

    /**
     * Tests handling of BadRequestException.
     */
    @Test
    void handleBadRequestException_ShouldReturnBadRequestResponse() {
        BadRequestException ex = new BadRequestException("Bad request");
        when(webRequest.getDescription(false)).thenReturn("uri=/test");

        ResponseEntity<?> response = globalExceptionHandler.handleBadRequestException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> expectedBody = new HashMap<>();
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        expectedBody.put("timestamp", responseBody.get("timestamp"));
        expectedBody.put("message", "Bad request");
        expectedBody.put("details", "uri=/test");
        assertEquals(expectedBody, response.getBody());
    }

    /**
     * Tests handling of generic Exception.
     */
    @Test
    void handleGlobalException_ShouldReturnInternalServerErrorResponse() {
        Exception ex = new Exception("Internal server error");
        when(webRequest.getDescription(false)).thenReturn("uri=/test");

        ResponseEntity<?> response = globalExceptionHandler.handleGlobalException(ex, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, Object> expectedBody = new HashMap<>();
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        expectedBody.put("timestamp", responseBody.get("timestamp"));
        expectedBody.put("message", "An unexpected error occurred");
        expectedBody.put("details", "uri=/test");
        assertEquals(expectedBody, response.getBody());
    }
}
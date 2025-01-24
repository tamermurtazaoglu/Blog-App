package com.tamerm.blog_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when the provided credentials are invalid.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidCredentialsException extends RuntimeException {

    /**
     * Constructs a new InvalidCredentialsException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidCredentialsException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidCredentialsException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
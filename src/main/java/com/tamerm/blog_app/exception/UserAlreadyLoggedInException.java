package com.tamerm.blog_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user is already logged in with an active token.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserAlreadyLoggedInException extends RuntimeException {

    /**
     * Constructs a new UserAlreadyLoggedInException with the specified detail message.
     *
     * @param message the detail message
     */
    public UserAlreadyLoggedInException(String message) {
        super(message);
    }

    /**
     * Constructs a new UserAlreadyLoggedInException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public UserAlreadyLoggedInException(String message, Throwable cause) {
        super(message, cause);
    }
}
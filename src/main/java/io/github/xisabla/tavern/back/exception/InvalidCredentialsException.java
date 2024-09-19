package io.github.xisabla.tavern.back.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when the credentials provided are invalid.
 */
public class InvalidCredentialsException extends APIException {
    public InvalidCredentialsException() {
        super(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }
}

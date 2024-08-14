package io.github.xisabla.back.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when the credentials are invalid.
 */
public class InvalidCredentialsException extends APIException {
    public InvalidCredentialsException() {
        super(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }
}

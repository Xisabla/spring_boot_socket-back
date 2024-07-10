package io.github.xisabla.back.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * Generic exception for API errors.
 */
@Getter
public class APIException extends RuntimeException {
    private final HttpStatus status;
    private final String details;

    public APIException(HttpStatus status, String message) {
        super(message);

        this.status = status;
        this.details = null;
    }

    public APIException(HttpStatus status, String message, String details) {
        super(message);

        this.status = status;
        this.details = details;
    }
}

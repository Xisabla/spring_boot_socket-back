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

    public APIException(final HttpStatus status, final String message) {
        super(message);

        this.status = status;
        this.details = null;
    }

    public APIException(final HttpStatus status, final String message, final String details) {
        super(message);

        this.status = status;
        this.details = details;
    }
}

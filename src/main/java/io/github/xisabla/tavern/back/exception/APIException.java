package io.github.xisabla.tavern.back.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Generic exception for API errors.
 */
@Getter
public class APIException extends RuntimeException {
    private final HttpStatus status;
    private final String details;

    public APIException(final String message) {
        super(message);

        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.details = null;
    }

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

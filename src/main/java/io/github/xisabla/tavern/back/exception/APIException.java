package io.github.xisabla.tavern.back.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Generic exception for API errors.
 */
@Getter
public class APIException extends RuntimeException {
    /**
     * HTTP status code to return when the exception is thrown.
     */
    private final HttpStatus status;

    /**
     * Additional details about the exception.
     */
    private final String details;

    /**
     * Constructor for APIException. Uses the default HTTP status code (500).
     *
     * @param message Exception message.
     */
    public APIException(final String message) {
        super(message);

        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.details = null;
    }

    /**
     * Constructor for APIException.
     *
     * @param status  HTTP status code to return when the exception is thrown.
     * @param message Exception message.
     */
    public APIException(final HttpStatus status, final String message) {
        super(message);

        this.status = status;
        this.details = null;
    }

    /**
     * Constructor for APIException.
     *
     * @param status  HTTP status code to return when the exception is thrown.
     * @param message Exception message.
     * @param details Additional details about the exception.
     */
    public APIException(final HttpStatus status, final String message, final String details) {
        super(message);

        this.status = status;
        this.details = details;
    }
}

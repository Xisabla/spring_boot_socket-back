package io.github.xisabla.tavern.back.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.xisabla.tavern.back.exception.APIException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Objects used to return a JSON response with an error message.
 */
@Data
public class ErrorResponse {
    /**
     * Timestamp of the error.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private final LocalDateTime timestamp;

    // Error fields

    /**
     * Status code of the response.
     */
    private final int status;

    /**
     * Name of the error to display.
     */
    private final String error;
    /**
     * Message of the error to display.
     */
    private final String message;
    /**
     * Any additional details about the error.
     */
    private final String details;

    // Request fields

    /**
     * Method of the request that caused the error.
     */
    private String method = null;
    /**
     * Path of the request that caused the error.
     */
    private String path = null;
    /**
     * Request ID of the request that caused the error.
     */
    private String requestId;

    /**
     * Create an error response with the given parameters.
     *
     * @param status  Status code of the response.
     * @param error   Name of the error to display.
     * @param message Message of the error to display.
     * @param details Any additional details about the error.
     */
    public ErrorResponse(final int status, final String error, final String message, final String details) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details;
    }

    /**
     * Create an error response from an API exception.
     *
     * @param apiException The API exception to create the response from.
     */
    public ErrorResponse(final APIException apiException) {
        this(
            apiException.getStatus().value(),
            apiException.getClass().getSimpleName(),
            apiException.getMessage(),
            apiException.getDetails());
    }

    /**
     * Create an error response from a generic exception.
     *
     * @param exception The exception to create the response from.
     */
    public ErrorResponse(final Exception exception) {
        this(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            exception.getClass().getSimpleName(),
            exception.getMessage(),
            exception.toString());
    }

    /**
     * Add request information to the error response.
     *
     * @param request Request to get information from.
     *
     * @return The instance of the error response.
     */
    public ErrorResponse withRequest(final HttpServletRequest request) {
        this.requestId = request.getRequestId();
        this.path = request.getRequestURI();
        this.method = request.getMethod();

        return this;
    }
}

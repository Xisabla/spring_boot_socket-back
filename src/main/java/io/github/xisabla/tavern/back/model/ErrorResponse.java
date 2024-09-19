package io.github.xisabla.tavern.back.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.xisabla.tavern.back.exception.APIException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Objects used to return a JSON response with an error message.
 */
@Data
public class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private final LocalDateTime timestamp;

    // Error fields

    private final int status;
    private final String error;
    private final String message;
    private final String details;

    // Request fields

    private String method = null;
    private String path = null;
    private String requestId;

    /**
     * Create an error response with the given parameters.
     *
     * @param status  Status code of the response.
     * @param error   Name of the error to display.
     * @param message Message of the error to display.
     * @param details Any additional details about the error.
     */
    public ErrorResponse(int status, String error, String message, String details) {
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
    public ErrorResponse(APIException apiException) {
        this(
            apiException.getStatus().value(),
            apiException.getClass().getSimpleName(),
            apiException.getMessage(),
            apiException.getDetails()
        );
    }

    /**
     * Create an error response from a generic exception.
     *
     * @param exception The exception to create the response from.
     */
    public ErrorResponse(Exception exception) {
        this(
            500,
            exception.getClass().getSimpleName(),
            exception.getMessage(),
            exception.toString()
        );
    }

    /**
     * Add request information to the error response.
     *
     * @param request Request to get information from.
     * @return The instance of the error response.
     */
    public ErrorResponse withRequest(HttpServletRequest request) {
        this.requestId = request.getRequestId();
        this.path = request.getRequestURI();
        this.method = request.getMethod();

        return this;
    }
}

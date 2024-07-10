package io.github.xisabla.back.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.github.xisabla.back.exception.APIException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

/**
 * Responses sent by the API for errors.
 */
@Data
public class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String details;
    private final String message;
    private String method = null;
    private String path = null;
    private String requestId = null;

    public ErrorResponse(int status, String error, String message, String details) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details;
    }

    public ErrorResponse(APIException exception) {
        this.timestamp = LocalDateTime.now();
        this.status = exception.getStatus().value();
        this.error = exception.getClass().getSimpleName();
        this.message = exception.getMessage();
        this.details = exception.getDetails();
    }

    public ErrorResponse(Exception exception) {
        this.timestamp = LocalDateTime.now();
        this.status = 500;
        this.error = exception.getClass().getSimpleName();
        this.message = exception.getMessage();
        this.details = exception.toString();
    }

    public ErrorResponse withRequest(HttpServletRequest request) {
        this.requestId = request.getRequestId();
        this.path = request.getRequestURI();
        this.method = request.getMethod();

        return this;
    }
}

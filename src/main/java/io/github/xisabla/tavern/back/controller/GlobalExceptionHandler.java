package io.github.xisabla.tavern.back.controller;

import io.github.xisabla.tavern.back.exception.APIException;
import io.github.xisabla.tavern.back.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for the application.
 * Returns a response with the appropriate status code and message when an exception is thrown.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Handle APIExceptions.
     *
     * @param apiException The exception to handle.
     * @param request      The request that caused the exception.
     * @return The response entity with the error response.
     */
    @ExceptionHandler({APIException.class})
    public ResponseEntity<ErrorResponse> handleException(final APIException apiException, @NonNull final HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(apiException).withRequest(request);

        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    /**
     * Handle any exception that is not an APIException.
     *
     * @param exception The exception to handle.
     * @param request   The request that caused the exception.
     * @return The response entity with the error response.
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleException(final Exception exception, @NonNull final HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(exception).withRequest(request);

        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }
}

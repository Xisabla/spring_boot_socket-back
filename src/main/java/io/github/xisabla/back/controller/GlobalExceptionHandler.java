package io.github.xisabla.back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.github.xisabla.back.exception.APIException;
import io.github.xisabla.back.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Handle exceptions and return a proper response.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ APIException.class })
    public ResponseEntity<ErrorResponse> handleException(APIException e, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(e).withRequest(request);

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(e).withRequest(request);

        return ResponseEntity.status(error.getStatus()).body(error);
    }

}

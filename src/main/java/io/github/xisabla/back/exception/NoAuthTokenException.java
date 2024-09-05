package io.github.xisabla.back.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when no auth token is provided in the request.
 */
public class NoAuthTokenException extends APIException {
    public NoAuthTokenException() {
        super(HttpStatus.UNAUTHORIZED, "No auth token provided");
    }
}

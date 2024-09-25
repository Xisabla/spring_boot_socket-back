package io.github.xisabla.tavern.back.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a method or an endpoint is not implemented yet.
 */
public class NotImplementedException extends APIException {
    /**
     * Constructor.
     */
    public NotImplementedException() {
        super(HttpStatus.NOT_IMPLEMENTED, "Not implemented yet");
    }
}

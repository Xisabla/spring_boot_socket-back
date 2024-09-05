package io.github.xisabla.back.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a message is not found.
 */
public class MessageNotFoundException extends APIException {
    public MessageNotFoundException(final UUID id) {
        super(HttpStatus.NOT_FOUND, "Message not found with id " + id);
    }
}

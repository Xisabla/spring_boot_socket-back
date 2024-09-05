package io.github.xisabla.back.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a channel is not found.
 */
public class ChannelAlreadyExistsException extends APIException {
    public ChannelAlreadyExistsException(final UUID id) {
        super(HttpStatus.CONFLICT, "Channel with name \"" + id + "\" already exists");
    }

    public ChannelAlreadyExistsException(final String name) {
        super(HttpStatus.CONFLICT, "Channel with name \"" + name + "\" already exists");
    }
}

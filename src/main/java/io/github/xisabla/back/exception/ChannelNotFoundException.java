package io.github.xisabla.back.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a channel is not found.
 */
public class ChannelNotFoundException extends APIException {
    public ChannelNotFoundException(final UUID id) {
        super(HttpStatus.NOT_FOUND, "Channel not found with id " + id);
    }

    public ChannelNotFoundException(final String name) {
        super(HttpStatus.NOT_FOUND, "Channel not found with name " + name);
    }
}

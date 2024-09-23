package io.github.xisabla.tavern.back.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

/**
 * Exception thrown when trying to find a user that does not exist.
 */
public class ChannelNotFoundException extends APIException {
    public ChannelNotFoundException(final UUID id) {
        super(HttpStatus.NOT_FOUND, "Channel not found", "Channel with id " + id + " not found");
    }

    public ChannelNotFoundException(final String name) {
        super(HttpStatus.NOT_FOUND, "Channel not found", "Channel with name " + name + " not found");
    }
}

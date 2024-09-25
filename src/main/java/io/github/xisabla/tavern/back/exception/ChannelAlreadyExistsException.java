package io.github.xisabla.tavern.back.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when trying to create a Channel that already exists.
 */
public class ChannelAlreadyExistsException extends APIException {
    /**
     * @param name Name of the channel that already exists.
     */
    public ChannelAlreadyExistsException(final String name) {
        super(HttpStatus.CONFLICT, "Channel already exists", "Channel with name " + name + " already exists");
    }
}

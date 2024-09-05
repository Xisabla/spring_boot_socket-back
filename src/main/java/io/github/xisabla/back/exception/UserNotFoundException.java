package io.github.xisabla.back.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user is not found.
 */
public class UserNotFoundException extends APIException {
    public UserNotFoundException(final UUID id) {
        super(HttpStatus.NOT_FOUND, "User with id \"" + id + "\" not found");
    }

    public UserNotFoundException(final String username) {
        super(HttpStatus.NOT_FOUND, "User with username \"" + username + "\" not found");
    }
}

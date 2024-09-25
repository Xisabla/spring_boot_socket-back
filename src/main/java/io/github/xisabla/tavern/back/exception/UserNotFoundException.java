package io.github.xisabla.tavern.back.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

/**
 * Exception thrown when trying to find a user that does not exist.
 */
public class UserNotFoundException extends APIException {
    /**
     * @param id ID of the user that was not found.
     */
    public UserNotFoundException(final UUID id) {
        super(HttpStatus.NOT_FOUND, "User not found", "User with id " + id + " not found");
    }

    /**
     * @param username Username of the user that was not found.
     */
    public UserNotFoundException(final String username) {
        super(HttpStatus.NOT_FOUND, "User not found", "User with username " + username + " not found");
    }
}

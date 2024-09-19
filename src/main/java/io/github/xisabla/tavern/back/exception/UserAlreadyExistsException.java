package io.github.xisabla.tavern.back.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when trying to create a user that already exists.
 */
public class UserAlreadyExistsException extends APIException {
    public UserAlreadyExistsException(final String login) {
        super(HttpStatus.CONFLICT, "User already exists", "User with login " + login + " already exists");
    }
}

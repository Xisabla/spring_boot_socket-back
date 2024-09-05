package io.github.xisabla.back.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user is not found.
 */
public class UserAlreadyExistsException extends APIException {
    public UserAlreadyExistsException(final String login) {
        // NOTE: `login` represents either a username or an email
        super(HttpStatus.CONFLICT, "User with login \"" + login + "\" already exists");
    }
}

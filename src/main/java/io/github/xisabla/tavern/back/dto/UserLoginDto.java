package io.github.xisabla.tavern.back.dto;

import io.github.xisabla.tavern.back.model.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for user login.
 */
@Data
public class UserLoginDto {
    /**
     * User login. Can either be an email or a username.
     */
    @NotNull
    @Size(min = User.USERNAME_MIN_LENGTH, max = User.EMAIL_MAX_LENGTH)
    private String login;

    /**
     * Password of the user to login in plain text. Will be hashed before comparing.
     */
    @NotNull
    @Size(min = User.PASSWORD_MIN_LENGTH, max = User.PASSWORD_MAX_LENGTH)
    private String password;

    /**
     * Whether to remember the user.
     */
    private boolean remember = false;
}

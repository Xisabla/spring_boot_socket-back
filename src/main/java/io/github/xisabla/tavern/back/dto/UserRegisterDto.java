package io.github.xisabla.tavern.back.dto;

import io.github.xisabla.tavern.back.model.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for user registration.
 */
@Data
public class UserRegisterDto {
    /**
     * Username of the user to register.
     */
    @NotNull
    @Size(min = User.USERNAME_MIN_LENGTH, max = User.USERNAME_MAX_LENGTH)
    private String username;

    /**
     * Email of the user to register.
     */
    @NotNull
    @Size(min = User.EMAIL_MIN_LENGTH, max = User.EMAIL_MAX_LENGTH)
    private String email;

    /**
     * Password of the user to register. Must be in plain text, will be hashed before storing.
     */
    @NotNull
    @Size(min = User.PASSWORD_MIN_LENGTH, max = User.PASSWORD_MAX_LENGTH)
    private String password;
}

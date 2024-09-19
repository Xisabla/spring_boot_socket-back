package io.github.xisabla.tavern.back.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for user login.
 */
@Data
public class UserLoginDto {
    /**
     * User login.
     * Can either be an email or a username.
     */
    @NotNull
    @Size(min = 3, max = 128)
    private String login;

    @NotNull
    @Size(min = 8, max = 128)
    private String password;

    private boolean remember = false;
}

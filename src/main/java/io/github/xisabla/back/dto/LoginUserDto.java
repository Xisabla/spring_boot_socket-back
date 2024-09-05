package io.github.xisabla.back.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for login requests.
 */
@Data
public class LoginUserDto {
    @NotNull
    @Size(min = 3, max = 128)
    // Note: Is either a username or an email
    private String username;

    @NotNull
    @Size(min = 8, max = 128)
    private String password;

    private boolean remember;
}

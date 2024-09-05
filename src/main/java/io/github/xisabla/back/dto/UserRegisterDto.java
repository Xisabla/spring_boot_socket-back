package io.github.xisabla.back.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for user registration requests.
 */
@Data
public class UserRegisterDto {
    @NotNull
    @Size(min = 3, max = 32)
    private String username;

    @NotNull
    @Size(min = 8, max = 128)
    @Email
    private String email;

    @NotNull
    @Size(min = 8, max = 128)
    private String password;
}

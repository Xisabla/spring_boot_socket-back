package io.github.xisabla.back.dto;

import java.util.Date;
import java.util.UUID;

import io.github.xisabla.back.enums.Role;
import io.github.xisabla.back.model.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenizedUserDto {
    private UUID id;

    private String username;

    private Role role;

    private boolean enabled;

    private Date createdAt;

    private Date updatedAt;

    @Enumerated(EnumType.STRING)
    private String token;

    public static TokenizedUserDto fromUser(User user, String token) {
        return TokenizedUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .token(token)
                .build();
    }

}

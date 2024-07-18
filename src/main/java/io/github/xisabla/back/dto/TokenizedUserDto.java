package io.github.xisabla.back.dto;

import java.util.Date;
import java.util.UUID;

import javax.management.relation.Role;

import lombok.Data;

@Data
public class TokenizedUserDto {
    private UUID id;

    private String username;

    private String password;

    private Role role;

    private boolean enabled;

    private Date createdAt;

    private Date updatedAt;

    private String token;

}

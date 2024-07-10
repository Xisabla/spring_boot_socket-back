package io.github.xisabla.back.model;

import java.util.UUID;

import io.github.xisabla.back.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

/**
 * Application user.
 */
@Entity
@Data
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    @Size(min = 3, max = 30)
    private String username;

    @Size(min = 8, max = 30)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}

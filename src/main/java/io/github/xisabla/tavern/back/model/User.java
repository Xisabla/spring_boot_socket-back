package io.github.xisabla.tavern.back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.xisabla.tavern.back.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Application user model.
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {

    /**
     * Minimum length of a username.
     */
    public static final int USERNAME_MIN_LENGTH = 3;
    /**
     * Maximum length of a username.
     */
    public static final int USERNAME_MAX_LENGTH = 32;
    /**
     * Minimum length of an email.
     */
    public static final int EMAIL_MIN_LENGTH = 3;
    /**
     * Maximum length of an email.
     */
    public static final int EMAIL_MAX_LENGTH = 128;
    /**
     * Minimum length of a password.
     */
    public static final int PASSWORD_MIN_LENGTH = 8;
    /**
     * Maximum length of a password.
     */
    public static final int PASSWORD_MAX_LENGTH = 128;

    /**
     * ID of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Username of the user.
     */
    @Column(unique = true)
    @Size(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH)
    private String username;

    /**
     * Email of the user.
     */
    @Column(unique = true)
    @Size(min = EMAIL_MIN_LENGTH, max = EMAIL_MAX_LENGTH)
    private String email;

    /**
     * Password of the user. Will be hashed before storing.
     */
    @JsonIgnore
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private transient String password;

    /**
     * Role of the user. Will define its permissions (aka authorities).
     */
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    /**
     * Whether the user is enabled.
     */
    @Builder.Default
    private boolean enabled = true;

    /**
     * Whether the user is locked.
     */
    @Builder.Default
    private boolean locked = false;

    /**
     * Timestamp of the user creation.
     */
    @CreationTimestamp
    private Date createdAt;

    /**
     * Timestamp of the user last update.
     */
    @UpdateTimestamp
    private Date updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof User user && user.getId().equals(id);
    }
}

package io.github.xisabla.tavern.back.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

/**
 * Sets of permissions that can be assigned to a user.
 */
@Getter
@RequiredArgsConstructor
public enum Role {
    /**
     * Administrator role. Has all permissions.
     */
    ADMIN(Set.of(
        // Channel
        Permission.CREATE_CHANNEL,
        Permission.READ_CHANNEL,
        Permission.UPDATE_CHANNEL,
        Permission.UPDATE_CHANNEL_SELF,
        Permission.DELETE_CHANNEL,
        Permission.DELETE_CHANNEL_SELF,
        // Message
        Permission.SEND_MESSAGE,
        Permission.READ_MESSAGE,
        Permission.UPDATE_MESSAGE,
        Permission.UPDATE_MESSAGE_SELF,
        Permission.DELETE_MESSAGE,
        Permission.DELETE_MESSAGE_SELF)),
    /**
     * User role.
     * <p>
     * Has basic permissions to interact with the application.
     */
    USER(Set.of(
        // Channel
        Permission.READ_CHANNEL,
        // Message
        Permission.READ_MESSAGE,
        Permission.SEND_MESSAGE,
        Permission.UPDATE_MESSAGE_SELF,
        Permission.DELETE_MESSAGE_SELF));

    /**
     * Permissions of the role. -- GETTER -- Get the permissions of the role.
     *
     * @return The permissions of the role.
     */
    private final Set<Permission> permissions;

    /**
     * Get the authorities of the role.
     *
     * @return The permissions of the role cast as authorities.
     */
    public Set<? extends GrantedAuthority> getAuthorities() {
        return permissions;
    }

    /**
     * Check if the role has a specific permission.
     *
     * @param permission The permission to check.
     *
     * @return Whether the role has the permission.
     */
    public boolean hasPermission(final Permission permission) {
        return permissions.contains(permission);
    }
}

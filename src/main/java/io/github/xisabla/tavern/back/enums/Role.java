package io.github.xisabla.tavern.back.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

/**
 * Sets of permissions that can be assigned to a user.
 */
@RequiredArgsConstructor
public enum Role {
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
        Permission.DELETE_MESSAGE_SELF
    )),
    USER(Set.of(
        // Channel
        Permission.READ_CHANNEL,
        // Message
        Permission.READ_MESSAGE,
        Permission.SEND_MESSAGE,
        Permission.UPDATE_MESSAGE_SELF,
        Permission.DELETE_MESSAGE_SELF
    ));

    private final Set<Permission> permissions;

    public Set<? extends GrantedAuthority> getAuthorities() {
        return permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }
}

package io.github.xisabla.back.enums;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.RequiredArgsConstructor;

/**
 * Sets of permissions that can be assigned to a User.
 */
@RequiredArgsConstructor
public enum Role {
    ADMIN(Set.of(
            Permission.CREATE_CHANNEL,
            Permission.SEND_MESSAGE,
            Permission.DELETE_MESSAGES)),
    USER(Set.of(
            Permission.CREATE_CHANNEL,
            Permission.SEND_MESSAGE)),
    GUEST(Set.of());

    private final Set<Permission> permissions;

    public Collection<Permission> getPermissions() {
        return permissions;
    }

    public Collection<SimpleGrantedAuthority> getAuthorities() {
        return permissions
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermissionName()))
                .toList();
    }
}

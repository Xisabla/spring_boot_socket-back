package io.github.xisabla.back.enums;

import java.util.Set;

import lombok.RequiredArgsConstructor;

/**
 * Sets of permissions that can be assigned to a User.
 */
@RequiredArgsConstructor
public enum Role {
    ADMIN(Set.of(Permission.DELETE_MESSAGES)),
    USER(Set.of());

    private final Set<Permission> permissions;

    public Set<Permission> getPermissions() {
        return permissions;
    }
}

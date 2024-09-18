package io.github.xisabla.tavern.back.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/*
  Notice on permission naming and the use of some terms:
  <p>
  - The enum values are in the format of "ACTION_RESOURCE[_TARGET]".
  - The permission string is in the format of "resource:action[:target]".
  - The "resource" is the entity that the permission is related to.
  - The "action" is the operation that the permission allows, it should follow the CRUD operations naming.
  - The "target" is the entity that the action is being performed on, it should be used when the action is performed on a specific entity.
  - "self" should be used when the action is performed on the user itself, or a resource that the user owns.
 */

/**
 * Permissions assigned to users roles.
 */
@RequiredArgsConstructor
public enum Permission implements GrantedAuthority {
    // Channels
    CREATE_CHANNEL("channel:create"),
    READ_CHANNEL("channel:read"),
    UPDATE_CHANNEL("channel:update"),
    UPDATE_CHANNEL_SELF("channel:update:self"),
    DELETE_CHANNEL("channel:delete"),
    DELETE_CHANNEL_SELF("channel:delete:self"),
    // Messages
    SEND_MESSAGE("message:send"),
    READ_MESSAGE("message:read"),
    UPDATE_MESSAGE("message:update"),
    UPDATE_MESSAGE_SELF("message:update:self"),
    DELETE_MESSAGE("message:delete"),
    DELETE_MESSAGE_SELF("message:delete:self"),
    ;

    private final String permissionString;

    @Override
    public String getAuthority() {
        return permissionString;
    }

    @Override
    public String toString() {
        return permissionString;
    }
}

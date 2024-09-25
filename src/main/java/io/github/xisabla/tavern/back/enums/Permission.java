package io.github.xisabla.tavern.back.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/*
  Notice on permission naming and the use of some terms:
  <p>
  - The enum values are in the format of "ACTION_RESOURCE[_TARGET]".
  - The permission string is in the format of "resource:action[:target]".
  - The "resource" is the entity that the permission is related to.
  - The "action" is the operation that the permission allows, it should follow the CRUD operations naming.
  - The "target" is the entity that the action is being performed on, it should be used when the action is performed
  on a specific entity.
  - "self" should be used when the action is performed on the user itself, or a resource that the user owns.
 */

/**
 * Permissions assigned to users roles.
 */
@Getter
@RequiredArgsConstructor
public enum Permission implements GrantedAuthority {
    // Channels
    /**
     * Permission to create a channel.
     */
    CREATE_CHANNEL("channel:create"),
    /**
     * Permission to the info of a channel.
     */
    READ_CHANNEL("channel:read"),
    /**
     * Permission to update a channel.
     */
    UPDATE_CHANNEL("channel:update"),
    /**
     * Permission to update a channel that the user owns.
     */
    UPDATE_CHANNEL_SELF("channel:update:self"),
    /**
     * Permission to delete a channel.
     */
    DELETE_CHANNEL("channel:delete"),
    /**
     * Permission to delete a channel that the user owns.
     */
    DELETE_CHANNEL_SELF("channel:delete:self"),
    // Messages
    /**
     * Permission to send a message.
     */
    SEND_MESSAGE("message:send"),
    /**
     * Permission to read messages.
     */
    READ_MESSAGE("message:read"),
    /**
     * Permission to update a message.
     */
    UPDATE_MESSAGE("message:update"),
    /**
     * Permission to update a message that the user sent.
     */
    UPDATE_MESSAGE_SELF("message:update:self"),
    /**
     * Permission to delete a message.
     */
    DELETE_MESSAGE("message:delete"),
    /**
     * Permission to delete a message that the user sent.
     */
    DELETE_MESSAGE_SELF("message:delete:self");

    /**
     * The string representation of the permission.
     */
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

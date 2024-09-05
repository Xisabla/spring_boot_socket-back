package io.github.xisabla.back.enums;

/**
 * Permissions assigned to a User through a Role.
 */
public enum Permission {
    CREATE_CHANNEL("channel:create"),
    SEND_MESSAGE("message:send"),
    DELETE_MESSAGES("messages:delete");

    private final String permissionName;

    Permission(String permission) {
        this.permissionName = permission;
    }

    public String getPermissionName() {
        return permissionName;
    }

    @Override
    public String toString() {
        return permissionName;
    }
}

package io.github.xisabla.back.enums;

/**
 * Actions that can be relayed through WebSocket.
 */
public enum WebSocketAction {
    SEND_MESSAGE("message:send"),
    EDIT_MESSAGE("message:edit"),
    DELETE_MESSAGE("message:delete"),
    JOIN_CHANNEL("channel:join"),
    LEAVE_CHANNEL("channel:leave");

    private final String messageType;

    WebSocketAction(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return messageType;
    }

    @Override
    public String toString() {
        return messageType;
    }
}

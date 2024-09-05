package io.github.xisabla.back.model;

import java.util.UUID;

import io.github.xisabla.back.enums.WebSocketAction;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * WebSocket message to notify the client that a message has been sent.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WebSocketSendMessage extends WebSocketMessage {
    public WebSocketSendMessage(Message message) {
        super(message.getAuthor().getId());

        this.messageId = message.getId();
    }

    private final WebSocketAction type = WebSocketAction.SEND_MESSAGE;

    private final UUID messageId;
}

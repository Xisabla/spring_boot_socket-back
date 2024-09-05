package io.github.xisabla.back.model;

import java.util.Date;
import java.util.UUID;

import io.github.xisabla.back.enums.WebSocketAction;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

/**
 * General notice on WebSocket messages:
 *
 * - Any message sent through the WebSocket connection must be a subclass of
 * this class.
 * This is to ensure that all messages have a unique identifier and a sender as
 * much
 * as additional metadata.
 *
 * - Any message represents a single action that should be taken by the client.
 * This means that a message should not contain multiple actions, and should be
 * as specific and simple as possible. Any additional data is to be retrieved by
 * the client through the REST API.
 *
 * - WebSocket messages should be as lightweight as possible. This means that
 * data should
 * be kept to a minimum, and transmit IDs instead of full objects.
 *
 * - WebSocket messages should be considered as a way to notify the client of
 * changes
 * in the server. This means that the client should not rely on WebSocket
 * messages
 * to retrieve data, but rather to update the client's state.
 */

/**
 * WebSocket message wrapper.
 * This class is the base class for all WebSocket messages.
 * It aimes to relay actions performed by a client (or the server) to the
 * others.
 */
@Data
public abstract class WebSocketMessage {
    protected WebSocketMessage(UUID sender) {
        this.sender = sender;
    }

    protected WebSocketMessage(User sender) {
        this.sender = sender.getId();
    }

    private final UUID id = UUID.randomUUID();

    private final UUID sender;

    @Enumerated(EnumType.STRING)
    private WebSocketAction type;

    private final long timestamp = new Date().getTime();
}

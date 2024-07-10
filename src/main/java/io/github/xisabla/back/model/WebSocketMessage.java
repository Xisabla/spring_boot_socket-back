package io.github.xisabla.back.model;

import lombok.Builder;
import lombok.Data;

/**
 * Generic message for WebSocket communication.
 * TODO: Should be replaced by a standard library. (eg: STOMP)
 */
@Data
@Builder
public class WebSocketMessage {
    private String type;
    private String content;
}

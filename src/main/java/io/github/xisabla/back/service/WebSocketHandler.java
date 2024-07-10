package io.github.xisabla.back.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import io.github.xisabla.back.model.WebSocketMessage;
import io.github.xisabla.back.utils.JsonUtils;

/**
 * Handles WebSocket messages and events.
 * TODO: Should be replaced by a standard library. (eg: STOMP)
 */
@Service
public class WebSocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);

        WebSocketMessage message = WebSocketMessage.builder()
                .type("CONNECT")
                .content(session.getId() + " connected")
                .build();

        broadcast(message);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        sessions.remove(session.getId());

        WebSocketMessage message = WebSocketMessage.builder()
                .type("DISCONNECT")
                .content(session.getId() + " disconnected : " + status.getReason())
                .build();

        broadcast(message);
    }

    public void broadcast(String message) {
        sessions.values().forEach(session -> {
            try {
                session.sendMessage(new org.springframework.web.socket.TextMessage(message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void broadcast(WebSocketMessage message) {
        broadcast(JsonUtils.toJson(message));
    }
}

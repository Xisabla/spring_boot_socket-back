package io.github.xisabla.back.service;

import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import io.github.xisabla.back.model.Channel;
import io.github.xisabla.back.model.WebSocketMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebSocketService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendMessageToChannel(Channel channel, WebSocketMessage message) throws MessagingException {
        String topic = "/topic/" + channel.getId();

        simpMessagingTemplate.convertAndSend(topic, message);
    }
}

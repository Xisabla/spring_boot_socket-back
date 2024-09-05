package io.github.xisabla.back.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.xisabla.back.dto.MessageSendDto;
import io.github.xisabla.back.model.Message;
import io.github.xisabla.back.model.User;
import io.github.xisabla.back.model.WebSocketSendMessage;
import io.github.xisabla.back.service.MessageService;
import io.github.xisabla.back.service.WebSocketService;

/**
 * Controller for managing messages.
 */
@Controller
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final WebSocketService webSocketService;

    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody MessageSendDto messageSendDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        Message message = messageService.createMessage(messageSendDto, user);
        WebSocketSendMessage wsMessage = new WebSocketSendMessage(message);

        webSocketService.sendMessageToChannel(message.getChannel(), wsMessage);

        return ResponseEntity.ok(message);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable UUID id) {
        Message message = messageService.getMessageById(id);

        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID id) {
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // User user = (User) auth.getPrincipal();

        Message message = messageService.getMessageById(id);

        messageService.deleteMessage(message.getId());
        // WebSocketDeleteMessage wsMessage = new WebSocketDeleteMessage(message, user);

        // webSocketService.sendMessageToChannel(message.getChannel(), wsMessage);

        return ResponseEntity.noContent().build();
    }
}

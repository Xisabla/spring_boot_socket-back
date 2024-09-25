package io.github.xisabla.tavern.back.controller;

import io.github.xisabla.tavern.back.dto.MessageCreateDto;
import io.github.xisabla.tavern.back.model.Message;
import io.github.xisabla.tavern.back.model.User;
import io.github.xisabla.tavern.back.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing messages.
 */
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    /**
     * Service for managing messages.
     */
    private final MessageService messageService;

    /**
     * Create a message.
     *
     * @param messageCreateDto Message to create
     *
     * @return Created message
     */
    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody @Valid final MessageCreateDto messageCreateDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Message message = messageService.createMessage(messageCreateDto, user);

        return ResponseEntity.ok(message);
    }
}

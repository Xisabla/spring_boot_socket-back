package io.github.xisabla.back.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.xisabla.back.dto.ChannelCreateDto;
import io.github.xisabla.back.model.Channel;
import io.github.xisabla.back.model.Message;
import io.github.xisabla.back.model.User;
import io.github.xisabla.back.service.ChannelService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller for managing channels.
 */
@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping
    public ResponseEntity<Channel> createChannel(@RequestBody @Valid ChannelCreateDto channelCreate) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        Channel channel = channelService.createChannel(channelCreate.getName(), channelCreate.getDescription(), user);

        return ResponseEntity.ok(channel);
    }

    @GetMapping
    public ResponseEntity<Iterable<Channel>> listChannels() {
        Iterable<Channel> channels = channelService.getAllChannels();

        return ResponseEntity.ok(channels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Channel> getChannel(@PathVariable UUID id) {
        Channel channel = channelService.getChannelById(id);

        return ResponseEntity.ok(channel);
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<Message>> getMessages(@PathVariable UUID id,
            @SortDefault(sort = "createdAt", direction = Direction.DESC) @PageableDefault(page = 0, size = 20) final Pageable pageable) {
        List<Message> messages = channelService.getChannelMessages(id, pageable);

        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<User>> getUsers(@PathVariable UUID id,
            @SortDefault(sort = "createdAt", direction = Direction.DESC) @PageableDefault(page = 0, size = 20) final Pageable pageable) {
        List<User> users = channelService.getChannelUsers(id, pageable);

        return ResponseEntity.ok(users);
    }
}

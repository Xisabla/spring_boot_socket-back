package io.github.xisabla.tavern.back.controller;

import io.github.xisabla.tavern.back.dto.ChannelCreateDto;
import io.github.xisabla.tavern.back.exception.NotImplementedException;
import io.github.xisabla.tavern.back.model.Channel;
import io.github.xisabla.tavern.back.model.User;
import io.github.xisabla.tavern.back.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller for managing channels.
 */
@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping
    public ResponseEntity<Channel> createChannel(@RequestBody @Valid ChannelCreateDto channelCreateDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Channel channel = channelService.createChannel(channelCreateDto, user);

        return ResponseEntity.ok(channel);
    }

    @GetMapping
    public ResponseEntity<Page<Channel>> getChannels(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String description,
        @RequestParam(required = false) Boolean isPublic,
        @RequestParam(required = false) Boolean enabled,
        @RequestParam(required = false) UUID ownerId,
        @SortDefault @PageableDefault Pageable pageable) {
        Page<Channel> channels = channelService.getAllChannelsByFilters(name, description, isPublic, enabled, ownerId, pageable);

        return ResponseEntity.ok(channels);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Channel> getChannelById(@PathVariable UUID id) {
        Channel channel = channelService.getChannelById(id);

        return ResponseEntity.ok(channel);
    }

    @GetMapping("/id/{id}/messages")
    public ResponseEntity<Page<Void>> getChannelMessages(@PathVariable UUID id, @SortDefault @PageableDefault Pageable pageable) {
        throw new NotImplementedException();
    }

    @GetMapping("/id/{id}/members")
    public ResponseEntity<Page<User>> getChannelMembers(@PathVariable UUID id, @SortDefault(sort = "username") @PageableDefault Pageable pageable) {
        Page<User> members = channelService.getChannelMembers(id, pageable);

        return ResponseEntity.ok(members);
    }

}

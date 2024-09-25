package io.github.xisabla.tavern.back.controller;

import io.github.xisabla.tavern.back.dto.ChannelCreateDto;
import io.github.xisabla.tavern.back.model.Channel;
import io.github.xisabla.tavern.back.model.Message;
import io.github.xisabla.tavern.back.model.User;
import io.github.xisabla.tavern.back.service.ChannelService;
import io.github.xisabla.tavern.back.service.MessageService;
import jakarta.validation.Valid;
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
    /**
     * Service for managing channels.
     */
    private final ChannelService channelService;
    /**
     * Service for managing messages.
     */
    private final MessageService messageService;

    /**
     * Create a new channel.
     *
     * @param channelCreateDto DTO containing the information to create the channel.
     *
     * @return The created channel.
     */
    @PostMapping
    public ResponseEntity<Channel> createChannel(@RequestBody @Valid final ChannelCreateDto channelCreateDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Channel channel = channelService.createChannel(channelCreateDto, user);

        return ResponseEntity.ok(channel);
    }

    /**
     * Get all channels.
     *
     * @param name        Optional name filter.
     * @param description Optional description filter.
     * @param isPublic    Optional isPublic filter.
     * @param enabled     Optional enabled filter.
     * @param ownerId     Optional ownerId filter.
     * @param pageable    Pageable object to control pagination.
     *
     * @return Page of channels.
     */
    @GetMapping
    public ResponseEntity<Page<Channel>> getChannels(@RequestParam(required = false) final String name,
                                                     @RequestParam(required = false) final String description,
                                                     @RequestParam(required = false) final Boolean isPublic,
                                                     @RequestParam(required = false) final Boolean enabled,
                                                     @RequestParam(required = false) final UUID ownerId,
                                                     @SortDefault @PageableDefault final Pageable pageable
    ) {
        Page<Channel> channels = channelService.getAllChannelsByFilters(
            name,
            description,
            isPublic,
            enabled,
            ownerId,
            pageable);

        return ResponseEntity.ok(channels);
    }

    /**
     * Get a channel by its ID.
     *
     * @param id ID of the channel to get.
     *
     * @return The channel with the given ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Channel> getChannelById(@PathVariable final UUID id) {
        Channel channel = channelService.getChannelById(id);

        return ResponseEntity.ok(channel);
    }

    /**
     * Get all messages in a channel.
     *
     * @param id       ID of the channel to get messages of.
     * @param pageable Pageable object to control pagination.
     *
     * @return Page of messages in the channel.
     */
    @GetMapping("/{id}/messages")
    public ResponseEntity<Page<Message>> getChannelMessages(@PathVariable final UUID id,
                                                            @SortDefault @PageableDefault final Pageable pageable
    ) {
        Page<Message> messages = messageService.getMessagesInChannel(id, pageable);

        return ResponseEntity.ok(messages);
    }

    /**
     * Get all members of a channel.
     *
     * @param id       ID of the channel to get members of.
     * @param pageable Pageable object to control pagination.
     *
     * @return Page of users who are members of the channel.
     */
    @GetMapping("/{id}/members")
    public ResponseEntity<Page<User>> getChannelMembers(@PathVariable final UUID id,
                                                        @SortDefault(sort = "username") @PageableDefault
                                                        final Pageable pageable
    ) {
        Page<User> members = channelService.getChannelMembers(id, pageable);

        return ResponseEntity.ok(members);
    }

}

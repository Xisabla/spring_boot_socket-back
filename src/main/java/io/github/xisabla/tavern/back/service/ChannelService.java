package io.github.xisabla.tavern.back.service;

import io.github.xisabla.tavern.back.dto.ChannelCreateDto;
import io.github.xisabla.tavern.back.exception.ChannelAlreadyExistsException;
import io.github.xisabla.tavern.back.exception.ChannelNotFoundException;
import io.github.xisabla.tavern.back.model.Channel;
import io.github.xisabla.tavern.back.model.User;
import io.github.xisabla.tavern.back.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service to handle channels.
 */
@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;

    //
    // Create
    //

    private Channel createChannel(Channel channel) throws ChannelAlreadyExistsException {
        if (channelRepository.findByName(channel.getName()).isPresent()) {
            throw new ChannelAlreadyExistsException(channel.getName());
        }

        return channelRepository.save(channel);
    }

    private Channel createChannel(String name, String description, User owner, boolean isPublic, boolean enabled) throws ChannelAlreadyExistsException {
        Channel channel = Channel.builder()
            .name(name)
            .description(description)
            .owner(owner)
            .isPublic(isPublic)
            .enabled(enabled)
            .members(List.of(owner))
            .build();

        return createChannel(channel);
    }

    public Channel createChannel(ChannelCreateDto channelCreateDto, User owner) throws ChannelAlreadyExistsException {
        return createChannel(channelCreateDto.getName(), channelCreateDto.getDescription(), owner, true, true);
    }

    //
    // Read
    //

    public Page<Channel> getAllChannelsByFilters(String name, String description, Boolean isPublic, Boolean enabled, UUID ownerId, Pageable pageable) {
        return channelRepository.findAllByFilters(name, description, isPublic, enabled, ownerId, pageable);
    }

    public Channel getChannelById(UUID id) throws ChannelNotFoundException {
        return channelRepository.findById(id).orElseThrow(() -> new ChannelNotFoundException(id));
    }

    public Page<User> getChannelMembers(UUID id, Pageable pageable) throws ChannelNotFoundException {
        Channel channel = getChannelById(id);

        return channelRepository.getMembersInChannel(channel, pageable);
    }

}

package io.github.xisabla.tavern.back.service;

import io.github.xisabla.tavern.back.dto.ChannelCreateDto;
import io.github.xisabla.tavern.back.exception.ChannelAlreadyExistsException;
import io.github.xisabla.tavern.back.exception.ChannelNotFoundException;
import io.github.xisabla.tavern.back.model.Channel;
import io.github.xisabla.tavern.back.model.User;
import io.github.xisabla.tavern.back.repository.ChannelRepository;
import jakarta.transaction.Transactional;
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
    /**
     * Repository for channels.
     */
    private final ChannelRepository channelRepository;

    //
    // Create
    //

    /**
     * Create a channel.
     *
     * @param channel Channel to create
     *
     * @return Created channel
     *
     * @throws ChannelAlreadyExistsException If a channel with the same name already exists
     */
    @Transactional
    private Channel createChannel(final Channel channel) throws ChannelAlreadyExistsException {
        if (channelRepository.findByName(channel.getName()).isPresent()) {
            throw new ChannelAlreadyExistsException(channel.getName());
        }

        return channelRepository.save(channel);
    }

    /**
     * Create a channel.
     *
     * @param name        Channel name
     * @param description Channel description
     * @param owner       Owner of the channel
     * @param isPublic    Whether the channel is public
     * @param enabled     Whether the channel is enabled
     *
     * @return Created channel
     *
     * @throws ChannelAlreadyExistsException If a channel with the same name already exists
     */
    private Channel createChannel(final String name, final String description, final User owner, final boolean isPublic,
                                  final boolean enabled
    )
        throws ChannelAlreadyExistsException {
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

    /**
     * Create a channel.
     *
     * @param channelCreateDto Channel to create
     * @param owner            Owner of the channel
     *
     * @return Created channel
     *
     * @throws ChannelAlreadyExistsException If a channel with the same name already exists
     */
    public Channel createChannel(final ChannelCreateDto channelCreateDto, final User owner)
        throws ChannelAlreadyExistsException {
        return createChannel(channelCreateDto.getName(), channelCreateDto.getDescription(), owner, true, true);
    }

    //
    // Read
    //

    /**
     * Get channels matching the given filters.
     *
     * @param name        Name of the channel
     * @param description Description of the channel
     * @param isPublic    Whether the channel is public
     * @param enabled     Whether the channel is enabled
     * @param ownerId     ID of the owner of the channel
     * @param pageable    Pageable object to use for pagination
     *
     * @return Page of channels matching the filters
     */
    public Page<Channel> getAllChannelsByFilters(final String name,
                                                 final String description,
                                                 final Boolean isPublic,
                                                 final Boolean enabled,
                                                 final UUID ownerId,
                                                 final Pageable pageable
    ) {
        return channelRepository.findAllByFilters(name, description, isPublic, enabled, ownerId, pageable);
    }

    /**
     * Get a channel by its ID.
     *
     * @param id ID of the channel to get
     *
     * @return Channel with the given ID
     *
     * @throws ChannelNotFoundException If the channel with the given ID does not exist
     */
    public Channel getChannelById(final UUID id) throws ChannelNotFoundException {
        return channelRepository.findById(id).orElseThrow(() -> new ChannelNotFoundException(id));
    }

    /**
     * Get the users member of a channel.
     *
     * @param id       ID of the channel to get the members of
     * @param pageable Pageable object to use for pagination
     *
     * @return Page of users in the channel
     *
     * @throws ChannelNotFoundException If the channel with the given ID does not exist
     */
    @Transactional
    public Page<User> getChannelMembers(final UUID id, final Pageable pageable) throws ChannelNotFoundException {
        Channel channel = getChannelById(id);

        return channelRepository.getMembersInChannel(channel, pageable);
    }

    //
    // Helpers
    //

    /**
     * Check if a user is a member of a channel.
     *
     * @param channel Channel to check
     * @param user    User to check
     *
     * @return True if the user is a member of the channel, false otherwise
     */
    public boolean isMemberOfChannel(final Channel channel, final User user) {
        return channel.getMembers().contains(user);
    }

}

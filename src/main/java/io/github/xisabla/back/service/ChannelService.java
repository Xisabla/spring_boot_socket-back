package io.github.xisabla.back.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.github.xisabla.back.exception.ChannelAlreadyExistsException;
import io.github.xisabla.back.exception.ChannelNotFoundException;
import io.github.xisabla.back.model.Channel;
import io.github.xisabla.back.model.Message;
import io.github.xisabla.back.model.User;
import io.github.xisabla.back.repository.ChannelRepositoryInterface;
import io.github.xisabla.back.repository.MessageRepositoryInterface;
import lombok.RequiredArgsConstructor;

/**
 * Service for managing channels.
 */
@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepositoryInterface channelRepository;
    private final MessageRepositoryInterface messageRepository;

    //
    // CREATE
    //

    private Channel createChannel(final Channel channel) throws ChannelAlreadyExistsException {
        if (channelRepository.findByName(channel.getName()).isPresent()) {
            throw new ChannelAlreadyExistsException(channel.getName());
        }

        return channelRepository.save(channel);
    }

    private Channel createChannel(final String name, final String description, final User owner, final boolean isPublic,
            final boolean enabled) {
        Channel channel = Channel.builder()
                .name(name)
                .description(description)
                .owner(owner)
                .isPublic(isPublic)
                .enabled(enabled)
                .build();

        return createChannel(channel);
    }

    public Channel createChannel(final String name, final String description, final User owner,
            final boolean isPublic) {
        return createChannel(name, description, owner, isPublic, true);
    }

    public Channel createChannel(final String name, final String description, final User owner) {
        return createChannel(name, description, owner, true, true);
    }

    //
    // READ
    //

    public Iterable<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    public Channel getChannelById(UUID id) throws ChannelNotFoundException {
        return channelRepository.findById(id).orElseThrow(() -> new ChannelNotFoundException(id));
    }

    public Channel getChannelByName(String name) throws ChannelNotFoundException {
        return channelRepository.findByName(name).orElseThrow(() -> new ChannelNotFoundException(name));
    }

    public List<Message> getChannelMessages(UUID id, Pageable pageable) {
        Channel channel = getChannelById(id);

        return messageRepository.findByChannel(channel, pageable);
    }

    public List<User> getChannelUsers(UUID id, Pageable pageable) {
        Channel channel = getChannelById(id);

        return channelRepository.findMembersByChannel(channel, pageable);
    }

    //
    // UPDATE
    //

    private Channel updateChannel(Channel channel) {
        return channelRepository.save(channel);
    }

    public Channel renameChannel(UUID id, String name) throws ChannelNotFoundException {
        Channel channel = getChannelById(id);

        channel.setName(name);

        return updateChannel(channel);
    }

    public Channel changeChannelDescription(UUID id, String description) throws ChannelNotFoundException {
        Channel channel = getChannelById(id);

        channel.setDescription(description);

        return updateChannel(channel);
    }

    public Channel changeChannelOwner(UUID id, User owner) throws ChannelNotFoundException {
        Channel channel = getChannelById(id);

        channel.setOwner(owner);

        return updateChannel(channel);
    }

    public Channel addChannelMember(UUID id, User member) throws ChannelNotFoundException {
        Channel channel = getChannelById(id);

        channel.getMembers().add(member);

        return updateChannel(channel);
    }

    public Channel setChannelPublic(UUID id) throws ChannelNotFoundException {
        Channel channel = getChannelById(id);

        channel.setPublic(true);

        return updateChannel(channel);
    }

    public Channel setChannelPrivate(UUID id) throws ChannelNotFoundException {
        Channel channel = getChannelById(id);

        channel.setPublic(false);

        return updateChannel(channel);
    }

    public Channel disableChannel(UUID id) throws ChannelNotFoundException {
        Channel channel = getChannelById(id);

        channel.setEnabled(false);

        return updateChannel(channel);
    }
}

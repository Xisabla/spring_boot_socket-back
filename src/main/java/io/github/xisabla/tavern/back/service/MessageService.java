package io.github.xisabla.tavern.back.service;

import io.github.xisabla.tavern.back.dto.MessageCreateDto;
import io.github.xisabla.tavern.back.exception.ChannelNotFoundException;
import io.github.xisabla.tavern.back.exception.UserNotMemberOfChannelException;
import io.github.xisabla.tavern.back.model.Channel;
import io.github.xisabla.tavern.back.model.Message;
import io.github.xisabla.tavern.back.model.User;
import io.github.xisabla.tavern.back.repository.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service to handle messages.
 */
@Service
@RequiredArgsConstructor
public class MessageService {
    /**
     * The message repository.
     */
    private final MessageRepository messageRepository;
    /**
     * The channel service.
     */
    private final ChannelService channelService;

    //
    // Create
    //

    /**
     * Create a message.
     *
     * @param message The message to create.
     *
     * @return The created message.
     */
    private Message createMessage(final Message message) {
        return messageRepository.save(message);
    }

    /**
     * Create a message.
     *
     * @param content The content of the message.
     * @param author  The author of the message.
     * @param channel The channel the message is in.
     * @param deleted Whether the message is deleted.
     *
     * @return The created message.
     */
    private Message createMessage(final String content, final User author, final Channel channel,
                                  final boolean deleted
    ) {
        Message message = Message.builder()
                                 .content(content)
                                 .author(author)
                                 .channel(channel)
                                 .deleted(deleted)
                                 .build();

        return createMessage(message);
    }

    /**
     * Create a message.
     *
     * @param messageCreateDto The message create DTO.
     * @param author           The author of the message.
     *
     * @return The created message.
     *
     * @throws ChannelNotFoundException If the channel does not exist.
     */
    @Transactional
    public Message createMessage(final MessageCreateDto messageCreateDto, final User author)
        throws ChannelNotFoundException {
        Channel channel = channelService.getChannelById(messageCreateDto.getChannelId());

        if (!channelService.isMemberOfChannel(channel, author)) {
            throw new UserNotMemberOfChannelException(author, channel);
        }

        return createMessage(messageCreateDto.getContent(), author, channel, false);
    }

    //
    // Read
    //

    /**
     * Get the messages in a channel.
     *
     * @param channelId The channel id.
     * @param pageable  Pageable object to control pagination.
     *
     * @return A page of messages.
     *
     * @throws ChannelNotFoundException If the channel does not exist.
     */
    @Transactional
    public Page<Message> getMessagesInChannel(final UUID channelId, final Pageable pageable)
        throws ChannelNotFoundException {
        Channel channel = channelService.getChannelById(channelId);

        return messageRepository.findByChannel(channel, pageable);
    }
}

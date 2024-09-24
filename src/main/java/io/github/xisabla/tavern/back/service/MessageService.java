package io.github.xisabla.tavern.back.service;

import io.github.xisabla.tavern.back.dto.MessageCreateDto;
import io.github.xisabla.tavern.back.exception.ChannelNotFoundException;
import io.github.xisabla.tavern.back.exception.UserNotMemberOfChannelException;
import io.github.xisabla.tavern.back.model.Channel;
import io.github.xisabla.tavern.back.model.Message;
import io.github.xisabla.tavern.back.model.User;
import io.github.xisabla.tavern.back.repository.MessageRepository;
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
    private final MessageRepository messageRepository;
    private final ChannelService channelService;

    //
    // Create
    //

    private Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    private Message createMessage(String content, User author, Channel channel, boolean deleted) {
        Message message = Message.builder()
            .content(content)
            .author(author)
            .channel(channel)
            .deleted(deleted)
            .build();

        return createMessage(message);
    }

    public Message createMessage(MessageCreateDto messageCreateDto, User author) throws ChannelNotFoundException {
        Channel channel = channelService.getChannelById(messageCreateDto.getChannelId());

        if (!channelService.isMemberOfChannel(channel, author)) {
            throw new UserNotMemberOfChannelException(author, channel);
        }

        return createMessage(messageCreateDto.getContent(), author, channel, false);
    }

    //
    // Read
    //

    public Page<Message> getMessagesInChannel(UUID channelId, Pageable pageable) throws ChannelNotFoundException {
        Channel channel = channelService.getChannelById(channelId);

        return messageRepository.findByChannel(channel, pageable);
    }
}

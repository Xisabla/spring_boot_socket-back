package io.github.xisabla.back.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.github.xisabla.back.dto.MessageSendDto;
import io.github.xisabla.back.exception.ChannelNotFoundException;
import io.github.xisabla.back.exception.MessageNotFoundException;
import io.github.xisabla.back.model.Channel;
import io.github.xisabla.back.model.Message;
import io.github.xisabla.back.model.User;
import io.github.xisabla.back.repository.ChannelRepositoryInterface;
import io.github.xisabla.back.repository.MessageRepositoryInterface;
import lombok.RequiredArgsConstructor;

/**
 * Service to handle message operations.
 */
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepositoryInterface messageRepository;
    private final ChannelRepositoryInterface channelRepository;

    //
    // CREATE
    //

    private Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    private Message createMessage(String content, User author, Channel channel) {
        Message message = Message.builder()
                .content(content)
                .author(author)
                .channel(channel)
                .build();

        return createMessage(message);
    }

    public Message createMessage(MessageSendDto messageSendDto, User author) throws ChannelNotFoundException {
        UUID channelId = messageSendDto.getChannelId();
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ChannelNotFoundException(channelId));

        return createMessage(messageSendDto.getContent(), author, channel);
    }

    //
    // READ
    //

    public Message getMessageById(UUID id) throws MessageNotFoundException {
        return messageRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException(id));
    }

    public List<Message> getMessagesByAuthor(User author, Pageable pageable) {
        return messageRepository.findByAuthor(author, pageable);
    }

    //
    // UPDATE
    //

    private Message updateMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message updateMessageContent(UUID id, String content) throws MessageNotFoundException {
        Message message = getMessageById(id);

        message.setContent(content);

        return updateMessage(message);
    }

    public Message deleteMessage(UUID id) throws MessageNotFoundException {
        Message message = getMessageById(id);

        message.setDeleted(true);

        return updateMessage(message);
    }
}

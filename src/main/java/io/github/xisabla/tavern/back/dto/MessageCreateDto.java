package io.github.xisabla.tavern.back.dto;

import io.github.xisabla.tavern.back.model.Message;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

/**
 * DTO for sending a message.
 */
@Data
public class MessageCreateDto {
    /**
     * Content of the message.
     */
    @NotNull
    @Size(max = Message.MAX_CONTENT_LENGTH)
    private String content;

    /**
     * The ID of the channel the message is sent in.
     */
    @NotNull
    private UUID channelId;
}

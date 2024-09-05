package io.github.xisabla.back.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for sending a message in a channel.
 */
@Data
public class MessageSendDto {
    @NotNull
    private UUID channelId;

    @NotNull
    @Size(max = 4096)
    private String content;
}

package io.github.xisabla.tavern.back.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

/**
 * DTO for sending a message.
 */
@Data
public class MessageCreateDto {
    @NotNull
    @Size(max = 4096)
    private String content;

    @NotNull
    private UUID channelId;
}

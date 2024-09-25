package io.github.xisabla.tavern.back.dto;

import io.github.xisabla.tavern.back.model.Channel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for creating a channel.
 */
@Data
public class ChannelCreateDto {
    /**
     * Name of the channel to create.
     */
    @NotNull
    @Size(min = Channel.NAME_MIN_LENGTH, max = Channel.NAME_MAX_LENGTH)
    private String name;

    /**
     * Description of the channel to create.
     */
    @Size(max = Channel.DESCRIPTION_MAX_LENGTH)
    private String description;
}

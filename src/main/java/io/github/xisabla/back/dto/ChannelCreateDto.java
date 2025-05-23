package io.github.xisabla.back.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for creating a channel.
 */
@Data
public class ChannelCreateDto {
    @NotNull
    @Size(min = 3, max = 32)
    private String name;

    @NotNull
    @Size(max = 512)
    private String description;
}

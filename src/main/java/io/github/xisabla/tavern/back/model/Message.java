package io.github.xisabla.tavern.back.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.UUID;

/**
 * Chat message model.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "messages")
public class Message {

    /**
     * The maximum length of the message content.
     */
    public static final int MAX_CONTENT_LENGTH = 4096;

    /**
     * Message ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Content of the message.
     */
    @Size(max = MAX_CONTENT_LENGTH)
    private String content;

    /**
     * The user who sent the message.
     */
    @ManyToOne
    private User author;

    /**
     * The channel the message is sent in.
     */
    @ManyToOne
    private Channel channel;

    /**
     * Whether the message is deleted.
     */
    @Builder.Default
    private boolean deleted = false;

    /**
     * The date the message was created.
     */
    @CreationTimestamp
    private Date createdAt;

    /**
     * The date the message was last updated.
     */
    @UpdateTimestamp
    private Date updatedAt;

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Message message && message.getId().equals(this.id);
    }
}

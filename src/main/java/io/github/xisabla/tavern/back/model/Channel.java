package io.github.xisabla.tavern.back.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * Chat channel model.
 */
@Entity
@Getter
@Setter
@ToString(exclude = { "owner", "members" })
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "channels")
public class Channel {

    /**
     * Minimum length of the channel name.
     */
    public static final int NAME_MIN_LENGTH = 3;
    /**
     * Maximum length of the channel name.
     */
    public static final int NAME_MAX_LENGTH = 32;
    /**
     * Maximum length of the channel description.
     */
    public static final int DESCRIPTION_MAX_LENGTH = 512;

    /**
     * Channel ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Name of the channel.
     */
    @Column(unique = true)
    @Size(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH)
    private String name;

    /**
     * Description of the channel.
     */
    @Size(max = DESCRIPTION_MAX_LENGTH)
    private String description;

    /**
     * The user who created the channel.
     */
    @ManyToOne
    private User owner;

    /**
     * All users who are members of the channel.
     */
    @ManyToMany
    private List<User> members;

    /**
     * Whether the channel is public or private.
     */
    @Builder.Default
    private boolean isPublic = true;

    /**
     * Whether the channel is enabled or disabled.
     */
    @Builder.Default
    private boolean enabled = true;

    /**
     * Date and time when the channel was created.
     */
    @CreationTimestamp
    private Date createdAt;

    /**
     * Date and time when the channel was last updated.
     */
    @UpdateTimestamp
    private Date updatedAt;

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Channel channel && channel.getId().equals(this.id);
    }
}

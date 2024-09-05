package io.github.xisabla.back.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

/**
 * Channel model.
 * A channel is a chat room where users can send messages.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    @Size(min = 3, max = 32)
    private String name;

    @Size(max = 512)
    private String description;

    @ManyToOne
    private User owner;

    @ManyToMany
    private List<User> members;

    /**
     * Note: This field is not used in the current version of the application.
     * Later, it will be used to determine whether a channel is public or private.
     * A public channel can be joined by anyone, while a private channel requires an
     * invitation (link) to join.
     * A table will be created to store the invitations.
     * Another table will be created to store the channel members.
     */
    @Default
    private boolean isPublic = true;

    @Default
    private boolean enabled = true;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}

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
@ToString(exclude = {"owner", "members"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "channels")
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

    @Builder.Default
    private boolean isPublic = true;

    @Builder.Default
    private boolean enabled = true;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

}

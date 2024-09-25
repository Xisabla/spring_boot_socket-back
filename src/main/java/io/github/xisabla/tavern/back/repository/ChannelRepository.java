package io.github.xisabla.tavern.back.repository;

import io.github.xisabla.tavern.back.model.Channel;
import io.github.xisabla.tavern.back.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {
    /**
     * Find all channels by filters.
     *
     * @param name        Name of the channel to find.
     * @param description Description of the channel to find.
     * @param isPublic    Whether the channel is public.
     * @param enabled     Whether the channel is enabled.
     * @param ownerId     ID of the owner of the channel.
     * @param pageable    Pageable object to control pagination.
     *
     * @return Page of channels that match the filters.
     */
    @Query("SELECT channel FROM Channel channel WHERE " + "(:name IS NULL OR channel.name LIKE %:name%) AND "
        + "(:description IS NULL OR channel.description LIKE %:description%) AND "
        + "(:isPublic IS NULL OR channel.isPublic = :isPublic) AND "
        + "(:enabled IS NULL OR channel.enabled = :enabled) AND " + "(:ownerId IS NULL OR channel.owner.id = :ownerId)")
    Page<Channel> findAllByFilters(String name,
                                   String description,
                                   Boolean isPublic,
                                   Boolean enabled,
                                   UUID ownerId,
                                   Pageable pageable
    );

    /**
     * Find a channel by its name.
     *
     * @param name Name of the channel to find.
     *
     * @return Optional containing the channel if found.
     */
    Optional<Channel> findByName(String name);

    /**
     * Get all members in a channel.
     *
     * @param channel  The channel to get the members from.
     * @param pageable Pageable object to control pagination.
     *
     * @return Page of users in the channel.
     */
    @Query("SELECT channel.members FROM Channel channel WHERE channel.id = :channelId")
    Page<User> getMembersInChannel(Channel channel, Pageable pageable);

}

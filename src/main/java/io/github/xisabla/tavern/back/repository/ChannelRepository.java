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
    @Query("SELECT channel FROM Channel channel WHERE " +
        "(:name IS NULL OR channel.name LIKE %:name%) AND " +
        "(:description IS NULL OR channel.description LIKE %:description%) AND " +
        "(:isPublic IS NULL OR channel.isPublic = :isPublic) AND " +
        "(:enabled IS NULL OR channel.enabled = :enabled) AND " +
        "(:ownerId IS NULL OR channel.owner.id = :ownerId)")
    Page<Channel> findAllByFilters(String name, String description, Boolean isPublic, Boolean enabled, UUID ownerId, Pageable pageable);

    Optional<Channel> findByName(String name);

    @Query("SELECT channel.members FROM Channel channel WHERE channel.id = :channelId")
    Page<User> getMembersInChannel(Channel channel, Pageable pageable);

}

package io.github.xisabla.back.repository;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

import io.github.xisabla.back.model.Channel;
import io.github.xisabla.back.model.User;

public interface ChannelRepositoryInterface extends CrudRepository<Channel, UUID> {
    Optional<Channel> findByName(String name);

    List<Channel> findByOwner(User owner);

    List<Channel> findByOwnerId(UUID ownerId);

    @Query("SELECT c.members FROM Channel c WHERE c.id = :channelId")
    List<User> findMembersByChannel(Channel channel, Pageable pageable);
}

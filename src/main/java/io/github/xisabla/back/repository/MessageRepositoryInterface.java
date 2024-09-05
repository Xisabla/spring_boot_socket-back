package io.github.xisabla.back.repository;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

import io.github.xisabla.back.model.Channel;
import io.github.xisabla.back.model.Message;
import io.github.xisabla.back.model.User;

public interface MessageRepositoryInterface extends CrudRepository<Message, UUID> {
    List<Message> findByChannel(Channel channel, Pageable pageable);

    List<Message> findByAuthor(User author, Pageable pageable);
}

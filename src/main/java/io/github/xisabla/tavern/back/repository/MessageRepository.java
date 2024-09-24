package io.github.xisabla.tavern.back.repository;

import io.github.xisabla.tavern.back.model.Channel;
import io.github.xisabla.tavern.back.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    Page<Message> findByChannel(Channel channel, Pageable pageable);
}

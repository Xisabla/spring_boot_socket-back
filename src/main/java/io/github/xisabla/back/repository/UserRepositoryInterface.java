package io.github.xisabla.back.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

import io.github.xisabla.back.enums.Role;
import io.github.xisabla.back.model.User;

public interface UserRepositoryInterface extends CrudRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);
}

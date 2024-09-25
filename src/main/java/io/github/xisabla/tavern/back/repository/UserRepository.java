package io.github.xisabla.tavern.back.repository;

import io.github.xisabla.tavern.back.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Find a user by username.
     *
     * @param username The username to search for
     *
     * @return An optional containing the user if found, empty otherwise
     */
    Optional<User> findByUsername(String username);

    /**
     * Find a user by email.
     *
     * @param email The email to search for
     *
     * @return An optional containing the user if found, empty otherwise
     */
    Optional<User> findByEmail(String email);

}

package io.github.xisabla.tavern.back.service;

import io.github.xisabla.tavern.back.enums.Role;
import io.github.xisabla.tavern.back.exception.UserAlreadyExistsException;
import io.github.xisabla.tavern.back.exception.UserNotFoundException;
import io.github.xisabla.tavern.back.model.User;
import io.github.xisabla.tavern.back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service to manage application users.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    //
    // Create
    //

    private User createUser(User user) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(user.getUsername());
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(user.getEmail());
        }

        return userRepository.save(user);
    }

    private User createUser(String username, String email, String password, Role role, boolean enabled, boolean locked) throws UserAlreadyExistsException {
        User user = User.builder()
            .username(username)
            .email(email)
            .password(password)
            .role(role)
            .enabled(enabled)
            .locked(locked)
            .build();

        return createUser(user);
    }

    public User createUser(String username, String email, String password) throws UserAlreadyExistsException {
        return createUser(username, email, password, Role.USER, true, false);
    }

    //
    // Read
    //

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(UUID id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User getUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    public User getUserByLogin(String login) throws UserNotFoundException {
        return userRepository.findByUsername(login)
            .orElseGet(() -> userRepository.findByEmail(login)
                .orElseThrow(() -> new UserNotFoundException(login)));
    }
}

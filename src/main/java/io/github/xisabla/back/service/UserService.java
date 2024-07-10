package io.github.xisabla.back.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.xisabla.back.enums.Role;
import io.github.xisabla.back.exception.UserNotFoundException;
import io.github.xisabla.back.model.User;
import io.github.xisabla.back.repository.UserRepositoryInterface;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepositoryInterface userRepository;

    //
    // CREATE
    //

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User createUser(String username, String password, Role role) {
        User user = User.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();

        return createUser(user);
    }

    public User createUser(String username, String password) {
        return createUser(username, password, Role.USER);
    }

    //
    // READ
    //

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }
}

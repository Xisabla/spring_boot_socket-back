package io.github.xisabla.back.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.xisabla.back.dto.LoginUserDto;
import io.github.xisabla.back.dto.RegisterUserDto;
import io.github.xisabla.back.dto.TokenizedUserDto;
import io.github.xisabla.back.enums.Role;
import io.github.xisabla.back.exception.APIException;
import io.github.xisabla.back.exception.UserNotFoundException;
import io.github.xisabla.back.model.User;
import io.github.xisabla.back.repository.UserRepositoryInterface;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepositoryInterface userRepository;

    private final JwtService jwtService;

    private final Argon2PasswordEncoder passwordEncoder;

    //
    // CREATE
    //

    public User createUser(User user) throws APIException {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new APIException(HttpStatus.CONFLICT, "User already exists");
        }

        return userRepository.save(user);
    }

    public User createUser(String username, String password, Role role, boolean enabled) {
        User user = User.builder()
                .username(username)
                .password(encodePassword(password))
                .role(role)
                .enabled(enabled)
                .build();

        return createUser(user);
    }

    public User createUser(String username, String password) {
        return createUser(username, password, Role.USER, true);
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

    //
    // AUTH
    //

    public TokenizedUserDto registerUser(RegisterUserDto registerUser) {
        User user = createUser(registerUser.getUsername(), registerUser.getPassword());
        String token = createToken(user);

        return TokenizedUserDto.fromUser(user, token);
    }

    public TokenizedUserDto loginUser(LoginUserDto loginUser) {
        User user = getUserByUsername(loginUser.getUsername());

        if (!checkPassword(loginUser.getPassword(), user.getPassword())) {
            throw new APIException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        String token = createToken(user);

        return TokenizedUserDto.fromUser(user, token);
    }

    public String createToken(User user) {
        return jwtService.generateToken(user);
    }

    //
    // OTHERS
    //

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}

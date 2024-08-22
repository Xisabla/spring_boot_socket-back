package io.github.xisabla.back.service;

import java.util.UUID;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.xisabla.back.dto.LoginUserDto;
import io.github.xisabla.back.dto.RegisterUserDto;
import io.github.xisabla.back.enums.Role;
import io.github.xisabla.back.exception.InvalidCredentialsException;
import io.github.xisabla.back.exception.UserAlreadyExistsException;
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

    public User createUser(User user) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(user.getUsername());
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(user.getEmail());
        }

        return userRepository.save(user);
    }

    public User createUser(String username, String email, String password, Role role, boolean enabled) {
        User user = User.builder()
                .username(username)
                .email(email)
                .password(encodePassword(password))
                .role(role)
                .enabled(enabled)
                .build();

        return createUser(user);
    }

    public User createUser(String username, String email, String password) {
        return createUser(username, email, password, Role.USER, true);
    }

    //
    // READ
    //

    public Iterable<User> getAllUsers() {
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

    public User getUserByUsernameOrEmail(String usernameOrEmail) throws UserNotFoundException {
        try {
            return getUserByUsername(usernameOrEmail);
        } catch (UserNotFoundException e) {
            return getUserByEmail(usernameOrEmail);
        }
    }

    //
    // AUTH
    //

    public User registerUser(RegisterUserDto registerUser) {
        return createUser(registerUser.getUsername(), registerUser.getEmail(), registerUser.getPassword());
    }

    public User loginUser(LoginUserDto loginUser) throws InvalidCredentialsException {
        try {
            User user = getUserByUsernameOrEmail(loginUser.getUsername());

            if (!checkPassword(loginUser.getPassword(), user.getPassword())) {
                throw new InvalidCredentialsException();
            }

            return user;
        } catch (UserAlreadyExistsException e) {
            throw new InvalidCredentialsException();
        }
    }

    public String createToken(User user) {
        return jwtService.generateToken(user);
    }

    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }

    public String extractUsernameFromToken(String token) {
        return jwtService.extractUsername(token);
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

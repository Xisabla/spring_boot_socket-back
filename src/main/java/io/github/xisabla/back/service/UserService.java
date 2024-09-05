package io.github.xisabla.back.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.xisabla.back.dto.UserLoginDto;
import io.github.xisabla.back.dto.UserRegisterDto;
import io.github.xisabla.back.enums.Role;
import io.github.xisabla.back.exception.InvalidCredentialsException;
import io.github.xisabla.back.exception.UserAlreadyExistsException;
import io.github.xisabla.back.exception.UserNotFoundException;
import io.github.xisabla.back.model.Channel;
import io.github.xisabla.back.model.User;
import io.github.xisabla.back.repository.UserRepositoryInterface;
import lombok.RequiredArgsConstructor;

/**
 * Service to handle user operations.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    private final UserRepositoryInterface userRepository;

    private final ChannelService channelService;

    Logger logger = LoggerFactory.getLogger(getClass().getName());

    //
    // CREATE
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

    private User createUser(String username, String email, String password, Role role, boolean enabled) {
        User user = User.builder()
                .username(username)
                .email(email)
                .password(encodePassword(password))
                .role(role)
                .enabled(enabled)
                .build();

        return createUser(user);
    }

    private User createUser(String username, String email, String password) {
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
    // UPDATE
    //

    private User updateUser(User user) {
        return userRepository.save(user);
    }

    public User renameUser(UUID id, String username) throws UserNotFoundException, UserAlreadyExistsException {
        User user = getUserById(id);

        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException(username);
        }

        user.setUsername(username);

        return updateUser(user);
    }

    public User changeUserEmail(UUID id, String email) throws UserNotFoundException, UserAlreadyExistsException {
        User user = getUserById(id);

        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException(email);
        }

        user.setEmail(email);

        return updateUser(user);
    }

    public User changeUserPassword(UUID id, String password) throws UserNotFoundException {
        User user = getUserById(id);

        user.setPassword(encodePassword(password));

        return updateUser(user);
    }

    public User changeUserRole(UUID id, Role role) throws UserNotFoundException {
        User user = getUserById(id);

        user.setRole(role);

        return updateUser(user);
    }

    public User unlockUser(UUID id) throws UserNotFoundException {
        User user = getUserById(id);

        user.setLocked(false);

        return updateUser(user);
    }

    public User lockUser(UUID id) throws UserNotFoundException {
        User user = getUserById(id);

        user.setLocked(true);

        return updateUser(user);
    }

    public User enableUser(UUID id) throws UserNotFoundException {
        User user = getUserById(id);

        user.setEnabled(true);

        return updateUser(user);
    }

    public User disableUser(UUID id) throws UserNotFoundException {
        User user = getUserById(id);

        user.setEnabled(false);

        return updateUser(user);
    }

    //
    // AUTH
    //

    public User registerUser(UserRegisterDto registerUser) {
        User user = createUser(registerUser.getUsername(), registerUser.getEmail(), registerUser.getPassword());

        try {
            Channel generalChannel = channelService.getChannelByName("general");
            channelService.addChannelMember(generalChannel.getId(), user);
        } catch (Exception e) {
            logger.warn("Failed to add user to general channel", e);
        }

        return user;
    }

    public User loginUser(UserLoginDto loginUser) throws InvalidCredentialsException {
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

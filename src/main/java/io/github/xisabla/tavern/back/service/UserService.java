package io.github.xisabla.tavern.back.service;

import io.github.xisabla.tavern.back.dto.UserRegisterDto;
import io.github.xisabla.tavern.back.enums.Role;
import io.github.xisabla.tavern.back.exception.UserAlreadyExistsException;
import io.github.xisabla.tavern.back.exception.UserNotFoundException;
import io.github.xisabla.tavern.back.model.User;
import io.github.xisabla.tavern.back.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service to manage application users.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    /**
     * Repository for user data.
     */
    private final UserRepository userRepository;

    /**
     * Password encoder to hash and verify passwords.
     */
    private final PasswordEncoder passwordEncoder;

    //
    // Create
    //

    /**
     * Creates a new user.
     *
     * @param user The user to create.
     *
     * @return The created user.
     *
     * @throws UserAlreadyExistsException If a user with the given username or email already exists.
     */
    @Transactional
    private User createUser(final User user) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(user.getUsername());
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(user.getEmail());
        }

        return userRepository.save(user);
    }

    /**
     * Creates a new user with the given username, email, password, role, enabled, and locked status.
     *
     * @param username The username of the new user.
     * @param email    The email of the new user.
     * @param password The password of the new user. (Must be hashed)
     * @param role     The role of the new user.
     * @param enabled  The enabled status of the new user.
     * @param locked   The locked status of the new user.
     *
     * @return The created user.
     *
     * @throws UserAlreadyExistsException If a user with the given username or email already exists.
     */
    private User createUser(final String username, final String email, final String password, final Role role,
                            final boolean enabled,
                            final boolean locked
    )
        throws UserAlreadyExistsException {
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

    /**
     * Creates a new user with the given username, email, and password.
     *
     * @param username The username of the new user.
     * @param email    The email of the new user.
     * @param password The password of the new user.
     *
     * @return The created user.
     *
     * @throws UserAlreadyExistsException If a user with the given username or email already exists.
     */
    private User createUser(final String username, final String email, final String password)
        throws UserAlreadyExistsException {
        return createUser(username, email, password, Role.USER, true, false);
    }

    /**
     * Creates a new user with the given user register DTO.
     *
     * @param userRegisterDto The user register DTO.
     *
     * @return The created user.
     *
     * @throws UserAlreadyExistsException If a user with the given username or email already exists.
     */
    public User createUser(final UserRegisterDto userRegisterDto) throws UserAlreadyExistsException {
        String encodedPassword = encodePassword(userRegisterDto.getPassword());

        return createUser(userRegisterDto.getUsername(), userRegisterDto.getEmail(), encodedPassword);
    }

    //
    // Read
    //

    /**
     * Retrieves all users.
     *
     * @return A list of all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id The ID to search for.
     *
     * @return The user with the given ID.
     *
     * @throws UserNotFoundException If no user with the given ID is found.
     */
    public User getUserById(final UUID id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Retrieves a user by username.
     *
     * @param username The username to search for.
     *
     * @return The user with the given username.
     *
     * @throws UserNotFoundException If no user with the given username is found.
     */
    public User getUserByUsername(final String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                             .orElseThrow(() -> new UserNotFoundException(username));
    }

    /**
     * Retrieves a user by email.
     *
     * @param email The email to search for.
     *
     * @return The user with the given email.
     *
     * @throws UserNotFoundException If no user with the given email is found.
     */
    public User getUserByEmail(final String email) throws UserNotFoundException {
        return userRepository.findByEmail(email)
                             .orElseThrow(() -> new UserNotFoundException(email));
    }

    /**
     * Retrieves a user by login, either username or email.
     *
     * @param login The login to search for.
     *
     * @return The user with the given login.
     *
     * @throws UserNotFoundException If no user with the given login is found.
     */
    @Transactional
    public User getUserByLogin(final String login) throws UserNotFoundException {
        return userRepository.findByUsername(login)
                             .orElseGet(
                                 () -> userRepository.findByEmail(login)
                                                     .orElseThrow(() -> new UserNotFoundException(
                                                         login))
                             );
    }

    //
    // Utils
    //

    /**
     * Encodes a password.
     *
     * @param password The password to encode.
     *
     * @return The encoded password.
     */
    public String encodePassword(final String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Checks if a password matches an encoded password.
     *
     * @param password        The password to check.
     * @param encodedPassword The encoded password to check against.
     *
     * @return True if the password matches the encoded password, false otherwise.
     */
    public boolean checkPassword(final String password, final String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }
}

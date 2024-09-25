package io.github.xisabla.tavern.back.service;

import io.github.xisabla.tavern.back.dto.UserLoginDto;
import io.github.xisabla.tavern.back.dto.UserRegisterDto;
import io.github.xisabla.tavern.back.exception.InvalidCredentialsException;
import io.github.xisabla.tavern.back.exception.UserAlreadyExistsException;
import io.github.xisabla.tavern.back.exception.UserNotFoundException;
import io.github.xisabla.tavern.back.model.User;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service to manage authentication.
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    /**
     * Service to manage users.
     */
    private final UserService userService;
    /**
     * Service to manage JWT tokens.
     */
    private final JWTService jwtService;

    /**
     * Register a new user and return it.
     *
     * @param userRegisterDto User registration data
     *
     * @return User that was registered
     *
     * @throws UserAlreadyExistsException If the user already exists
     */
    public User registerUser(final UserRegisterDto userRegisterDto) throws UserAlreadyExistsException {
        return userService.createUser(userRegisterDto);
    }

    /**
     * Login the user and return it.
     *
     * @param userLoginDto User login data
     *
     * @return User that logged in
     *
     * @throws InvalidCredentialsException If the credentials are invalid
     */
    public User loginUser(final UserLoginDto userLoginDto) throws InvalidCredentialsException {
        try {
            User user = userService.getUserByLogin(userLoginDto.getLogin());

            if (!userService.checkPassword(userLoginDto.getPassword(), user.getPassword())) {
                throw new InvalidCredentialsException();
            }

            return user;
        } catch (UserNotFoundException e) {
            throw new InvalidCredentialsException();
        }
    }

    /**
     * Validate the token and return the user.
     *
     * @param token Token to validate
     *
     * @return User associated with the token
     *
     * @throws InvalidCredentialsException If the token is invalid
     */
    public User validate(final String token) throws InvalidCredentialsException {
        if (!jwtService.validateToken(token)) {
            throw new InvalidCredentialsException();
        }

        try {
            String username = jwtService.extractUsername(token);

            return userService.getUserByLogin(username);
        } catch (JwtException | IllegalArgumentException | UserNotFoundException e) {
            throw new InvalidCredentialsException();
        }
    }

}

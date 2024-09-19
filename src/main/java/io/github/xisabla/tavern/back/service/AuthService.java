package io.github.xisabla.tavern.back.service;

import io.github.xisabla.tavern.back.dto.UserLoginDto;
import io.github.xisabla.tavern.back.dto.UserRegisterDto;
import io.github.xisabla.tavern.back.exception.InvalidCredentialsException;
import io.github.xisabla.tavern.back.exception.UserAlreadyExistsException;
import io.github.xisabla.tavern.back.exception.UserNotFoundException;
import io.github.xisabla.tavern.back.model.User;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service to manage authentication.
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JWTService jwtService;

    private final PasswordEncoder passwordEncoder;

    public User registerUser(UserRegisterDto userRegisterDto) throws UserAlreadyExistsException {
        String encodedPassword = encodePassword(userRegisterDto.getPassword());

        return userService.createUser(userRegisterDto.getUsername(), userRegisterDto.getEmail(), encodedPassword);
    }

    public User loginUser(UserLoginDto userLoginDto) throws InvalidCredentialsException {
        try {
            User user = userService.getUserByLogin(userLoginDto.getLogin());

            if (!checkPassword(userLoginDto.getPassword(), user.getPassword())) {
                throw new InvalidCredentialsException();
            }
            return user;
        } catch (UserNotFoundException e) {
            throw new InvalidCredentialsException();
        }
    }

    public User validate(String token) throws InvalidCredentialsException {
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

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private boolean checkPassword(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }
}

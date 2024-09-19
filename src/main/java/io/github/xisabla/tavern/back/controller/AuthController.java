package io.github.xisabla.tavern.back.controller;

import io.github.xisabla.tavern.back.dto.UserLoginDto;
import io.github.xisabla.tavern.back.dto.UserRegisterDto;
import io.github.xisabla.tavern.back.exception.InvalidCredentialsException;
import io.github.xisabla.tavern.back.model.User;
import io.github.xisabla.tavern.back.service.AuthService;
import io.github.xisabla.tavern.back.service.CookieService;
import io.github.xisabla.tavern.back.service.JWTService;
import io.jsonwebtoken.security.InvalidKeyException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for user authentication.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JWTService jwtService;
    private final CookieService cookieService;

    //
    // Endpoints
    //

    /**
     * Registers a new user and logs them in.
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterDto userRegisterDto, @NonNull HttpServletResponse response) {
        User user = authService.registerUser(userRegisterDto);

        response.addCookie(createAuthCookie(user));

        return ResponseEntity.ok(user);
    }

    /**
     * Logs in an existing user.
     */
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody @Valid UserLoginDto userLoginDto, @NonNull HttpServletResponse response) {
        User user = authService.loginUser(userLoginDto);

        response.addCookie(createAuthCookie(user));

        return ResponseEntity.ok(user);
    }

    /**
     * Logs out the currently authenticated user.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@NonNull HttpServletResponse response) {
        Cookie authCookie = cookieService.createLogoutCookie();

        response.addCookie(authCookie);

        return ResponseEntity.ok().build();
    }

    /**
     * Validates the current user.
     * <p>
     * Makes sure the user is authenticated and returns their information.
     */
    @GetMapping("/validate")
    public ResponseEntity<User> validate(@NonNull HttpServletRequest request) {
        User user = getAuthenticatedUser(request);

        return ResponseEntity.ok(user);
    }

    /**
     * Refreshes the current user's token.
     * <p>
     * Makes sure the user is authenticated and returns their information with a new token.
     */
    @GetMapping("/refresh")
    public ResponseEntity<User> refresh(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response) {
        User user = getAuthenticatedUser(request);

        response.addCookie(createAuthCookie(user));

        return ResponseEntity.ok(user);
    }

    //
    // Helpers
    //

    /**
     * Generates the authentication token for the given user and provides its corresponding auth cookie.
     *
     * @param user User to generate the token for.
     * @return The auth cookie containing the token.
     * @throws InvalidKeyException If the key used to sign the token is invalid.
     */
    private Cookie createAuthCookie(User user) throws InvalidKeyException {
        String token = jwtService.generateToken(user);

        return cookieService.createAuthCookie(token, false);
    }

    /**
     * Retrieves the authenticated user from the request.
     *
     * @param request Request to retrieve the user from.
     * @return The authenticated user.
     * @throws InvalidCredentialsException If the cookie is not present or the token is invalid or does not correspond to a user.
     */
    private User getAuthenticatedUser(HttpServletRequest request) throws InvalidCredentialsException {
        Cookie authCookie = cookieService.getAuthCookie(request).orElseThrow(InvalidCredentialsException::new);
        String authToken = authCookie.getValue();

        return authService.validate(authToken);
    }
}

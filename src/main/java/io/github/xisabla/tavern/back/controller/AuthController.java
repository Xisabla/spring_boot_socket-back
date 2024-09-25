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
    /**
     * Service for handling user authentication.
     */
    private final AuthService authService;

    /**
     * Service for handling JWT auth tokens.
     */
    private final JWTService jwtService;

    /**
     * Service for handling auth cookies.
     */
    private final CookieService cookieService;

    //
    // Endpoints
    //

    /**
     * Registers a new user and logs them in.
     *
     * @param userRegisterDto User registration data
     * @param response        Response to add the auth cookie to.
     *
     * @return The authenticated user.
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid final UserRegisterDto userRegisterDto,
                                         @NonNull final HttpServletResponse response
    ) {
        User user = authService.registerUser(userRegisterDto);

        response.addCookie(createAuthCookie(user));

        return ResponseEntity.ok(user);
    }

    /**
     * Logs in an existing user.
     *
     * @param userLoginDto User login data
     * @param response     Response to add the auth cookie to.
     *
     * @return The authenticated user.
     */
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody @Valid final UserLoginDto userLoginDto,
                                      @NonNull final HttpServletResponse response
    ) {
        User user = authService.loginUser(userLoginDto);

        response.addCookie(createAuthCookie(user));

        return ResponseEntity.ok(user);
    }

    /**
     * Logs out the currently authenticated user.
     *
     * @param response Response to add the logout cookie to.
     *
     * @return Response entity with no content.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@NonNull final HttpServletResponse response) {
        Cookie authCookie = cookieService.createLogoutCookie();

        response.addCookie(authCookie);

        return ResponseEntity.ok().build();
    }

    /**
     * Validates the current user.
     *
     * @param request Request to retrieve the user from.
     *
     * @return The authenticated user.
     */
    @GetMapping("/validate")
    public ResponseEntity<User> validate(@NonNull final HttpServletRequest request) {
        User user = getAuthenticatedUser(request);

        return ResponseEntity.ok(user);
    }

    /**
     * Refreshes the current user's token.
     *
     * @param request  Request to retrieve the user from.
     * @param response Response to add the new token to.
     *
     * @return The refreshed user.
     */
    @GetMapping("/refresh")
    public ResponseEntity<User> refresh(@NonNull final HttpServletRequest request,
                                        @NonNull final HttpServletResponse response
    ) {
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
     *
     * @return The auth cookie containing the token.
     *
     * @throws InvalidKeyException If the key used to sign the token is invalid.
     */
    private Cookie createAuthCookie(final User user) throws InvalidKeyException {
        String token = jwtService.generateToken(user);

        return cookieService.createAuthCookie(token, false);
    }

    /**
     * Retrieves the authenticated user from the request.
     *
     * @param request Request to retrieve the user from.
     *
     * @return The authenticated user.
     *
     * @throws InvalidCredentialsException If the cookie is not present or the token is invalid or does not correspond
     *                                     to a user.
     */
    private User getAuthenticatedUser(final HttpServletRequest request) throws InvalidCredentialsException {
        Cookie authCookie = cookieService.getAuthCookie(request).orElseThrow(InvalidCredentialsException::new);
        String authToken = authCookie.getValue();

        return authService.validate(authToken);
    }
}

package io.github.xisabla.back.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.xisabla.back.dto.UserLoginDto;
import io.github.xisabla.back.dto.UserRegisterDto;
import io.github.xisabla.back.exception.NoAuthTokenException;
import io.github.xisabla.back.model.User;
import io.github.xisabla.back.service.CookieService;
import io.github.xisabla.back.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for authentication.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final CookieService cookieService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterDto registerUser,
            HttpServletResponse response) {
        User user = userService.registerUser(registerUser);
        String token = userService.createToken(user);

        Cookie cookie = cookieService.createAuthTokenCookie(token);

        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Value("${cookie.token.name}")
    private String tokenCookieName;

    @Value("${cookie.token.maxAge}")
    private int tokenCookieMaxAge;

    @Value("${cookie.token.maxAge.remember}")
    private int tokenCookieMaxAgeRemember;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody @Valid UserLoginDto loginUser,
            HttpServletResponse response) {
        User user = userService.loginUser(loginUser);
        String token = userService.createToken(user);

        int maxAge = loginUser.isRemember() ? tokenCookieMaxAgeRemember : tokenCookieMaxAge;

        response.addCookie(cookieService.createHttpOnlyCookie(tokenCookieName, token, maxAge));

        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        response.addCookie(cookieService.createHttpOnlyCookie(tokenCookieName, "", 0));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate")
    public ResponseEntity<User> validate(HttpServletRequest request) {
        String token = getAuthToken(request);

        String username = userService.extractUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        return ResponseEntity.ok(user);
    }

    //
    // Helper methods
    //

    /**
     * Get the authentication token from the request
     *
     * @param request The request to get the token from
     * @return The authentication token
     */
    private String getAuthToken(HttpServletRequest request) throws NoAuthTokenException {
        return cookieService.getAuthTokenCookie(request)
                .orElseThrow(NoAuthTokenException::new)
                .getValue();
    }

}

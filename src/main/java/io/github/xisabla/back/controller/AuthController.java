package io.github.xisabla.back.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.xisabla.back.dto.LoginUserDto;
import io.github.xisabla.back.dto.RegisterUserDto;
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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    @Value("${cookie.token.name}")
    private String tokenCookieName;

    @Value("${cookie.token.maxAge}")
    private int tokenCookieMaxAge;

    @Value("${cookie.token.maxAge.remember}")
    private int tokenCookieMaxAgeRemember;

    @Value("${cookie.token.secure}")
    private boolean tokenCookieSecure;

    private final CookieService cookieUtils;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid RegisterUserDto registerUser,
            HttpServletResponse response) {
        User user = userService.registerUser(registerUser);
        String token = userService.createToken(user);

        Cookie cookie = cookieUtils.createAuthTokenCookie(token);

        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody @Valid LoginUserDto loginUser,
            HttpServletResponse response) {
        User user = userService.loginUser(loginUser);
        String token = userService.createToken(user);

        int maxAge = loginUser.isRemember() ? tokenCookieMaxAgeRemember : tokenCookieMaxAge;

        response.addCookie(cookieUtils.createHttpOnlyCookie(tokenCookieName, token, maxAge));

        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        response.addCookie(cookieUtils.createHttpOnlyCookie(tokenCookieName, "", 0));

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
        return cookieUtils.getAuthTokenCookie(request)
                .orElseThrow(NoAuthTokenException::new)
                .getValue();
    }

}

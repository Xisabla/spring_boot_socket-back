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
import io.github.xisabla.back.model.User;
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

    @Value("${cookie.token.secure}")
    private boolean tokenCookieSecure;

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid RegisterUserDto registerUser,
            HttpServletResponse response) {
        User user = userService.registerUser(registerUser);
        String token = userService.createToken(user);

        response.addCookie(createHttpOnlyCookie(tokenCookieName, token, tokenCookieMaxAge));

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody @Valid LoginUserDto loginUser, HttpServletResponse response) {
        User user = userService.loginUser(loginUser);
        String token = userService.createToken(user);

        response.addCookie(createHttpOnlyCookie(tokenCookieName, token, tokenCookieMaxAge));

        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        response.addCookie(createHttpOnlyCookie(tokenCookieName, "", 0));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate")
    public ResponseEntity<User> validate(HttpServletRequest request) {
        Cookie tokenCookie = getTokenCookie(request);

        if (tokenCookie == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = tokenCookie.getValue();

        if (!userService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = userService.extractUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        return ResponseEntity.ok(user);
    }

    private Cookie createHttpOnlyCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);

        cookie.setHttpOnly(true);
        cookie.setSecure(tokenCookieSecure);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);

        return cookie;
    }

    private Cookie getTokenCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(tokenCookieName)) {
                return cookie;
            }
        }

        return null;
    }
}

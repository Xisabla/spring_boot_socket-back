package io.github.xisabla.back.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.xisabla.back.dto.LoginUserDto;
import io.github.xisabla.back.dto.RegisterUserDto;
import io.github.xisabla.back.dto.TokenizedUserDto;
import io.github.xisabla.back.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<TokenizedUserDto> register(@RequestBody @Valid RegisterUserDto registerUser) {
        TokenizedUserDto user = userService.registerUser(registerUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenizedUserDto> login(@RequestBody @Valid LoginUserDto loginUser) {
        TokenizedUserDto user = userService.loginUser(loginUser);

        return ResponseEntity.ok(user);
    }
}

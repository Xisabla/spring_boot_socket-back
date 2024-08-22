package io.github.xisabla.back.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.xisabla.back.model.User;
import io.github.xisabla.back.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/username")
    public List<String> listUsernames() {
        return StreamSupport.stream(userService.getAllUsers().spliterator(), false)
                .map(User::getUsername)
                .toList();
    }

    @GetMapping("/email")
    public List<String> listEmails() {
        return StreamSupport.stream(userService.getAllUsers().spliterator(), false)
                .map(User::getEmail)
                .toList();
    }

}

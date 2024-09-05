package io.github.xisabla.back.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.xisabla.back.model.User;
import io.github.xisabla.back.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for managing users.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/username")
    public ResponseEntity<List<String>> listUsernames() {
        List<String> usernames = StreamSupport.stream(userService.getAllUsers().spliterator(), false)
                .map(User::getUsername)
                .toList();

        return ResponseEntity.ok(usernames);
    }

    @GetMapping("/email")
    public ResponseEntity<List<String>> listEmails() {
        List<String> emails = StreamSupport.stream(userService.getAllUsers().spliterator(), false)
                .map(User::getEmail)
                .toList();

        return ResponseEntity.ok(emails);
    }

}

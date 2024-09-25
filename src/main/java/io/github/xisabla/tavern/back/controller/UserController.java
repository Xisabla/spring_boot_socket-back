package io.github.xisabla.tavern.back.controller;

import io.github.xisabla.tavern.back.model.User;
import io.github.xisabla.tavern.back.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Manage user resource.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    /**
     * User service to handle user operations.
     */
    private final UserService userService;

    /**
     * Get all users.
     *
     * @return List of all users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    /**
     * Get user by its username.
     *
     * @param username Username of the user
     *
     * @return User with the username
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable final String username) {
        User user = userService.getUserByUsername(username);

        return ResponseEntity.ok(user);
    }

    /**
     * Get user by its email.
     *
     * @param email Email of the user
     *
     * @return User with the email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable final String email) {
        User user = userService.getUserByEmail(email);

        return ResponseEntity.ok(user);
    }
}

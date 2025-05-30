package com.example.userservice.infrastructure.api;

import com.example.userservice.application.UserService;
import com.example.userservice.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("Intentando crear usuario con email: {}", user.getEmail());
        User created = userService.createUser(user);
        log.info("Usuario creado exitosamente con id: {}", created.getId());
        return ResponseEntity.ok(created);
    }
}

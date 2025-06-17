package com.project.warehousemanagement.controller;


import com.project.warehousemanagement.dto.LoginRequest;
import com.project.warehousemanagement.dto.LoginResponse;
import com.project.warehousemanagement.persistence.entity.User;
import com.project.warehousemanagement.persistence.service.UserService;
import com.project.warehousemanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService auth;
    private  final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest req) {
        return ResponseEntity.ok(auth.login(req));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(
                Map.of("message", "Logged out — discard the JWT client-side"));
    }

    @GetMapping("/currentUser")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = authentication.getName();

        User user = userService.getUserByUsername(username).toEntity();
        if (user == null) {

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

}
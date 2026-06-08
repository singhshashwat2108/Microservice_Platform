package com.Query.System.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ─── POST /auth/register ──────────────────────────────
    // For new users — creates an account and returns a JWT
    @PostMapping("/register")
    public ResponseEntity<AuthDto.AuthResponse> register(
            @Valid @RequestBody AuthDto.RegisterRequest request) {

        AuthDto.AuthResponse response = authService.register(request);
        return ResponseEntity.status(201).body(response);
    }

    // ─── POST /auth/login ─────────────────────────────────
    // For existing users — validates credentials and returns a JWT
    @PostMapping("/login")
    public ResponseEntity<AuthDto.AuthResponse> login(
            @Valid @RequestBody AuthDto.LoginRequest request) {

        AuthDto.AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}

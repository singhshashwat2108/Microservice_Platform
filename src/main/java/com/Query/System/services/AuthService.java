package com.Query.System.services;

import org.springframework.stereotype.Service;

import com.Query.System.utility.JwtUtil;

@Service
public class AuthService {

    private final UserRepository        userRepository;
    private final PasswordEncoder       passwordEncoder;
    private final JwtUtil               jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager) {
        this.userRepository        = userRepository;
        this.passwordEncoder       = passwordEncoder;
        this.jwtUtil               = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    // ─── Register a new user ──────────────────────────────
    public AuthDto.AuthResponse register(AuthDto.RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already taken.");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername());
        return new AuthDto.AuthResponse(token, user.getUsername(),
                "Account created successfully.");
    }

    // ─── Login an existing user ───────────────────────────
    public AuthDto.AuthResponse login(AuthDto.LoginRequest request) {

        // AuthenticationManager will throw if credentials are wrong
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String token = jwtUtil.generateToken(request.getUsername());
        return new AuthDto.AuthResponse(token, request.getUsername(),
                "Login successful.");
    }
}
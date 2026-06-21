package com.queryhub.auth.service;

import com.queryhub.auth.dto.AuthDto;
import com.queryhub.auth.entity.Role;
import com.queryhub.auth.entity.User;
import com.queryhub.auth.repository.UserRepository;
import com.queryhub.common.security.JwtPrincipal;
import com.queryhub.common.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public AuthDto.AuthResponse register(AuthDto.RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already taken.");
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()
                && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered.");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        log.info("User registered: username={}", user.getUsername());

        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole().name());
        return new AuthDto.AuthResponse(token, user.getId(), user.getUsername(),
                user.getRole().name(), "Account created successfully.");
    }

    public AuthDto.AuthResponse login(AuthDto.LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        log.info("User logged in: username={}", user.getUsername());

        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole().name());
        return new AuthDto.AuthResponse(token, user.getId(), user.getUsername(),
                user.getRole().name(), "Login successful.");
    }

    public AuthDto.ProfileResponse getProfile(JwtPrincipal principal) {
        User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        return new AuthDto.ProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }

    public AuthDto.TokenValidationResponse validateToken(String token) {
        if (!jwtUtil.isTokenValid(token)) {
            return new AuthDto.TokenValidationResponse(false, null, null, null);
        }
        return new AuthDto.TokenValidationResponse(
                true,
                jwtUtil.extractUserId(token),
                jwtUtil.extractUsername(token),
                jwtUtil.extractRole(token)
        );
    }
}

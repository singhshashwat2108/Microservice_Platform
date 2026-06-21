package com.queryhub.auth.service;

import com.queryhub.auth.dto.AuthDto;
import com.queryhub.auth.entity.User;
import com.queryhub.auth.repository.UserRepository;
import com.queryhub.common.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_rejectsDuplicateUsername() {
        AuthDto.RegisterRequest request = new AuthDto.RegisterRequest();
        request.setUsername("alice");
        request.setPassword("secret1");

        when(userRepository.existsByUsername("alice")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }

    @Test
    void register_createsUserAndReturnsToken() {
        AuthDto.RegisterRequest request = new AuthDto.RegisterRequest();
        request.setUsername("alice");
        request.setPassword("secret1");

        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(passwordEncoder.encode("secret1")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User user = inv.getArgument(0);
            user.setId(1L);
            return user;
        });
        when(jwtUtil.generateToken("alice", 1L, "USER")).thenReturn("token-123");

        AuthDto.AuthResponse response = authService.register(request);

        assertEquals("token-123", response.getToken());
        assertEquals("alice", response.getUsername());
        verify(userRepository).save(any(User.class));
    }
}

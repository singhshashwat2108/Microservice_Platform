package com.queryhub.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class AuthDto {

    @Data
    public static class RegisterRequest {
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be 3-50 characters")
        private String username;

        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;
    }

    @Data
    public static class LoginRequest {
        @NotBlank(message = "Username is required")
        private String username;

        @NotBlank(message = "Password is required")
        private String password;
    }

    @Data
    public static class AuthResponse {
        private String token;
        private Long userId;
        private String username;
        private String role;
        private String message;

        public AuthResponse(String token, Long userId, String username, String role, String message) {
            this.token = token;
            this.userId = userId;
            this.username = username;
            this.role = role;
            this.message = message;
        }
    }

    @Data
    public static class ProfileResponse {
        private Long id;
        private String username;
        private String email;
        private String role;
        private java.time.LocalDateTime createdAt;

        public ProfileResponse(Long id, String username, String email, String role,
                               java.time.LocalDateTime createdAt) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.role = role;
            this.createdAt = createdAt;
        }
    }

    @Data
    public static class TokenValidationResponse {
        private boolean valid;
        private Long userId;
        private String username;
        private String role;

        public TokenValidationResponse(boolean valid, Long userId, String username, String role) {
            this.valid = valid;
            this.userId = userId;
            this.username = username;
            this.role = role;
        }
    }
}

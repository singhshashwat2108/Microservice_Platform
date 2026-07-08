package com.queryhub.common.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtPrincipal {
    private Long userId;
    private String username;
    private String role;
}

package com.queryhub.query.config;

import com.queryhub.common.security.JwtAuthFilter;
import com.queryhub.common.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/query/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/category/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/comments/query/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/likes/*/count").permitAll()
                        .requestMatchers(HttpMethod.POST, "/category").authenticated()
                        .requestMatchers(HttpMethod.POST, "/query").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/query/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/query/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/comments").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/comments/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/likes/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/likes/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

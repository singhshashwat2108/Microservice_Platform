package com.Query.System.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration                   
public class Securityconfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    public Securityconfig(JwtAuthFilter jwtAuthFilter, UserDetailsService userDetailsService) {
        this.jwtAuthFilter      = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                
                .requestMatchers("/", "/index.html").permitAll()
                .requestMatchers("/**.html").permitAll()        // allow all HTML pages
                .requestMatchers("/**.css").permitAll()         // allow shared.css
                .requestMatchers("/**.js").permitAll()          // allow any JS files
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/auth/register").permitAll()
                 
                .requestMatchers(HttpMethod.GET, "/view").permitAll()
                .requestMatchers(HttpMethod.GET, "/view/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/category").permitAll()
                .requestMatchers(HttpMethod.GET, "/category/**").permitAll()
                
                .requestMatchers(HttpMethod.POST, "/Query/*/comment").permitAll()
                .requestMatchers(HttpMethod.GET,  "/Query/*/comment").permitAll()

                
                .requestMatchers(HttpMethod.POST, "/Query/*/like").permitAll()
                .requestMatchers(HttpMethod.GET,  "/Query/*/like").permitAll()
 
                .requestMatchers(HttpMethod.POST, "/Query/*/comment").permitAll()
                .requestMatchers(HttpMethod.POST, "/Query/*/like").permitAll()

    
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain staticResources(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/**.html", "/**.css", "/**.js", "/favicon.ico")
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
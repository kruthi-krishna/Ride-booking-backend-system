package com.ridesharing.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration.
 *
 * NOTE: For simplicity, this fresher-friendly version disables CSRF and
 * permits all requests, while still exposing a BCryptPasswordEncoder bean so
 * that user/driver passwords are hashed before being persisted.
 *
 * To harden this for production, wire in a JWT filter here (see README's
 * "Future Enhancements" section) and replace permitAll() with rules such as:
 *   .requestMatchers("/users/register", "/users/login",
 *                     "/drivers/register", "/drivers/login").permitAll()
 *   .requestMatchers("/admin/**").hasRole("ADMIN")
 *   .anyRequest().authenticated()
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }
}

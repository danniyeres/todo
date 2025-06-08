package org.example.todo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todo.dto.AuthRequest;
import org.example.todo.dto.AuthResponse;
import org.example.todo.model.User;
import org.example.todo.repository.UserRepository;
import org.example.todo.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(AuthRequest authRequest) {
        if (userRepository.existsByUsername(authRequest.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        var user = User.builder()
                .username(authRequest.getUsername())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .build();
        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(authRequest.getUsername());
        String refreshToken = jwtService.generateRefreshToken(authRequest.getUsername());

        log.info("User {} registered successfully", authRequest.getUsername());
        return new AuthResponse(accessToken, refreshToken);

    }

    public AuthResponse login(AuthRequest authRequest) {
        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String accessToken = jwtService.generateAccessToken(user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        log.info("User {} logged in successfully", user.getUsername());
        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);

        if (username == null) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (jwtService.isTokenValid(refreshToken, user.getUsername())) {
            throw new IllegalArgumentException("Refresh token is expired");
        }
        String accessToken = jwtService.generateAccessToken(user.getUsername());

        log.info("User {} refreshed token", user.getUsername());
        return new AuthResponse(accessToken, refreshToken);
    }
}

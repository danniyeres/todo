package org.example.todo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todo.dto.AuthRequest;
import org.example.todo.dto.AuthResponse;
import org.example.todo.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping ("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest authRequest) {
        try {
            if (authRequest.getUsername() == null || authRequest.getPassword() == null) {
                log.error("Username or password is null");
                return ResponseEntity.badRequest().body(new AuthResponse(null, null));
            }
            var authResponse = authService.register(authRequest);
            return ResponseEntity.ok(authResponse);
        } catch (IllegalArgumentException e) {
            log.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new AuthResponse(null, null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            if (authRequest.getUsername() == null || authRequest.getPassword() == null) {
                log.error("Username or password is null");
                return ResponseEntity.badRequest().body(new AuthResponse(null, null));
            }
            var authResponse = authService.login(authRequest);
            return ResponseEntity.ok(authResponse);
        } catch (IllegalArgumentException e) {
            log.error("Login failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new AuthResponse(null, null));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody String refreshToken) {
        try {
            if (refreshToken == null) {
                log.error("Refresh token is null");
                return ResponseEntity.badRequest().body(new AuthResponse(null, null));
            }
            var authResponse = authService.refreshToken(refreshToken);
            return ResponseEntity.ok(authResponse);
        } catch (IllegalArgumentException e) {
            log.error("Refresh token failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new AuthResponse(null, null));
        } catch (RuntimeException e) {
            log.error("Error during refresh token: {}", e.getMessage());
            return ResponseEntity.status(500).body(new AuthResponse(null, null));
        }
    }

    @GetMapping
    public ResponseEntity<String> healthCheck() {
        log.info("Health check endpoint hit");
        return ResponseEntity.ok("Service is running");
    }
}

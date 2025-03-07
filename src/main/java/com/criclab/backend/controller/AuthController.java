package com.criclab.backend.controller;

import com.criclab.backend.dto.AuthRequest;
import com.criclab.backend.dto.LogoutResponse;
import com.criclab.backend.dto.RefreshTokenRequest;
import com.criclab.backend.dto.JwtResponse;
import com.criclab.backend.entity.User;
import com.criclab.backend.repository.UserRepository;
import com.criclab.backend.services.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Admin Authentication", description = "Endpoints for authentication")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody AuthRequest request) {
        try {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser.isPresent()) {
                return ResponseEntity.badRequest().body("Email is already in use.");
            }

            String accessToken = jwtService.generateToken(request.getEmail(), true);
            String refreshToken = jwtService.generateToken(request.getEmail(), false);

            User newUser = new User();
            newUser.setEmail(request.getEmail());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setRefreshToken(refreshToken);

            userRepository.save(newUser);

            JwtResponse jwtResponse = new JwtResponse(accessToken, refreshToken, newUser.getEmail());
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            // e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        String accessToken = jwtService.generateToken(request.getEmail(), true);
        String refreshToken = jwtService.generateToken(request.getEmail(), false);
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        user.get().setRefreshToken(refreshToken);
        JwtResponse jwtResponse = new JwtResponse(accessToken, refreshToken, user.get().getEmail());
        return ResponseEntity.ok().body(jwtResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        if (!jwtService.validateToken(request.refreshToken())) {
            return ResponseEntity.badRequest().body("Invalid refresh token");
        }

        String email = jwtService.getUsernameFromToken(request.refreshToken());
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (user.get().getRefreshToken() == null || !user.get().getRefreshToken().equals(request.refreshToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        String accessToken = jwtService.generateToken(email, true);
        String refreshToken = jwtService.generateToken(email, false);
        user.get().setRefreshToken(refreshToken);
        JwtResponse jwtResponse = new JwtResponse(accessToken, refreshToken, email);
        return ResponseEntity.ok().body(jwtResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody RefreshTokenRequest request) {
        if (!jwtService.validateToken(request.refreshToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
        String email = jwtService.getUsernameFromToken(request.refreshToken());

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        user.setRefreshToken(null);
        userRepository.save(user);

        LogoutResponse logoutResponse = new LogoutResponse("Logged out successfully");
        return ResponseEntity.ok(logoutResponse);
    }
}

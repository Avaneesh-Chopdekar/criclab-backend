package com.criclab.backend.controller;

import com.criclab.backend.dto.AuthRequest;
import com.criclab.backend.dto.RefreshTokenRequest;
import com.criclab.backend.dto.JwtResponse;
import com.criclab.backend.entity.User;
import com.criclab.backend.repository.UserRepository;
import com.criclab.backend.services.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        String accessToken = jwtService.generateToken(request.getEmail(), true);
        String refreshToken = jwtService.generateToken(request.getEmail(), false);
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        JwtResponse jwtResponse = new JwtResponse(accessToken, refreshToken, user.get().getEmail());
        return ResponseEntity.ok().body(jwtResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        if (!jwtService.validateToken(request.refreshToken())) {
            return ResponseEntity.badRequest().body("Invalid refresh token");
        }

        String email = jwtService.getUsernameFromToken(request.refreshToken());
        String accessToken = jwtService.generateToken(email, true);
        String refreshToken = jwtService.generateToken(email, false);
        JwtResponse jwtResponse = new JwtResponse(accessToken, refreshToken, email);
        return ResponseEntity.ok().body(jwtResponse);
    }
}

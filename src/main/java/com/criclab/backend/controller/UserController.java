package com.criclab.backend.controller;

import com.criclab.backend.dto.JwtResponse;
import com.criclab.backend.dto.UserAndJwtResponse;
import com.criclab.backend.entity.User;
import com.criclab.backend.repository.UserRepository;
import com.criclab.backend.services.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User Management", description = "Endpoints for user management")
public class UserController {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String email = jwtService.getUsernameFromToken(token);
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (user.get().getRefreshToken() == null || user.get().getRefreshToken().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String accessToken = jwtService.generateToken(email, true);
        String refreshToken = jwtService.generateToken(email, false);
        UserAndJwtResponse response = new UserAndJwtResponse(accessToken, refreshToken, user.get().getEmail(), user.get().isSuperAdmin(), user.get().isEnabled());
        return ResponseEntity.ok(response);
    }
}
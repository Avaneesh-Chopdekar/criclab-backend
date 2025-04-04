package com.criclab.backend.controller;

import com.criclab.backend.entity.Match;
import com.criclab.backend.entity.User;
import com.criclab.backend.repository.UserRepository;
import com.criclab.backend.services.JwtService;
import com.criclab.backend.services.MatchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/matches")
@Tag(name = "Matches", description = "Endpoints for managing matches")
public class MatchController {

    private final MatchService matchService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches() {
        List<Match> matches = matchService.getAllMatches();
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Match>> getAllActiveMatches() {
        List<Match> matches = matchService.getAllActiveMatches();
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/live")
    public ResponseEntity<List<Match>> getLiveMatches() {
        List<Match> matches = matchService.getLiveMatches();
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/ict-point-table")
    public ResponseEntity<List<List<String>>> getICTPointTable() {
        List<List<String>> pointTable = matchService.getICTPointTable();
        return ResponseEntity.ok(pointTable);
    }

    @GetMapping("/ipl-point-table")
    public ResponseEntity<List<List<String>>> getIPLPointTable() {
        List<List<String>> pointTable = matchService.getIPLPointTable();
        return ResponseEntity.ok(pointTable);
    }

    @DeleteMapping("/{id}/soft-delete")
    public ResponseEntity<?> softDeleteMatch(@PathVariable Long id, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String email = jwtService.getUsernameFromToken(token);
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (user.get().getRefreshToken() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        boolean deleted = matchService.softDeleteMatch(id);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Match not found");
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteMatch(@PathVariable Long id, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String email = jwtService.getUsernameFromToken(token);
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (user.get().getRefreshToken() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        boolean deleted = matchService.deleteMatch(id);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Match not found");
        }

        return ResponseEntity.noContent().build();
    }
}

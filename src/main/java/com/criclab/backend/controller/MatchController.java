package com.criclab.backend.controller;

import com.criclab.backend.entity.Match;
import com.criclab.backend.services.MatchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/matches")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Matches", description = "Endpoints for managing matches")
public class MatchController {

    private final MatchService matchService;

    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches() {
        List<Match> matches = matchService.getAllMatches();
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/live")
    public ResponseEntity<List<Match>> getLiveMatches() {
        List<Match> matches = matchService.getLiveMatches();
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/point-table")
    public ResponseEntity<List<List<String>>> getPointTable() {
        List<List<String>> pointTable = matchService.getPointTable();
        return ResponseEntity.ok(pointTable);
    }
}

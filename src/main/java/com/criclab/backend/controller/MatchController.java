package com.criclab.backend.controller;

import com.criclab.backend.entity.Match;
import com.criclab.backend.services.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/matches")
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
}

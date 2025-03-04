package com.criclab.backend.services.impl;

import com.criclab.backend.entity.Match;
import com.criclab.backend.repository.MatchRepository;
import com.criclab.backend.services.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;

    /**
     * Gets all matches in database, stored as match history
     *
     * @return A list of all matches.
     */
    @Override
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public List<Match> getLiveMatches() {
        return List.of();
    }

    @Override
    public List<Map<String, String>> getPointTable() {
        return List.of();
    }
}

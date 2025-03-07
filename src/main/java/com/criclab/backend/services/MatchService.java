package com.criclab.backend.services;

import com.criclab.backend.entity.Match;

import java.util.List;
import java.util.Map;

public interface MatchService {

    List<Match> getAllMatches();

    List<Match> getLiveMatches();

    List<List<String>> getPointTable();

    boolean softDeleteMatch(Long id);

    boolean deleteMatch(Long id);
}

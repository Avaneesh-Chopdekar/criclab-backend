package com.criclab.backend.services;

import com.criclab.backend.entity.Match;

import java.util.List;

public interface MatchService {

    List<Match> getAllMatches();

    List<Match> getAllActiveMatches();

    List<Match> getLiveMatches();

    List<List<String>> getICTPointTable();

    List<List<String>> getIPLPointTable();

    boolean softDeleteMatch(Long id);

    boolean deleteMatch(Long id);
}

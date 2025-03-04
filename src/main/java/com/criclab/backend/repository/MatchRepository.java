package com.criclab.backend.repository;

import com.criclab.backend.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    Optional<Match> findByTeamHeading(String teamHeading);
}

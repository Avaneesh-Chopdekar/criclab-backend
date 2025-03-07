package com.criclab.backend.repository;

import com.criclab.backend.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    Optional<Match> findByTeamHeading(String teamHeading);


    @Query("SELECT m FROM Match m WHERE m.deletedAt IS NULL ORDER BY id DESC")
    List<Match> findAllActiveMatches();
}

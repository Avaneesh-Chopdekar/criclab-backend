package com.criclab.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "cricket_match")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teamHeading;
    private String matchNumberVenue;
    private String battingTeam;
    private String battingTeamScore;
    private String bowlingTeam;
    private String bowlingTeamScore;
    private String liveText;
    private String matchLink;
    private String textComplete;
    private MatchStatus status;
    private Date date = new Date();
    private Date deletedAt;

    public void setMatchStatus() {
        if (textComplete.isEmpty()) {
            status = MatchStatus.LIVE;
        } else {
            status = MatchStatus.COMPLETED;
        }
    }
}

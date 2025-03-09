package com.criclab.backend.services.impl;

import com.criclab.backend.entity.Match;
import com.criclab.backend.repository.MatchRepository;
import com.criclab.backend.services.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

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
        return matchRepository.findAllByOrderByIdDesc();
    }


    /**
     * Gets all active matches where deletedAt is null.
     *
     * @return A list of all active matches.
     */
    @Override
    public List<Match> getAllActiveMatches() {
        return matchRepository.findAllActiveMatches();
    }

    /**
     * Gets all live matches
     *
     * @return A list of all live matches.
     */
    @Override
    @Scheduled(fixedRate = 60000)
    public List<Match> getLiveMatches() {
        List<Match> matches = new ArrayList<>();
        try {
            String url = "https://www.cricbuzz.com/cricket-match/live-scores";
            Document document = Jsoup.connect(url).get();
            Elements liveScoreElements = document.select("div.cb-mtch-lst.cb-tms-itm");
            for (Element match : liveScoreElements) {
                String teamsHeading = match.select("h3.cb-lv-scr-mtch-hdr").select("a").text();
                String matchNumberVenue = match.select("span").text();
                Elements matchBatTeamInfo = match.select("div.cb-hmscg-bat-txt");
                String battingTeam = matchBatTeamInfo.select("div.cb-hmscg-tm-nm").text();
                String score = matchBatTeamInfo.select("div.cb-hmscg-tm-nm+div").text();
                Elements bowlTeamInfo = match.select("div.cb-hmscg-bwl-txt");
                String bowlTeam = bowlTeamInfo.select("div.cb-hmscg-tm-nm").text();
                String bowlTeamScore = bowlTeamInfo.select("div.cb-hmscg-tm-nm+div").text();
                String textLive = match.select("div.cb-text-live").text();
                String textComplete = match.select("div.cb-text-complete").text();
                //getting match link
                String matchLink = match.select("a.cb-lv-scrs-well.cb-lv-scrs-well-live").attr("href");

                Match match1 = new Match();
                match1.setTeamHeading(teamsHeading);
                match1.setMatchNumberVenue(matchNumberVenue);
                match1.setBattingTeam(battingTeam);
                match1.setBattingTeamScore(score);
                match1.setBowlingTeam(bowlTeam);
                match1.setBowlingTeamScore(bowlTeamScore);
                match1.setLiveText(textLive);
                match1.setMatchLink(matchLink);
                match1.setTextComplete(textComplete);
                match1.setMatchStatus();

                matches.add(match1);

                updateMatchInDb(match1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matches;
    }

    private void updateMatchInDb(Match match) {
        Match existingMatch = matchRepository.findByTeamHeading(match.getTeamHeading()).orElse(null);
        if (existingMatch == null) {
            matchRepository.save(match);
        } else {
            match.setId(existingMatch.getId());
            matchRepository.save(match);
        }
    }

    /**
     * Returns the point table of the given tournament (currently only Champions Trophy 2025)
     * The point table is represented as a list of lists of strings, where the first list contains the headers
     * The rest of the lists contain the points for each team, with the team name as the first element
     * and the points as the subsequent elements.
     *
     * @return A list of lists of strings, representing the point table of the given tournament.
     */
    @Override
    public List<List<String>> getPointTable() {
        List<List<String>> pointTable = new ArrayList<>();
        String tableURL = "https://www.cricbuzz.com/cricket-series/9325/icc-champions-trophy-2025/points-table";
        try {
            Document document = Jsoup.connect(tableURL).get();
            Element groupB = document.select("table.cb-srs-pnts").first();
            assert groupB != null;
            Elements groupBHeads = groupB.select("thead>tr>*");
            List<String> groupBHeaders = new ArrayList<>();
            groupBHeads.forEach((element) -> groupBHeaders.add(element.text()));
            pointTable.add(groupBHeaders);
            Elements groupBRows = groupB.select("tbody>*");
            groupBRows.forEach(tr -> {
                List<String> points = new ArrayList<>();
                if (tr.hasAttr("class")) {
                    Elements tds = tr.select("td");
                    String team = tds.getFirst().select("div.cb-col-84").text();
                    points.add(team);
                    tds.forEach(td -> {
                        if (!td.hasClass("cb-srs-pnts-name")) {
                            points.add(td.text());
                        }
                    });
//                    System.out.println(points);
                    pointTable.add(points);
                }
            });

            Element groupA = document.select("table.cb-srs-pnts").last();
            assert groupA != null;
            Elements groupAHeads = groupA.select("thead>tr>*");
            List<String> groupAHeaders = new ArrayList<>();
            groupAHeads.forEach(element -> groupAHeaders.add(element.text()));
            pointTable.add(groupAHeaders);
            Elements groupARows = groupA.select("tbody>*");

            groupARows.forEach(tr -> {
                List<String> points = new ArrayList<>();
                if (tr.hasAttr("class")) {
                    Elements tds = tr.select("td");
                    String team = tds.getFirst().select("div.cb-col-84").text();
                    points.add(team);
                    tds.forEach(td -> {
                        if (!td.hasClass("cb-srs-pnts-name")) {
                            points.add(td.text());
                        }
                    });
//                    System.out.println(points);
                    pointTable.add(points);
                }
            });

            System.out.println(pointTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pointTable;
    }

    @Override
    public boolean softDeleteMatch(Long id) {
        Match existingMatch = matchRepository.findById(id).orElse(null);
        if (existingMatch == null) {
            return false;
        }

        existingMatch.setDeletedAt(new Date(System.currentTimeMillis()));
        matchRepository.save(existingMatch);
        return true;
    }

    @Override
    public boolean deleteMatch(Long id) {
        Match existingMatch = matchRepository.findById(id).orElse(null);
        if (existingMatch == null) {
            return false;
        }
        matchRepository.delete(existingMatch);
        return true;
    }
}

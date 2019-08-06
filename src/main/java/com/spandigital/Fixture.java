package com.spandigital;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Fixture {

    private String homeTeam;
    private int scoreFor;
    private String awayTeam;
    private int scoreAgainst;

    public Fixture(String homeTeam, int scoreFor, String awayTeam, int scoreAgainst) {
        this.homeTeam = homeTeam;
        this.scoreFor = scoreFor;
        this.awayTeam = awayTeam;
        this.scoreAgainst = scoreAgainst;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public int getScoreFor() {
        return scoreFor;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public int getScoreAgainst() {
        return scoreAgainst;
    }

    public List<String> getTeams() {
        return Arrays.asList(homeTeam, awayTeam);
    }

    public String getWinningTeam() {
        if (scoreFor == scoreAgainst) {
            return null;
        }

        return scoreFor > scoreAgainst ? homeTeam : awayTeam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fixture fixture = (Fixture) o;
        return scoreFor == fixture.scoreFor &&
                scoreAgainst == fixture.scoreAgainst &&
                Objects.equals(homeTeam, fixture.homeTeam) &&
                Objects.equals(awayTeam, fixture.awayTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeam, scoreFor, awayTeam, scoreAgainst);
    }
}

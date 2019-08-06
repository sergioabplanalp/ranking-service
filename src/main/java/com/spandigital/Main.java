package com.spandigital;

import java.util.*;

import static com.spandigital.ExtractionUtil.*;

public class Main {

    private static List<Team> ranking = new ArrayList<>();

    public static List<Team> getRanking() {
        return ranking;
    }

    public static void setRanking(List<Team> ranking) {
        Main.ranking = ranking;
    }

    public static void main(String[] args) {
        System.out.println("Enter results ('X' to exit): ");

        Scanner scanner = new Scanner(System.in);

        String result;
        do {
            result = scanner.nextLine();
            if ("x".equalsIgnoreCase(result)) {
                continue;
            }

            Fixture fixture = parse(result);
            addTeamsToTable(fixture.getTeams());
            rankTeams(fixture);
        } while (!"x".equalsIgnoreCase(result));

        sortAndPrintRanking();
    }

    static Fixture parse(String result) {
        String[] resultSplit = result.split(",");

        String homeResult = resultSplit[0].trim();
        String homeTeam = extractTeamName(homeResult);
        int homeScore = extractScore(homeResult);

        String awayResult = resultSplit[1].trim();
        String awayTeam = extractTeamName(awayResult);
        int awayScore = extractScore(awayResult);

        return new Fixture(homeTeam, homeScore, awayTeam, awayScore);
    }

    static void addTeamsToTable(List<String> teams) {
        teams.forEach(teamName -> {
            Team team = new Team(teamName);
            if (ranking.contains(team)) {
                return;
            }

            ranking.add(team);
        });
    }

    static void rankTeams(Fixture fixture) {
        if (fixture.getWinningTeam() == null) {
            ranking.stream().filter(team -> Arrays.asList(fixture.getHomeTeam(), fixture.getAwayTeam()).contains(team.getName())).forEach(Team::addDraw);
            return;
        }

        Team team = ranking.stream().filter(t -> t.getName().equals(fixture.getWinningTeam())).findFirst().orElseThrow(IllegalArgumentException::new);
        team.addWin();
    }

    static void sortAndPrintRanking() {
        ranking.sort(Comparator.comparing(Team::getPoints).reversed().thenComparing(Team::getName));

        int previousTeamPoints = -1;
        int skip = 0;
        int position = 0;
        for (Team team : ranking) {
            if (team.getPoints() == previousTeamPoints) {
                skip++;
            } else {
                position += skip + 1;
                skip = 0;
            }

            System.out.println(String.format("%s. %s, %s pts", position, team.getName(), team.getPoints()));

            previousTeamPoints = team.getPoints();
        }
    }
}

package com.spandigital;

public class ExtractionUtil {
    public static String extractTeamName(String teamResult) {
        return teamResult.substring(0, teamResult.lastIndexOf(' '));
    }

    public static int extractScore(String teamResult) {
        return Integer.parseInt(teamResult.substring(teamResult.lastIndexOf(' ') + 1));
    }
}

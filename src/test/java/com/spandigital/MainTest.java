package com.spandigital;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainTest {

    private static final String TEAM_A = "Team A";
    private static final String TEAM_B = "Team B";
    private static final String TEAM_C = "Team C";
    private static final String TEAM_D = "Team D";
    private static final String TEAM_E = "Team E";
    private static final String TEAM_F = "Team F";

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @After
    public void tearDown() {
        System.setOut(System.out);
    }

    @Test
    public void parseFixtureInformation() {
        Fixture expected = new Fixture(TEAM_A, 2, TEAM_B, 1);
        Fixture actual = Main.parse("Team A 2, Team B 1");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addTeamsToTable_noExistingTeams() {
        Main.setRanking(new ArrayList<>());

        Main.addTeamsToTable(Arrays.asList(TEAM_A, TEAM_B));

        Assert.assertEquals(2, Main.getRanking().size());

        Team homeTeam = Main.getRanking().stream().filter(t -> t.getName().equals(TEAM_A)).findFirst().orElseThrow(IllegalArgumentException::new);
        Assert.assertNotNull(homeTeam);
        Assert.assertEquals(0, homeTeam.getPoints());

        Team awayTeam = Main.getRanking().stream().filter(t -> t.getName().equals(TEAM_B)).findFirst().orElseThrow(IllegalArgumentException::new);
        Assert.assertNotNull(awayTeam);
        Assert.assertEquals(0, awayTeam.getPoints());
    }

    @Test
    public void addTeamsToTable_existingTeam() {
        List<Team> teams = new ArrayList<>();
        teams.add(new Team(TEAM_A, 4));
        Main.setRanking(teams);

        Main.addTeamsToTable(Arrays.asList(TEAM_A, TEAM_B));

        Assert.assertEquals(2, Main.getRanking().size());

        Team homeTeam = Main.getRanking().stream().filter(t -> t.getName().equals(TEAM_A)).findFirst().orElseThrow(IllegalArgumentException::new);
        Assert.assertNotNull(homeTeam);
        Assert.assertEquals(4, homeTeam.getPoints());

        Team awayTeam = Main.getRanking().stream().filter(t -> t.getName().equals(TEAM_B)).findFirst().orElseThrow(IllegalArgumentException::new);
        Assert.assertNotNull(awayTeam);
        Assert.assertEquals(0, awayTeam.getPoints());
    }

    @Test
    public void rankTeams_teamsExist_pointsAllocatedForWin() {
        List<Team> teams = new ArrayList<>();
        teams.add(new Team(TEAM_A));
        teams.add(new Team(TEAM_B));
        Main.setRanking(teams);

        Fixture fixture = new Fixture(TEAM_A, 2, TEAM_B, 1);
        Main.rankTeams(fixture);

        Team homeTeam = Main.getRanking().stream().filter(t -> t.getName().equals(TEAM_A)).findFirst().orElseThrow(IllegalArgumentException::new);
        Assert.assertNotNull(homeTeam);
        Assert.assertEquals(3, homeTeam.getPoints());

        Team awayTeam = Main.getRanking().stream().filter(t -> t.getName().equals(TEAM_B)).findFirst().orElseThrow(IllegalArgumentException::new);
        Assert.assertNotNull(awayTeam);
        Assert.assertEquals(0, awayTeam.getPoints());
    }

    @Test
    public void rankTeams_teamsExist_pointsAllocatedForDraw() {
        List<Team> teams = new ArrayList<>();
        teams.add(new Team(TEAM_A));
        teams.add(new Team(TEAM_B));
        Main.setRanking(teams);

        Fixture fixture = new Fixture(TEAM_A, 3, TEAM_B, 3);
        Main.rankTeams(fixture);

        Team homeTeam = Main.getRanking().stream().filter(t -> t.getName().equals(TEAM_A)).findFirst().orElseThrow(IllegalArgumentException::new);
        Assert.assertNotNull(homeTeam);
        Assert.assertEquals(1, homeTeam.getPoints());

        Team awayTeam = Main.getRanking().stream().filter(t -> t.getName().equals(TEAM_B)).findFirst().orElseThrow(IllegalArgumentException::new);
        Assert.assertNotNull(awayTeam);
        Assert.assertEquals(1, awayTeam.getPoints());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rankTeams_teamsDontExist() {
        Main.setRanking(new ArrayList<>());

        Fixture fixture = new Fixture(TEAM_A, 2, TEAM_B, 1);
        Main.rankTeams(fixture);
    }

    @Test
    public void rankingPrintOut_standardTable() {
        List<Team> teams = new ArrayList<>();
        teams.add(new Team(TEAM_A, 4));
        teams.add(new Team(TEAM_B, 3));
        teams.add(new Team(TEAM_C, 2));
        teams.add(new Team(TEAM_D, 1));
        Main.setRanking(teams);

        Main.sortAndPrintRanking();

        String expected = "1. Team A, 4 pts\n2. Team B, 3 pts\n3. Team C, 2 pts\n4. Team D, 1 pts\n";
        String actual = outputStream.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void rankingPrintOut_alphabetiseSamePoints() {
        List<Team> teams = new ArrayList<>();
        teams.add(new Team(TEAM_D, 1));
        teams.add(new Team(TEAM_B, 3));
        teams.add(new Team(TEAM_A, 1));
        teams.add(new Team(TEAM_C, 2));
        Main.setRanking(teams);

        Main.sortAndPrintRanking();

        String expected = "1. Team B, 3 pts\n2. Team C, 2 pts\n3. Team A, 1 pts\n3. Team D, 1 pts\n";
        String actual = outputStream.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void rankingPrintOut_verifyPositioning() {
        List<Team> teams = new ArrayList<>();
        teams.add(new Team(TEAM_A, 1));
        teams.add(new Team(TEAM_B, 0));
        teams.add(new Team(TEAM_C, 1));
        teams.add(new Team(TEAM_D, 1));
        teams.add(new Team(TEAM_E, 6));
        teams.add(new Team(TEAM_F, 1));
        Main.setRanking(teams);

        Main.sortAndPrintRanking();

        String expected = "1. Team E, 6 pts\n2. Team A, 1 pts\n2. Team C, 1 pts\n2. Team D, 1 pts\n2. Team F, 1 pts\n6. Team B, 0 pts\n";
        String actual = outputStream.toString();
        Assert.assertEquals(expected, actual);
    }
}

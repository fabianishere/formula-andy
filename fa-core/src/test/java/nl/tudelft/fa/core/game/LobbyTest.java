package nl.tudelft.fa.core.game;

import nl.tudelft.fa.core.race.GrandPrix;
import nl.tudelft.fa.core.team.Team;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class LobbyTest {

    List<Team> teams;
    List<GrandPrix> shedule;
    int currentGrandPrix;

    Lobby lobby;

    @Before
    public void setUp() {
        teams = new ArrayList<Team>();
        shedule = new ArrayList<GrandPrix>();
        currentGrandPrix = 0;
        lobby = new Lobby(teams, shedule);
    }

    @Test
    public void getTeams() {
        assertTrue(teams == lobby.getTeams());
    }

    @Test
    public void getShedule() {
        assertTrue(shedule == lobby.getShedule());
    }

    @Test
    public void getCurrentGrandPrix() {
        assertTrue(currentGrandPrix == lobby.getCurrentGrandPrix());
    }
}

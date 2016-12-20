package nl.tudelft.fa.core.game;

import nl.tudelft.fa.core.race.GrandPrix;
import nl.tudelft.fa.core.team.Team;

import java.util.List;

/**
 * A class representing the lobby of players
 * @author F.C. Slothouber
 */
public class Lobby {

    /**
     * List of teams.
     */
    private List<Team> teams;

    /**
     * List of all the grand prix.
     */
    private List<GrandPrix> shedule;

    /**
     * integer representing the current grand prix.
     */
    private int currentGrandPrix;

    /**
     * Constrctor.
     * @param teams The list of teams.
     * @param shedule The list of grand prix.
     */
    public Lobby(List<Team> teams, List<GrandPrix> shedule) {
        this.teams = teams;
        this.shedule = shedule;
        this.currentGrandPrix = 0;
    }

    /**
     * Returns the list of teams.
     * @return The list of teams.
     */
    public List<Team> getTeams() {
        return teams;
    }

    /**
     * Returns the list of grand prix.
     * @return The list of grand prix.
     */
    public List<GrandPrix> getShedule() {
        return shedule;
    }

    /**
     * Returns the current grand prix.
     * @return The integer representing the current grand prix.
     */
    public int getCurrentGrandPrix() {
        return this.currentGrandPrix;
    }
}

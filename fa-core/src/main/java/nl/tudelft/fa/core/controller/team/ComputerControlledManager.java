package nl.tudelft.fa.core.controller.team;

import nl.tudelft.fa.core.model.team.Team;

import java.util.List;

/**
 * Class representing a computer controlled manager
 * @version 01 12 2016
 * @author F.C. Slothouber
 */
public class ComputerControlledManager extends Manager {

    /**
     * Constructor
     * @param teams List of teams owned by bot (only 1 team)
     */
    public ComputerControlledManager(List<Team> teams) {
        super(teams);
    }
}

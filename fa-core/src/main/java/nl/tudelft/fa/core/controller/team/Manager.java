package nl.tudelft.fa.core.controller.team;

import nl.tudelft.fa.core.model.team.*;
import java.util.List;

/**
 * Class representing a manager
 * @version 01 12 2016
 * @author F.C. Slothouber
 */
public abstract class Manager {

    private List<Team> teams;

    /**
     * default Constructor
     * @param teams list of teams owned by manager
     */
    public Manager(List<Team> teams) {
        this.teams = teams;
    }
}

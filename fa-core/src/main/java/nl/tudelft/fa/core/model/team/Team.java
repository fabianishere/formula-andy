package nl.tudelft.fa.core.model.team;

import nl.tudelft.fa.core.controller.team.*;
import java.util.List;

/**
 * Class representing a Formula 1 team/constructor
 * @version 01 12 2016
 * @author F.C. Slothouber
 */
public class Team {

    private TeamProfile teamProfile;
    private List<Member> staff;
    private Manager manager;

    /**
     * Constructor
     * @param teamProfile teamprofile
     * @param staff List of staffmemebers
     * @param manager the manager can be bot or human
     */
    public Team(TeamProfile teamProfile, List<Member> staff, Manager manager) {
        this.teamProfile = teamProfile;
        this.staff = staff;
        this.manager = manager;
    }
}

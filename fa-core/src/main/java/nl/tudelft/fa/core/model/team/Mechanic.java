package nl.tudelft.fa.core.model.team;

/**
 * Class representing a mechanic specialist
 * @version 30 11 2016
 * @author F.C. Slothouber
 */
public class Mechanic extends Specialist {
    /**
     * Constructor
     * @param name name of mechanic
     * @param salary salary of mechanic
     * @param level level of mechanic
     */
    public Mechanic(String name, int salary, String id, double level) {
        super(name, salary, id, level);
    }
}

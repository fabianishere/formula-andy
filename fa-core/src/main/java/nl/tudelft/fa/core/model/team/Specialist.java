package nl.tudelft.fa.core.model.team;

/**
 * Class representing a Specialist
 * @version 30 11 2016
 * @author F.C. Slothouber
 */
public class Specialist extends Member{

    private double level;

    /**
     * Constructor
     * @param name name specialist
     * @param salary slaray specialist
     * @param level level of specialist
     */
    public Specialist(String name, int salary, String id, double level) {
        super(name, salary, id);
        this.level = level;
    }

    /**
     * Getter of level
     * @return returns value of level
     */
    public double getLevel() {
        return level;
    }

    /**
     * Setter of level
     * @param level new level value
     */
    public void setLevel(double level) {
        this.level = level;
    }
}

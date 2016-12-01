package nl.tudelft.fa.core.model.team;

/**
 * Class representing a driver
 * @version
 * @author F.C. Slothouber
 */
public class Driver extends Member {

    private double speed;
    private double raceCraft;
    private double strategy;

    /**
     * Constructor
     * @param name name of driver
     * @param salary salary of driver
     * @param speed numeric representation of speed
     * @param racecraft numeric representation of race craft
     * @param strategy numeric representation of strategy
     */
    public Driver(String name, int salary, String id, double speed, double racecraft, double strategy) {
        super (name, salary, id);
        this.speed = speed;
        this.raceCraft = racecraft;
        this.strategy = strategy;
    }

    /**
     * Getter of speed
     * @return returns double speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Setter speed
     * @param speed new speed value
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Getter race craft
     * @return return double racecraft
     */
    public double getRacecraft() {
        return raceCraft;
    }

    /**
     * Setter race craft
     * @param racecraft new race craft value
     */
    public void setRacecraft(double racecraft) {
        this.raceCraft = racecraft;
    }

    /**
     * Getter strategy
     * @return returns double strategy
     */
    public double getStrategy() {
        return strategy;
    }

    /**
     * Setter strategy
     * @param strategy new strategy value
     */
    public void setStrategy(double strategy) {
        this.strategy = strategy;
    }
}

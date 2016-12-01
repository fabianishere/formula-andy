package nl.tudelft.fa.core.model.game;

import nl.tudelft.fa.core.model.team.Tire;

/**
 * Class containing changable parameters during race
 * @version 01 12 2016
 * @author F.C. Slothouber
 */
public class CarParameters {

    private int mechanicRisk;
    private int aerodynamicistRisk;
    private int strategistRisk;
    private Tire tire;

    /**
     * Constructor
     * @param mechanicRisk the risk of the car setup
     * @param aerodynamicistRisk the risk of the car design
     * @param strategistRisk the risk of the strategy
     * @param tire the tire being used
     */
    public CarParameters(int mechanicRisk, int aerodynamicistRisk, int strategistRisk, Tire tire) {
        this.mechanicRisk = mechanicRisk;
        this.aerodynamicistRisk = aerodynamicistRisk;
        this.strategistRisk = strategistRisk;
        this.tire = tire;
    }

    /**
     * Getter of setup risk
     * @return return mechanic risk
     */
    public int getMechanicRisk() {
        return mechanicRisk;
    }

    /**
     * Setter mechanicrisk
     * @param mechanicRisk new risk value
     */
    public void setMechanicRisk(int mechanicRisk) {
        this.mechanicRisk = mechanicRisk;
    }

    /**
     * Getter car design risk
     * @return retuns risk
     */
    public int getAerodynamicistRisk() {
        return aerodynamicistRisk;
    }

    /**
     * Setter car design risk
     * @param aerodynamicistRisk new risk value
     */
    public void setAerodynamicistRisk(int aerodynamicistRisk) {
        this.aerodynamicistRisk = aerodynamicistRisk;
    }

    /**
     * Getter strategy risk
     * @return returns risk value
     */
    public int getStrategistRisk() {
        return strategistRisk;
    }

    /**
     * Setter strategy risk
     * @param strategistRisk new risk value
     */
    public void setStrategistRisk(int strategistRisk) {
        this.strategistRisk = strategistRisk;
    }

    /**
     * Getter current tire setup
     * @return returns tire
     */
    public Tire getTire() {
        return tire;
    }

    /**
     * Setter tire setup
     * @param tire new tire
     */
    public void setTire(Tire tire) {
        this.tire = tire;
    }
}

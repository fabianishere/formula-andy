package nl.tudelft.fa.core.game;


import nl.tudelft.fa.core.race.GrandPrix;

import java.util.List;
import java.util.Random;

/**
 * A class representing a race,
 * @author F.C. Slothouber
 */
public class RaceSimulator {

    /**
     * The list of cars racing.
     */
    private List<CarSimulator> carSimulators;

    /**
     * The grand prix.
     */
    private GrandPrix grandPrix;

    /**
     * Boolean representing if it is raining or not.
     */
    private boolean raining;

    /**
     * Constructor.
     * @param carSimulators List of the racing cars.
     * @param grandPrix The grand prix.
     */
    public RaceSimulator(List<CarSimulator> carSimulators, GrandPrix grandPrix) {
        this.carSimulators = carSimulators;
        this.grandPrix = grandPrix;
        this.raining = this.grandPrix.isItRaining(new Random());
    }

    /**
     * Returns the list of racing cars.
     * @return The List of carSimulators.
     */
    public List<CarSimulator> getCarSimulators() {
        return carSimulators;
    }

    /**
     * Returns the grand prix.
     * @return The grand prix.
     */
    public GrandPrix getGrandPrix() {
        return grandPrix;
    }

    /**
     * Generates the next race cycle. The moved distance and the crashes.
     */
    public void getNextRaceCycle() {
        for (CarSimulator cs: carSimulators) {
            if (!cs.getCarParameters().getCrashed()) {
                if (!cs.crashedThisCycle(raining, cs.closeDriver(carSimulators, 100), new Random())) {
                    cs.getCarParameters().increaseTraveledDistance(cs.getMovedDistance(new Random()));
                } else {
                    cs.getCarParameters().setCrashed(true);
                }
            }
        }
    }
}

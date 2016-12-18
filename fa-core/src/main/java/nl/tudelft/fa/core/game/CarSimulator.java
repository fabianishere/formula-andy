package nl.tudelft.fa.core.game;

import nl.tudelft.fa.core.race.CarConfiguration;
import nl.tudelft.fa.core.race.CarParameters;

import java.util.List;
import java.util.Random;

/**
 * A class that represents a car during a race.
 * @author F.C. Slothouber
 */
public class CarSimulator {

    /**
     * The factor with which your chance of crashing increases during rain.
     */
    private static double rainingFactor = 1.5;

    /**
     * The factor with which your chance of crashing increases while another driver is nearby.
     */
    private static double closeDriverFactor = 1.1;

    /**
     * The factor which determines the chance of crashing. If this value is higher the chance will be less.
     */
    private static int crashFactor = 8000; // higher = less chance of crashing

    /**
     * The carConfiguration of the simulator.
     */
    private CarConfiguration carConfiguration;

    /**
     * The carParameters of the simulator.
     */
    private CarParameters carParameters;

    /**
     * Constructor.
     * @param carConfiguration The carConfiguration of the simulator.
     * @param carParameters The carParameters of the simulator.
     */
    public CarSimulator(CarConfiguration carConfiguration, CarParameters carParameters) {
        this.carConfiguration = carConfiguration;
        this.carParameters = carParameters;
    }

    /**
     * Returns the car configuration of the simulator.
     * @return The car configuration.
     */
    public CarConfiguration getCarConfiguration() {
        return carConfiguration;
    }

    /**
     * Returns the car parameters of the simulator.
     * @return The car parameters.
     */
    public CarParameters getCarParameters() {
        return carParameters;
    }

    /**
     * Reurns the moved distance of the car this race cycle.
     * @param random A Object of type Random to make the method less predictable.
     * @return The moved distance.
     */
    public double getMovedDistance(Random random) {
        double mechFactor = carConfiguration.getMechanic().getSpecialistFactor(carParameters.getMechanicalRisk());
        double aeroFactor = carConfiguration.getAerodynamicist().getSpecialistFactor(carParameters.getAerodynamicRisk());
        double stratFactor = carConfiguration.getStrategist().getSpecialistFactor(carParameters.getStrategistRisk())
                * carConfiguration.getDriver().getStrategy();
        double driverFactor = carConfiguration.getDriver().getDriverFactor();
        double maxDistance = carConfiguration.getEngine().getMaxDistance(carParameters.getTire());

        return random.nextDouble() * mechFactor * aeroFactor * stratFactor * driverFactor * maxDistance;
    }

    /**
     * Returns if the car crashed this race cycle.
     * @param raining True of it is raining else false.
     * @param closeDriver True is there is a driver close by.
     * @param random A random.
     * @return True if the crashes this cycle else False.
     */
    public boolean crashedThisCycle(boolean raining, boolean closeDriver, Random random) {
        double maxDistance = carConfiguration.getEngine().getMaxDistance(carParameters.getTire());
        double neededGrip = 10 * maxDistance / carConfiguration.getDriver().getRacecraft();
        neededGrip = raining ? neededGrip * rainingFactor : neededGrip;
        double checkFactor = closeDriver ? neededGrip * closeDriverFactor : neededGrip;

        return random.nextInt(crashFactor) < checkFactor;
    }

    /**
     * Returns if there is a close driver nearby.
     * @param carSimulators The list of drivers on the circuit.
     * @param closeDistance A double to be considered close.
     * @return True if there is at least 1 close driver else False.
     */
    public boolean closeDriver(List<CarSimulator> carSimulators, double closeDistance) {
        for (CarSimulator cs : carSimulators) {
            if (this != cs && Math.abs(this.carParameters.getTraveledDistance() - cs.getCarParameters().getTraveledDistance()) < closeDistance) {
                return true;
            }
        }
        return false;
    }
}

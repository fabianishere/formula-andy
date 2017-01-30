/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Fabian Mastenbroek, Christian Slothouber,
 * Laetitia Molkenboer, Nikki Bouman, Nils de Beukelaar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package nl.tudelft.fa.core.race;

import java.util.Random;
import java.util.function.Predicate;

/**
 * The {@link CarSimulator} contains the simulation logic for a single car.
 *
 * @author Christian Slothouber
 * @author Fabian Mastenbroek
 */
public class CarSimulator {
    /**
     * The factor with which your chance of crashing increases during rain.
     */
    private static final double rainingFactor = 1.5;

    /**
     * The factor with which your chance of crashing increases while another driver is nearby.
     */
    private static final double closeDriverFactor = 1.1;

    /**
     * The factor which determines the chance of crashing. If this value is higher the chance will
     * be less.
     */
    private static final int crashFactor = 8000; // higher = less chance of crashing

    /**
     * The configuration of the simulator.
     */
    private CarConfiguration configuration;

    /**
     * The parameters of the simulator.
     */
    private CarParameters parameters;

    /**
     * Construct a {@link CarSimulator} instance.
     *
     * @param configuration The configuration of the simulator.
     * @param parameters The parameters of the simulator.
     */
    public CarSimulator(CarConfiguration configuration, CarParameters parameters) {
        this.configuration = configuration;
        this.parameters = parameters;
    }

    /**
     * Return the car configuration of the simulator.
     *
     * @return The car configuration.
     */
    public CarConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Set the configuration of the of the {@link CarSimulator}.
     *
     * @param configuration The configuration to set.
     */
    public void setConfiguration(CarConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Return the car parameters of the simulator.
     *
     * @return The car parameters.
     */
    public CarParameters getParameters() {
        return parameters;
    }

    /**
     * Set the parameters of the {@link CarSimulator}.
     *
     * @param parameters The parameters to set.
     */
    public void setParameters(CarParameters parameters) {
        this.parameters = parameters;
    }

    /**
     * Calculate the moved distance of the car this race cycle.
     *
     * @param random A {@link Random} instance to add a random factor to the results.
     * @return The distance the car moved from its previous position.
     */
    public double calculateDelta(Random random) {
        double mechFactor = configuration.getMechanic()
            .getSpecialistFactor(parameters.getMechanicalRisk());
        double aeroFactor = configuration.getAerodynamicist()
            .getSpecialistFactor(parameters.getAerodynamicRisk());
        double stratFactor = configuration.getStrategist()
            .getSpecialistFactor(parameters.getStrategicRisk())
                * configuration.getDriver().getStrategy();
        double driverFactor = configuration.getDriver().getDriverFactor();
        double maxDistance = configuration.getEngine().getMaxDistance(parameters.getTire());

        return random.nextDouble() * mechFactor * aeroFactor * stratFactor * driverFactor
            * maxDistance;
    }

    /**
     * Determine whether the car has crashed this cycle.
     *
     * @param raining A flag to indicate whether it is raining.
     * @param nearby A flag to indicate a driver is nearby.
     * @param random A {@link Random} instance to add a random factor to the results.
     * @return <code>true</code> if the car has crashed, <code>false</code> otherwise.
     */
    public boolean hasCrashed(boolean raining, boolean nearby, Random random) {
        double maxDistance = configuration.getEngine().getMaxDistance(parameters.getTire());
        double neededGrip = 10 * maxDistance / configuration.getDriver().getRacecraft();
        neededGrip = raining ? neededGrip * rainingFactor : neededGrip;
        double checkFactor = nearby ? neededGrip * closeDriverFactor : neededGrip;

        return random.nextInt(crashFactor) < checkFactor;
    }

    /**
     * Determine whether a car is nearby this car.
     *
     * @param carResult The previous simulation result of the car.
     * @param raceResult The results of the whole cycle.
     * @param distance The distance that is considered close.
     * @return <code>true</code> if there is at least one car in the near vicinity,
     *      <code>false</code> otherwise.
     */
    public boolean isNearby(CarSimulationResult carResult, RaceSimulationResult raceResult,
                            double distance) {
        final Predicate<CarSimulationResult> close =
            res -> Math.abs(carResult.getDistanceTraveled() - res.getDistanceTraveled()) < distance;
        final Predicate<CarSimulationResult> self =
            res -> !res.getConfiguration().getCar().equals(configuration.getCar());
        return raceResult.getResults().stream().anyMatch(self.and(close));
    }
}

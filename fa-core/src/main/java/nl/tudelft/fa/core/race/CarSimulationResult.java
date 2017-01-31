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

import java.util.Objects;

/**
 * This class contains the result of a car simulation.
 *
 * @author Fabian Mastenbroek
 */
public class CarSimulationResult {
    /**
     * The configuration that produced this result.
     */
    private final CarConfiguration configuration;

    /**
     * The distance the car has traveled.
     */
    private final double distanceTraveled;

    /**
     * A flag to indicate whether the car crashed.
     */
    private final boolean crashed;

    /**
     * A flag to indicate whether the car has finished.
     */
    private final boolean finished;

    /**
     * Construct a {@link CarSimulationResult} instance.
     *
     * @param configuration The configuration that produced this result.
     * @param distanceTraveled The total distance the car has traveled.
     * @param crashed A flag to indicate whether the car crashed.
     */
    public CarSimulationResult(CarConfiguration configuration, double distanceTraveled,
                               boolean crashed, boolean finished) {
        this.configuration = configuration;
        this.distanceTraveled = distanceTraveled;
        this.crashed = crashed;
        this.finished = finished;
    }

    /**
     * Return the configuration that produced this result.
     *
     * @return The configuration that produced this result.
     */
    public CarConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Return the total distance the car has traveled.
     *
     * @return The total distance the car has traveled as double.
     */
    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    /**
     * Determine whether the car has crashed.
     *
     * @return <code>true</code> if the car has crashed, <code>false</code> otherwise.
     */
    public boolean hasCrashed() {
        return crashed;
    }

    /**
     * Determine whether the car has finished.
     *
     * @return <code>true</code> if the car has finished, <code>false</code> otherwise.
     */
    public boolean hasFinished() {
        return finished;
    }

    /**
     * Return a new {@link CarSimulationResult} instance with the same distance traveled, but has
     * crashed.
     *
     * @return A new {@link CarSimulationResult} instance with the same distance, but crashed.
     */
    public CarSimulationResult crash() {
        return new CarSimulationResult(configuration, distanceTraveled, true, finished);
    }

    /**
     * Return a new {@link CarSimulationResult} instance from this instance that is finished.
     *
     * @return A new {@link CarSimulationResult} instance that is finished.
     */
    public CarSimulationResult finish() {
        return new CarSimulationResult(configuration, distanceTraveled, crashed, true);
    }

    /**
     * Return a new {@link CarSimulationResult} instance from this instance if the distance
     * that has been traveled is larger or equal to the given distance.
     *
     * @param distance The distance needed to finish the simulation.
     * @return A new {@link CarSimulationResult} instance that is either finished or the same.
     */
    public CarSimulationResult finishOn(double distance) {
        return distance > distanceTraveled ? this : finish();
    }

    /**
     * Increase the traveled distance by the given amount if the car has not crashed, in a new
     * {@link CarSimulationResult} instance.
     *
     * @param delta The distance to increase the total distance with.
     * @return A new {@link CarSimulationResult} instance with the increased distance.
     */
    public CarSimulationResult increaseDistance(double delta) {
        return crashed ? this : new CarSimulationResult(configuration,distanceTraveled + delta,
            false, finished);
    }

    /**
     * Test whether this {@link CarSimulationResult} is equal to the given object.
     *
     * @param other The object to be tested for equality
     * @return <code>true</code> if both objects are equal, <code>false</code> otherwise.
     */
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        CarSimulationResult that = (CarSimulationResult) other;
        return Math.abs(distanceTraveled - that.distanceTraveled) < 0.001
            && Objects.equals(crashed, that.crashed)
            && Objects.equals(configuration, that.configuration)
            && Objects.equals(finished, that.finished);
    }

    /**
     * Return the hash code of this object.
     *
     * @return The hash code of this object as integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(distanceTraveled, crashed, configuration, finished);
    }

    /**
     * Return a string representation of this configuration.
     *
     * @return A string representation of this configuration.
     */
    @Override
    public String toString() {
        return String.format("CarSimulationResult(configuration=%s, distanceTraveled=%f, crashed=%s"
            + ", finished=%s)", configuration, distanceTraveled, crashed, finished);
    }
}

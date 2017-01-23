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

import nl.tudelft.fa.core.lobby.message.LobbyEvent;
import nl.tudelft.fa.core.team.inventory.Car;

import java.util.Map;
import java.util.Objects;

/**
 * A {@link LobbyEvent} that represents the completion of one cycle of the race simulation.
 *
 * @author Fabian Mastenbroek
 */
public class RaceSimulationResult implements LobbyEvent {
    /**
     * The cars in the simulation with the results.
     */
    private final Map<Car, CarSimulationResult> cars;

    /**
     * A flag to indicate whether the race is finished.
     */
    private final boolean finished;

    /**
     * Construct a {@link RaceSimulationResult} instance.
     *
     * @param cars The cars in the simulation with the results.
     * @param finished A flag to indicate whether the race is finished.
     */
    public RaceSimulationResult(Map<Car, CarSimulationResult> cars, boolean finished) {
        this.cars = cars;
        this.finished = finished;
    }

    /**
     * Return the cars in the simulation with the results.
     *
     * @return The cars in the simulation with the results.
     */
    public Map<Car, CarSimulationResult> getCars() {
        return cars;
    }

    /**
     * Determine whether the game is finished.
     *
     * @return <Code>true</Code> if the game is finished, <code>false</code> otherwise.
     */
    public boolean isFinished() {
        return  finished;
    }

    /**
     * Test whether this {@link RaceSimulationResult} is equal to the given object.
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
        RaceSimulationResult that = (RaceSimulationResult) other;
        return Objects.equals(cars, that.cars)
            && Objects.equals(finished, that.finished);
    }

    /**
     * Return the hash code of this object.
     *
     * @return The hash code of this object as integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(cars, finished);
    }

    /**
     * Return a string representation of this configuration.
     *
     * @return A string representation of this configuration.
     */
    @Override
    public String toString() {
        return String.format("RaceSimulationResult(cars=%s, finished=%s)", cars, finished);
    }
}

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

import nl.tudelft.fa.core.team.inventory.Car;
import nl.tudelft.fa.core.team.inventory.Tire;

import java.util.Objects;

/**
 * This class represents the tunable parameters of a {@link Car}
 * during a race.
 *
 * @author Christian Slothouber
 */
public class CarParameters {
    /**
     * The risk of the car setup.
     */
    private final int mechanicalRisk;

    /**
     * The risk of the car design.
     */
    private final int aerodynamicRisk;

    /**
     * The risk of the strategy.
     */
    private final int strategicRisk;

    /**
     * The tire that is being used.
     */
    private final Tire tire;

    /**
     * The total distance this car has traveled during this race.
     */
    private double traveledDistance;

    /**
     * True if the car has crashed this race.
     */
    private boolean crashed;

    /**
     * Construct a {@link CarParameters} instance.
     *
     * @param mechanicalRisk The risk of the car setup.
     * @param aerodynamicRisk The risk of the car design.
     * @param strategicRisk The risk of the strategy.
     * @param tire The tire that is being used.
     */
    public CarParameters(int mechanicalRisk, int aerodynamicRisk, int strategicRisk, Tire tire) {
        this.mechanicalRisk = mechanicalRisk;
        this.aerodynamicRisk = aerodynamicRisk;
        this.strategicRisk = strategicRisk;
        this.tire = tire;
        this.traveledDistance = 0;
        this.crashed = false;
    }

    /**
     * Return the total traveled distance this race.
     * @return The total traveled distance.
     */
    public double getTraveledDistance() {
        return traveledDistance;
    }

    /**
     * Increase the total traveled distance of this race.
     * @param distance The distance to increase the total distance with.
     */
    public void increaseTraveledDistance(double distance) {
        traveledDistance = traveledDistance + distance;
    }

    /**
     * Set the crash state of the car.
     * @param bool The new crash state of the car. True is crashed. False is not crashed.
     */
    public void setCrashed(boolean bool) {
        crashed = bool;
    }

    /**
     * Return the crash state of the car.
     * @return The crash state of the car. True if crashed. False if not crashed.
     */
    public boolean getCrashed() {
        return this.crashed;
    }

    /**
     * Return the mechanical risk of the car.
     *
     * @return A numerical representation of the mechanical risk.
     */
    public int getMechanicalRisk() {
        return mechanicalRisk;
    }

    /**
     * Return the aerodynamic risk of the car.
     *
     * @return A numerical representation of the aerodynamic risk.
     */
    public int getAerodynamicRisk() {
        return aerodynamicRisk;
    }

    /**
     * Return the strategic risk of the car.
     *
     * @return A numerical representation of the strategic risk.
     */
    public int getStrategistRisk() {
        return strategicRisk;
    }

    /**
     * Return the strategic risk of the car.
     *
     * @return The strategic risk of the car.
     */
    public Tire getTire() {
        return tire;
    }

    /**
     * Test whether this {@link CarParameters} instance is equal to the given object.
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
        CarParameters that = (CarParameters) other;
        return Objects.equals(mechanicalRisk, that.mechanicalRisk)
            && Objects.equals(aerodynamicRisk, that.aerodynamicRisk)
            && Objects.equals(strategicRisk, that.strategicRisk)
            && Objects.equals(tire, that.tire);
    }

    /**
     * Return the hash code of this object.
     *
     * @return The hash code of this object as integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(mechanicalRisk, aerodynamicRisk, strategicRisk, tire);
    }

    /**
     * Return a string representation of this engine.
     *
     * @return A string representation of this engine.
     */
    @Override
    public String toString() {
        return String.format("CarParameters(mechanical=%d, aerodynamic=%d, strategic=%d, tire=%s)",
            mechanicalRisk, aerodynamicRisk, strategicRisk, tire);
    }
}

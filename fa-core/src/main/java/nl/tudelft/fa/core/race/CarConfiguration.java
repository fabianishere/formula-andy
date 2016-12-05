/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Fabian Mastenbroek, Christian Slothouber,
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

import nl.tudelft.fa.core.team.Aerodynamicist;
import nl.tudelft.fa.core.team.Car;
import nl.tudelft.fa.core.team.Driver;
import nl.tudelft.fa.core.team.Engine;
import nl.tudelft.fa.core.team.Mechanic;
import nl.tudelft.fa.core.team.Strategist;

import java.util.Objects;

/**
 * This class represents the initial and static configuration of a car during the race.
 *
 * @author Christian Slothouber
 */
public class CarConfiguration {
    /**
     * The car used in this configuration.
     */
    private final Car car;

    /**
     * The engine used in this configuration.
     */
    private final Engine engine;

    /**
     * The driver used in this configuration.
     */
    private final Driver driver;

    /**
     * The mechanic used in this configuration.
     */
    private final Mechanic mechanic;

    /**
     * The aerodynamicist used in this configuration.
     */
    private final Aerodynamicist aerodynamicist;

    /**
     * The strategist used in this configuration.
     */
    private final Strategist strategist;

    /**
     * Construct a {@link CarConfiguration} instance.
     *
     * @param car the car
     * @param engine the engine the car is using
     * @param driver the driver of this car
     * @param mechanic the mechanic that set the car up
     * @param aerodynamicist the aerodynamicist that designed the car
     * @param strategist the strategist that made the strategy
     */
    public CarConfiguration(Car car, Engine engine, Driver driver, Mechanic mechanic,
                            Aerodynamicist aerodynamicist, Strategist strategist) {
        this.car = car;
        this.driver = driver;
        this.engine = engine;
        this.mechanic = mechanic;
        this.aerodynamicist = aerodynamicist;
        this.strategist = strategist;
    }

    /**
     * Return the {@link Car} used in this configuration.
     *
     * @return The car used in this configuration.
     */
    public Car getCar() {
        return car;
    }

    /**
     * Return the {@link Engine} used in this configuration.
     *
     * @return The engine used in this configuration.
     */
    public Engine getEngine() {
        return engine;
    }

    /**
     * Return the {@link Driver} used in this configuration.
     *
     * @return The driver used in this configuration.
     */
    public Driver getDriver() {
        return driver;
    }

    /**
     * Return the {@link Mechanic} used in this configuration.
     *
     * @return The mechanic used in this configuration.
     */
    public Mechanic getMechanic() {
        return mechanic;
    }

    /**
     * Return the {@link Aerodynamicist} used in this configuration.
     *
     * @return The aerodynamicist used in this configuration.
     */
    public Aerodynamicist getAerodynamicist() {
        return aerodynamicist;
    }

    /**
     * Return the {@link Strategist} used in this configuration.
     *
     * @return The strategist used in this configuration.
     */
    public Strategist getStrategist() {
        return strategist;
    }

    /**
     * Test whether this {@link CarConfiguration} is equal to the given object.
     *
     * @param other The object to be tested for equality
     * @return <code>true</code> if both objects are equal, <code>false</code> otherwise.
     */
    public boolean equals(Object other) {
        if (other instanceof CarConfiguration) {
            CarConfiguration that = (CarConfiguration) other;

            return this.car.equals(that.car) && this.driver.equals(that.driver)
                && this.engine.equals(that.engine) && this.mechanic.equals(that.mechanic)
                && this.aerodynamicist.equals(that.aerodynamicist)
                && this.strategist.equals(that.strategist);
        }
        return false;
    }

    /**
     * Return the hash code of this object.
     *
     * @return The hash code of this object as integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(car, engine, driver, mechanic, aerodynamicist, strategist);
    }

    /**
     * Return a string representation of this engine.
     *
     * @return A string representation of this engine.
     */
    @Override
    public String toString() {
        return String.format("CarConfiguration(car=%s, engine=%s, driver=%s, mechanic=%s, "
            + "aerodynamicist=%s, strategist=%s)", car, engine, driver, mechanic,
            aerodynamicist, strategist);
    }
}


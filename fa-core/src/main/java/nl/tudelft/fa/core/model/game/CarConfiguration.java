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

package nl.tudelft.fa.core.model.game;

import nl.tudelft.fa.core.model.team.Aerodynamicist;
import nl.tudelft.fa.core.model.team.Car;
import nl.tudelft.fa.core.model.team.Driver;
import nl.tudelft.fa.core.model.team.Engine;
import nl.tudelft.fa.core.model.team.Mechanic;
import nl.tudelft.fa.core.model.team.Strategist;

/**
 * This class represents the initial and static configuration of a car during the race.
 *
 * @author Christian Slothouber
 */
public class CarConfiguration {
    /**
     * The car used in this configuration.
     */
    private Car car;

    /**
     * The driver used in this configuration.
     */
    private Driver driver;

    /**
     * The engine used in this configuration.
     */
    private Engine engine;

    /**
     * The mechanic used in this configuration.
     */
    private Mechanic mechanic;

    /**
     * The aerodynamicist used in this configuration.
     */
    private Aerodynamicist aerodynamicist;

    /**
     * The strategist used in this configuration.
     */
    private Strategist strategist;

    /**
     * Construct a {@link CarConfiguration} instance.
     *
     * @param car the car
     * @param driver the driver of this car
     * @param engine the engine the car is using
     * @param mechanic the mechanic that set the car up
     * @param aerodynamicist the aerodynamicist that designed the car
     * @param strategist the strategist that made the strategy
     */
    public CarConfiguration(Car car, Driver driver, Engine engine, Mechanic mechanic,
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
     * Setter of car.
     *
     * @param car new Car
     */
    public void setCar(Car car) {
        this.car = car;
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
     * Setter of driver.
     *
     * @param driver new Driver
     */
    public void setDriver(Driver driver) {
        this.driver = driver;
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
     * Setter of engine.
     *
     * @param engine The new Engine
     */
    public void setEngine(Engine engine) {
        this.engine = engine;
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
     * Setter of mechanic.
     *
     * @param mechanic new Mechanic
     */
    public void setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
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
     * Setter of aerodynamicist.
     *
     * @param aerodynamicist new Aerodynamicist
     */
    public void setAerodynamicist(Aerodynamicist aerodynamicist) {
        this.aerodynamicist = aerodynamicist;
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
     * Setter strategist.
     *
     * @param strategist new Strategist
     */
    public void setStrategist(Strategist strategist) {
        this.strategist = strategist;
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

            if (!this.car.equals(that.car)) {
                return false;
            }
            if (!this.driver.equals(that.driver)) {
                return false;
            }
            if (!this.engine.equals(that.engine)) {
                return false;
            }
            if (!this.mechanic.equals(that.mechanic)) {
                return false;
            }
            if (!this.aerodynamicist.equals(that.aerodynamicist)) {
                return false;
            }
            if (this.strategist.equals(that.strategist)) {
                return true;
            }
        }
        return false;
    }
}

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

import nl.tudelft.fa.core.model.team.*;

/**
 * Class containing the static configuration of a car during a race
 * @version 01 12 2016
 * @author F.C. Slothouber
 */
public class CarConfiguration {

    private Car car;
    private Driver driver;
    private Engine engine;
    private Mechanic mechanic;
    private Aerodynamicist aerodynamicist;
    private Strategist strategist;

    /**
     * Constructore
     * @param car the car
     * @param driver the driver of this car
     * @param engine the engine the car is using
     * @param mechanic the mechanic that set the car up
     * @param aerodynamicist the aerodynamicist that designed the car
     * @param strategist the strategist that made the strategy
     */
    public CarConfiguration(Car car, Driver driver, Engine engine, Mechanic mechanic, Aerodynamicist aerodynamicist, Strategist strategist) {
        this.car = car;
        this.driver = driver;
        this.engine = engine;
        this.mechanic = mechanic;
        this.aerodynamicist = aerodynamicist;
        this.strategist = strategist;
    }

    /**
     * Getter of car
     * @return returns car
     */
    public Car getCar() {
        return car;
    }

    /**
     * Stter of car
     * @param car new Car
     */
    public void setCar(Car car) {
        this.car = car;
    }

    /**
     * Getter of driver
     * @return returns driver
     */
    public Driver getDriver() {
        return driver;
    }

    /**
     * Setter of driver
     * @param driver new Driver
     */
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    /**
     * Getter of engine
     * @return returns engine
     */
    public Engine getEngine() {
        return engine;
    }

    /**
     * Setter of engine
     * @param engine
     */
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    /**
     * Getter of mechanic
     * @return returns mechanic
     */
    public Mechanic getMechanic() {
        return mechanic;
    }

    /**
     * Setter of mechanic
     * @param mechanic new Mechanic
     */
    public void setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    /**
     * Getter of aerodynamicist
     * @return returns aerodynamicist
     */
    public Aerodynamicist getAerodynamicist() {
        return aerodynamicist;
    }

    /**
     * Setter of aerodynamicist
     * @param aerodynamicist new Aerodynamicist
     */
    public void setAerodynamicist(Aerodynamicist aerodynamicist) {
        this.aerodynamicist = aerodynamicist;
    }

    /**
     * Getter of strategist
     * @return returns strategist
     */
    public Strategist getStrategist() {
        return strategist;
    }

    /**
     * Setter strategist
     * @param strategist new Strategist
     */
    public void setStrategist(Strategist strategist) {
        this.strategist = strategist;
    }

    /**
     * equals method
     * @param other Object to be tested for equality
     * @return returns true if equal else false
     */
    public boolean equals(Object other) {
        if (other instanceof CarConfiguration) {
            CarConfiguration that = (CarConfiguration) other;

            if (!this.car.equals(that.car)) {
                return false;
            }
            if (!this.driver.equals(that.driver)){
                return false;
            }
            if (!this.engine.equals(that.engine)){
                return false;
            }
            if (!this.mechanic.equals(that.mechanic)){
                return false;
            }
            if (!this.aerodynamicist.equals(that.aerodynamicist)){
                return false;
            }
            if (this.strategist.equals(that.strategist)){
                return true;
            }
        } return false;
    }
}


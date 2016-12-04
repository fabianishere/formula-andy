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

package nl.tudelft.fa.core.model.team;

/**
 * This class represents a Formula 1 car engine.
 *
 * @author Christian Slothouber
 */
public class Engine {
    /**
     * The name of the brand of this engine.
     */
    private String brand;

    /**
     * The power of this engine.
     */
    private double power;

    /**
     * The driveability of this engine.
     */
    private double driveability;

    /**
     * The fuel consumption of this engine.
     */
    private double fuelConsumption;

    /**
     * Construct a {@link Engine} instance.
     * @param brand brand name
     * @param power numeric representation of power
     * @param driveability numeric representation of driveability
     * @param fuelConsumption numeric representation of fuel consumption
     */
    public Engine(String brand, double power, double driveability, double fuelConsumption) {
        this.brand = brand;
        this.power = power;
        this.driveability = driveability;
        this.fuelConsumption = fuelConsumption;
    }

    /**
     * Return the name of the brand of this engine.
     *
     * @return The name of the brand of this engine.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Setter of brand.
     * @param brand new brand name
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Return the power of this engine.
     *
     * @return The power of this engine.
     */
    public double getPower() {
        return power;
    }

    /**
     * Setter of  power.
     * @param power new power setting
     */
    public void setPower(double power) {
        this.power = power;
    }

    /**
     * Return the driveability of this engine.
     *
     * @return The driveability of this engine.
     */
    public double getDriveability() {
        return driveability;
    }

    /**
     * Setter of driveability.
     * @param driveability new driveability setting
     */
    public void setDriveability(double driveability) {
        this.driveability = driveability;
    }

    /**
     * Return the fuel consumption of this engine.
     *
     * @return The fuel consumption of this engine.
     */
    public double getFuelConsumption() {
        return fuelConsumption;
    }

    /**
     * Setter of fuelConsumption.
     * @param fuelConsumption new fuel Consumption setting
     */
    public void setFuelConsumption(double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    /**
     * Test whether this {@link Engine} is equal to the given object.
     *
     * @param other The object to be tested for equality
     * @return <code>true</code> if both objects are equal, <code>false</code> otherwise.
     */
    public boolean equals(Object other) {
        if (!(other instanceof Engine)) {
            return false;
        }

        Engine that = (Engine) other;

        if (!(this.power == that.power)) {
            return false;
        }
        if (!(this.driveability == that.driveability)) {
            return false;
        }
        if (!(this.fuelConsumption == that.fuelConsumption)) {
            return false;
        }
        return this.brand.equals(that.brand);
    }
}

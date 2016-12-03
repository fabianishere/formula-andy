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
 * Class representing a car engine
 * @version 30 11 2016
 * @author F.C. Slothouber
 */
public class Engine {

    private String brand;
    private double power;
    private double driveAbility;
    private double fuelConsumption;

    /**
     * Construct a {@link Engine} instance.
     * @param brand brand name
     * @param power numeric representation of power
     * @param driveAbility numeric representation of driveability
     * @param fuelConsumption numeric representation of fuel consumption
     */
    public Engine(String brand, double power, double driveAbility, double fuelConsumption) {
        this.brand = brand;
        this.power = power;
        this.driveAbility = driveAbility;
        this.fuelConsumption = fuelConsumption;
    }

    /**
     * Getter of brand.
     * @return returns String brand
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
     * Getter of power.
     * @return returns double power
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
     * Getter of driveability.
     * @return return double driveability
     */
    public double getDriveAbility() {
        return driveAbility;
    }

    /**
     * Setter of driveability.
     * @param driveAbility new driveability setting
     */
    public void setDriveAbility(double driveAbility) {
        this.driveAbility = driveAbility;
    }

    /**
     * Getter of fuel consumption.
     * @return return double fuelConsumption
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
        if (!(this.driveAbility == that.driveAbility)) {
            return false;
        }
        if (!(this.fuelConsumption == that.fuelConsumption)) {
            return false;
        }
        return this.brand.equals(that.brand);
    }
}

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

package nl.tudelft.fa.core.team.inventory;

import java.util.UUID;

/**
 * This class represents a Formula 1 car engine.
 *
 * @author Christian Slothouber
 */
public class Engine extends InventoryItem {
    /**
     * The name of the brand of this engine.
     */
    private String brand;

    /**
     * The name of the engine.
     */
    private String name;

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
     *
     * @param id The unique id of this engine.
     * @param brand The brand name of the engine.
     * @param name The name of the engine.
     * @param power A numeric representation of power
     * @param driveability A numeric representation of driveability
     * @param fuelConsumption A numeric representation of fuel consumption
     */
    public Engine(UUID id, String brand, String name, double power, double driveability,
                  double fuelConsumption) {
        super(id);
        this.brand = brand;
        this.name = name;
        this.power = power;
        this.driveability = driveability;
        this.fuelConsumption = fuelConsumption;
    }

    /**
     * Construct a {@link Engine} instance.
     */
    protected Engine() {
        super(null);
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
     * Return the name of the engine.
     *
     * @return The name of the engine.
     */
    public String getName() {
        return name;
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
     * Return the driveability of this engine.
     *
     * @return The driveability of this engine.
     */
    public double getDriveability() {
        return driveability;
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
     * Determines the maximum distance the car can potentially travel.
     * @param tire The tire type currently used by the car.
     * @return The maximum distance.
     */
    public double getMaxDistance(Tire tire) {
        return power * tire.getResistanceFactor() ;
    }

    /**
     * Return a string representation of this engine.
     *
     * @return A string representation of this engine.
     */
    @Override
    public String toString() {
        return String.format("Engine(id=%s, brand=%s, name=%s)", getId(), brand, name);
    }
}

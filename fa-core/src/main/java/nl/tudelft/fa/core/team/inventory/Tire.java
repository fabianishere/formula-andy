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

import nl.tudelft.fa.core.team.Team;

import java.util.UUID;

/**
 * This class represents a Formula 1 tire.
 *
 * @author Christian Slothouber
 */
public class Tire extends InventoryItem {
    /**
     * The name of the brand of this tire.
     */
    private String brand;

    /**
     * The type of tire.
     */
    private TireType type;

    /**
     * The durability of the tire.
     */
    private double durability;

    /**
     * The grip of the tire.
     */
    private double grip;

    /**
     * Construct a {@link Tire} instance.
     *
     * @param id The unique identifier of this tire.
     * @param owner The owner of this tire.
     * @param brand The name of the brand of this tire.
     * @param type The type of the tire e.g. "Ultra Soft" or "Intermediate"
     * @param durability A numeric representation of durability
     * @param grip A numeric representation of grip
     */
    public Tire(UUID id, Team owner, String brand, TireType type, double durability, double grip) {
        super(id, owner);
        this.brand = brand;
        this.type = type;
        this.durability = durability;
        this.grip = grip;
    }

    /**
     * Construct a {@link Tire} instance.
     */
    protected Tire() {
        super(null, null);
    }

    /**
     * Return a factor based on the grip of the tire. The more grip the tire has the more friction
     * they cause.
     * @return The resistance factor.
     */
    public double getResistanceFactor() {
        return 1 - this.grip * this.grip / 250;
    }

    /**
     * Return the name of the brand of this tire.
     *
     * @return The name of the brand of this tire.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Return the type of the tire.
     *
     * @return The tire type.
     */
    public TireType getType() {
        return type;
    }

    /**
     * Return the durability of the tire.
     *
     * @return A numeric representation of the durability.
     */
    public double getDurability() {
        return durability;
    }

    /**
     * Return the grip of the tire.
     *
     * @return A numeric representation of the grip.
     */
    public double getGrip() {
        return grip;
    }

    /**
     * Return a textual representation of this tire.
     *
     * @return A textual representation of this tire.
     */
    @Override
    public String toString() {
        return String.format("Tire(id=%s, brand=%s, type=%s)", getId(), brand, type);
    }
}

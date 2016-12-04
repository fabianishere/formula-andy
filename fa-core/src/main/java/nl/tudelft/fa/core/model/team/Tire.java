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
 * This class represents a Formula 1 tire.
 *
 * @author Christian Slothouber
 */
public class Tire {
    /**
     * The name of the brand of this tire.
     */
    private final String brand;

    /**
     * The type of tire.
     */
    private final String type;

    /**
     * The durability of the tire.
     */
    private final double durability;

    /**
     * The grip of the tire.
     */
    private final double grip;

    /**
     * Construct a {@link Tire} instance.
     *
     * @param brand The name of the brand of this tire.
     * @param type The type of tire e.g. "Ultra Soft" or "Intermediate"
     * @param durability A numeric representation of durability
     * @param grip A numeric representation of grip
     */
    public Tire(String brand, String type, double durability, double grip) {
        this.brand = brand;
        this.type = type;
        this.durability = durability;
        this.grip = grip;
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
     * @return A textual representation of the tire type.
     */
    public String getType() {
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
     * Test whether this {@link Tire} is equal to the given object.
     *
     * @param other The object to be tested for equality
     * @return <code>true</code> if both objects are equal, <code>false</code> otherwise.
     */
    public boolean equals(Object other) {
        if (other instanceof Tire) {
            Tire that = (Tire) other;
            return this.type.equals(that.type);
        }
        return false;
    }
}

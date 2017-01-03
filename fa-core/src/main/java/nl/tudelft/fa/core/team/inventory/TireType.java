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

/**
 * An enumeration of all types of tires used in a Formula 1 race.
 *
 * @author Fabian Mastenbroek
 */
public enum TireType {
    ULTRA_SOFT("Ultra Soft"),
    SUPER_SOFT("Super Soft"),
    SOFT("Soft"),
    MEDIUM("Medium"),
    HARD("Hard"),
    INTERMEDIATE("Intermediate"),
    WET("Wet");

    /**
     * The name of this type of tire.
     */
    private String name;

    /**
     * Construct a {@link TireType}.
     *
     * @param name The name of this type of tire.
     */
    TireType(String name) {
        this.name = name;
    }

    /**
     * Return the name of this type of tire.
     *
     * @return The name of this type of tire.
     */
    public String getName() {
        return name;
    }

    /**
     * Return a string representation of this tire type.
     *
     * @return A string representation of this tire type.
     */
    @Override
    public String toString() {
        return name;
    }
}

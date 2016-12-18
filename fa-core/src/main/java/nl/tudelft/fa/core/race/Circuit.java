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

import java.util.UUID;

/**
 * This class represents a race circuit on which races take place.
 *
 * @author Christian Slothouber
 */
public class Circuit {
    /**
     * The unique id of this circuit.
     */
    private final UUID id;

    /**
     * The name of the circuit.
     */
    private final String name;

    /**
     * The country the circuit is located in.
     */
    private final String country;

    /**
     * The length of the circuit in metres.
     */
    private final double length;

    /**
     * Construct a [@link Circuit} instance.
     *
     * @param id The unique id of the circuit.
     * @param name The name of the circuit
     * @param country The country this circuit is located in.
     */
    public Circuit(UUID id, String name, String country, double length) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.length = length;
    }

    /**
     * Return the length of the circuit.
     * @return The length of the circuit.
     */
    public double getLength() {
        return length;
    }

    /**
     * Return the unique id of the circuit.
     *
     * @return The unique id of the circuit.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Return the name of the circuit.
     *
     * @return The name of the circuit.
     */
    public String getName() {
        return name;
    }

    /**
     * Return the name of the country the circuit is located in.
     *
     * @return The name of the country the circuit is located in.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Test whether this {@link Circuit} is equal to the given object.
     *
     * @param other The object to be tested for equality
     * @return <code>true</code> if both objects are equal, <code>false</code> otherwise.
     */
    public boolean equals(Object other) {
        if (other instanceof Circuit) {
            Circuit that = (Circuit) other;
            return this.id.equals(that.id);
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
        return id.hashCode();
    }

    /**
     * Return a string representation of this engine.
     *
     * @return A string representation of this engine.
     */
    @Override
    public String toString() {
        return String.format("Circuit(id=%s, name=%s, country=%s)", id, name, country);
    }
}

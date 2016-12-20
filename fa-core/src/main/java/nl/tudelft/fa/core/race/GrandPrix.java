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

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

/**
 * This class represents a race weekend or grand prix at a specific {@link Circuit} which
 * has qualifications and a race.
 *
 * @author Christian Slothouber
 */
public class GrandPrix {
    /**
     * The unique id of this grand prix.
     */
    private final UUID id;

    /**
     * The circuit of this grand prix.
     */
    private final Circuit circuit;

    /**
     * The date of this grand prix.
     */
    private final Instant date;

    /**
     * The amount of laps in a grand prix.
     */
    private final int laps;

    /**
     * The chance in percentage that it will be raining the day of the race.
     */
    private final int rainChance;

    /**
     * Construct a {@link GrandPrix} instance.
     *
     * @param id The unique id of this grand prix.
     * @param circuit The race circuit at which this grand prix takes place.
     * @param date The date of the race.
     * @param laps The amount of laps in a grand prix.
     */
    public GrandPrix(UUID id, Circuit circuit, Instant date, int laps, int rainChance) {
        this.id = id;
        this.circuit = circuit;
        this.date = date;
        this.laps = laps;
        this.rainChance = rainChance;
    }

    /**
     * Return the chance of rain of this grand prix.
     * @return The rain chance.
     */
    public int getRainChance() {
        return rainChance;
    }

    /**
     * Determines if it is raining on the day of the race.
     * @param random The Random the method will be using to determine the weather.
     * @return True if it will be raining otherwise false.
     */
    public boolean isItRaining(Random random) {
        return random.nextInt(101) <= rainChance;
    }

    /**
     * Return the unique id of this grand prix.
     *
     * @return The unique id of this grand prix.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Return the {@link Circuit} at which the grand prix takes place.
     *
     * @return The {@link Circuit} of this grand prix.
     */
    public Circuit getCircuit() {
        return circuit;
    }

    /**
     * Return the date at which the grand prix takes place.
     *
     * @return The date at which the grand prix takes place.
     */
    public Instant getDate() {
        return date;
    }

    /**
     * Return the amount of laps in this grand prix.
     *
     * @return The amount of laps in this grand prix..
     */
    public int getLaps() {
        return laps;
    }

    /**
     * Test whether this {@link GrandPrix} is equal to the given object.
     *
     * @param other The object to be tested for equality
     * @return <code>true</code> if both objects are equal, <code>false</code> otherwise.
     */
    public boolean equals(Object other) {
        if (other instanceof GrandPrix) {
            GrandPrix that = (GrandPrix) other;
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
        return String.format("GrandPrix(id=%s, circuit=%s, date=%s, laps=%d)", id, circuit, date,
            laps);
    }
}

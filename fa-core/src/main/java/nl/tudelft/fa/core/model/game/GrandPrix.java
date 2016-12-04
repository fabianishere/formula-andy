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

/**
 * This class represents a race weekend or grand prix at a specific {@link Circuit} which
 * has qualifications and a race.
 *
 * @author Christian Slothouber
 */
public class GrandPrix {
    /**
     * The circuit of this grand prix.
     */
    private Circuit circuit;

    /**
     * The date of this grand prix.
     */
    private String date;

    /**
     * The amount of laps in a grand prix.
     */
    private final int laps;

    /**
     * Construct a {@link GrandPrix} instance.
     *
     * @param circuit The race circuit at which this grand prix takes place.
     * @param date The date of the race.
     * @param laps The amount of laps in a grand prix.
     */
    public GrandPrix(Circuit circuit, String date, int laps) {
        this.circuit = circuit;
        this.date = date;
        this.laps = laps;
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
    public String getDate() {
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
}

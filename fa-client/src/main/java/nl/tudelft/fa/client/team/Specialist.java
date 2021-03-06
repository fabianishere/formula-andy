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

package nl.tudelft.fa.client.team;

import java.util.UUID;

/**
 * A {@link Member} of a {@link Team} that is specialized in a specific part of the race or car.
 *
 * @author Christian Slothouber
 */
public abstract class Specialist extends Member {
    /**
     * The factor penalty/boost for low risk.
     */
    private static double lowRisk = 0.7;

    /**
     * The factor penalty/boost for medium risk.
     */
    private static double mediumRisk = 0.8;

    /**
     * The factor penalty/boost for high risk.
     */
    private static double highRisk = 1.0;

    /**
     * The level of this specialist.
     */
    private double level;

    /**
     * Construct a {@link Specialist} instance.
     *
     * @param id The unique id of the specialist.
     * @param name The name of the specialist.
     * @param salary The salary of the specialist.
     * @param level The level of the specialist.
     */
    public Specialist(UUID id, String name, int salary, double level) {
        super(id, name, salary);
        this.level = level;
    }

    /**
     * Construct a {@link Specialist}.
     */
    protected Specialist() {}

    /**
     * Return the level of this specialist.
     *
     * @return The level of this specialist.
     */
    public double getLevel() {
        return level;
    }
}

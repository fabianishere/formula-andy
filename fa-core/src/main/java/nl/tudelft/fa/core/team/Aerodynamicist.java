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

package nl.tudelft.fa.core.team;

import java.util.UUID;

/**
 * A aerodynamic {@link Specialist}.
 *
 * @author Christian Slothouber
 */
public class Aerodynamicist extends Specialist {
    /**
     * Construct a {@link Aerodynamicist} instance.
     *
     * @param id The unique id of this specialist.
     * @param team The team of the specialist.
     * @param name name The name of this specialist.
     * @param salary salary The salary of this specialist.
     * @param level level The level of this specialist.
     */
    public Aerodynamicist(UUID id, Team team, String name, int salary, double level) {
        super(id, team, name, salary, level);
    }

    /**
     * Construct a {@link Aerodynamicist}.
     */
    protected Aerodynamicist() {}

    /**
     * Return a string representation of this specialist.
     *
     * @return A string representation of this specialist.
     */
    @Override
    public String toString() {
        return String.format("Aerodynamicist(id=%s, name=%s, salary=%d, level=%f)",
            getId(), getName(), getSalary(), getLevel());
    }
}

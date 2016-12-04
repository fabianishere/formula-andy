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

import java.util.UUID;

/**
 * A strategic {@link Specialist}.
 *
 * @author Christian Slothouber
 */
public class Strategist extends Specialist {
    /**
     * Construct a {@link Strategist} instance.
     *
     * @param id The unique id of this strategist.
     * @param name name The name of the strategist.
     * @param salary salary The salary of the strategist.
     * @param level level The level of the strategist.
     *
     */
    public Strategist(UUID id, String name, int salary, double level) {
        super(id, name, salary, level);
    }

    /**
     * Return a string representation of this specialist.
     *
     * @return A string representation of this specialist.
     */
    @Override
    public String toString() {
        return String.format("Strategist(id=%s, name=%s, salary=%d, level=%f)",
            getId(), getName(), getSalary(), getLevel());
    }
}

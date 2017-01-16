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

package nl.tudelft.fa.core.race;

import java.util.Objects;
import java.util.Set;

/**
 * A {@link TeamConfiguration} class consist of the configuration of both cars of a team.
 *
 * @author Fabian Mastenbroek
 */
public class TeamConfiguration {
    /**
     * The configurations of the cars in the team.
     */
    private final Set<CarConfiguration> configurations;

    /**
     * Construct a {@link TeamConfiguration} instance.
     *
     * @param configurations The configurations of the cars of this team.
     */
    public TeamConfiguration(Set<CarConfiguration> configurations) {
        this.configurations = configurations;
    }

    /**
     * Return the configurations of the cars in the team.
     *
     * @return The configurations of the cars in the team.
     */
    public Set<CarConfiguration> getConfigurations() {
        return configurations;
    }

    /**
     * Test whether this {@link TeamConfiguration} is equal to the given object.
     *
     * @param other The object to be tested for equality
     * @return <code>true</code> if both objects are equal, <code>false</code> otherwise.
     */
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        TeamConfiguration that = (TeamConfiguration) other;
        return Objects.equals(configurations, that.configurations);
    }

    /**
     * Return the hash code of this object.
     *
     * @return The hash code of this object as integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(configurations);
    }

    /**
     * Return a string representation of this engine.
     *
     * @return A string representation of this engine.
     */
    @Override
    public String toString() {
        return String.format("TeamConfiguration(configurations=%s)", configurations);
    }
}
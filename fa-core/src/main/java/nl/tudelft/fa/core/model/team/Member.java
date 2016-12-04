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
 * This class represents a member of a Formula 1 {@link Team}.
 *
 * @author Christian Slothouber
 */
public abstract class Member {
    /**
     * The name of this member.
     */
    private final String name;

    /**
     * The salary of this member.
     */
    private int salary;

    /**
     * The id of this member.
     */
    private final String id;

    /**
     * Construct a {@link Member} instance.
     * @param name Name of crew member
     * @param salary salary of crew member
     */
    public Member(String name, int salary, String id) {
        this.name = name;
        this.salary = salary;
        this.id = id;
    }

    /**
     * Return the name of this member.
     *
     * @return The name of this member.
     */
    public String getName() {
        return name;
    }

    /**
     * Return the salary of this member.
     *
     * @return The salary of this member.
     */
    public int getSalary() {
        return salary;
    }

    /**
     * Set the salary of this member.
     *
     * @param salary The salary to set.
     */
    public void setSalary(int salary) {
        this.salary = salary;
    }

    /**
     * Return the id of this member.
     *
     * @return The id of this member.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Test whether this {@link Member} is equal to the given object.
     *
     * @param other The object to be tested for equality
     * @return <code>true</code> if both objects are equal, <code>false</code> otherwise.
     */
    public boolean equals(Object other) {
        if (other instanceof  Member) {
            Member that = (Member) other;
            return this.id.equals(that.id);
        }
        return false;
    }
}

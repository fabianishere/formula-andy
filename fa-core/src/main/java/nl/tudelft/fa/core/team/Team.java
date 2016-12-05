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

package nl.tudelft.fa.core.team;

import nl.tudelft.fa.core.race.GrandPrix;
import nl.tudelft.fa.core.team.manager.Manager;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * This class represents a Formula 1 team that participates in
 * {@link GrandPrix}s.
 *
 * @author Christian Slothouber
 */
public class Team {
    /**
     * The unique id of this team.
     */
    private UUID id;

    /**
     * The name of this team.
     */
    private String name;

    /**
     * The current budget of the team.
     */
    private int budget;

    /**
     * The staff members of this team.
     */
    private List<Member> staff;

    /**
     * The manager that manages this team.
     */
    private Manager manager;

    /**
     * Construct a {@link Team} instance.
     *
     * @param name The name of the team.
     * @param budget The budget of the team.
     * @param manager The manager that manages this team.
     * @param staff The staff members of this team.
     */
    public Team(UUID id, String name, int budget, Manager manager, List<Member> staff) {
        this.id = id;
        this.name = name;
        this.budget = budget;
        this.manager = manager;
        this.staff = staff;
    }

    /**
     * Construct a {@link Team} instance.
     */
    private Team() {}

    /**
     * Return the unique id of this team.
     *
     * @return The unique id of this team/
     */
    public UUID getId() {
        return id;
    }

    /**
     * Return the name of the team.
     *
     * @return The name of the team.
     */
    public String getName() {
        return name;
    }

    /**
     * Return the budget of the team.
     *
     * @return The budget of the team.
     */
    public int getBudget() {
        return budget;
    }

    /**
     * Return the manager of this team.
     *
     * @return The manager of this team.
     */
    public Manager getManager() {
        return manager;
    }

    /**
     * Return the staff members of the team.
     *
     * @return A list of the members of this team.
     */
    public List<Member> getStaff() {
        return Collections.unmodifiableList(staff);
    }

    /**
     * Test whether this {@link Team} is equal to the given object.
     *
     * @param other The object to be tested for equality
     * @return <code>true</code> if both objects are equal, <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Team) {
            Team that = (Team) other;
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
     * Return a string representation of this team.
     *
     * @return A string representation of this team.
     */
    @Override
    public String toString() {
        return String.format("Team(id=%s, name=%s)", id, name);
    }
}

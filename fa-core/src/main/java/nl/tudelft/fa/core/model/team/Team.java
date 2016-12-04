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

import nl.tudelft.fa.core.controller.team.Manager;

import java.util.List;

/**
 * This class represents a Formula 1 team participating in
 * {@link nl.tudelft.fa.core.model.game.GrandPrix}s.
 *
 * @author Christian Slothouber
 */
public class Team {
    /**
     * The team profile of this team.
     */
    private TeamProfile profile;

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
     * @param profile The profile of this team.
     * @param staff The staff members of this team.
     * @param manager The manager that manages this team.
     */
    public Team(TeamProfile profile, List<Member> staff, Manager manager) {
        this.profile = profile;
        this.staff = staff;
        this.manager = manager;
    }
}
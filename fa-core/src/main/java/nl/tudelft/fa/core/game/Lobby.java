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

package nl.tudelft.fa.core.game;

import nl.tudelft.fa.core.race.GrandPrix;
import nl.tudelft.fa.core.team.Team;

import java.util.List;

/**
 * A class representing the lobby of players
 * @author F.C. Slothouber
 */
public class Lobby {

    /**
     * List of teams.
     */
    private List<Team> teams;

    /**
     * List of all the grand prix.
     */
    private List<GrandPrix> shedule;

    /**
     * integer representing the current grand prix.
     */
    private int currentGrandPrix;

    /**
     * Constrctor.
     * @param teams The list of teams.
     * @param shedule The list of grand prix.
     */
    public Lobby(List<Team> teams, List<GrandPrix> shedule) {
        this.teams = teams;
        this.shedule = shedule;
        this.currentGrandPrix = 0;
    }

    /**
     * Returns the list of teams.
     * @return The list of teams.
     */
    public List<Team> getTeams() {
        return teams;
    }

    /**
     * Returns the list of grand prix.
     * @return The list of grand prix.
     */
    public List<GrandPrix> getShedule() {
        return shedule;
    }

    /**
     * Returns the current grand prix.
     * @return The integer representing the current grand prix.
     */
    public int getCurrentGrandPrix() {
        return this.currentGrandPrix;
    }
}

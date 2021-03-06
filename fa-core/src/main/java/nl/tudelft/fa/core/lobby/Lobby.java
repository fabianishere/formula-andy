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

package nl.tudelft.fa.core.lobby;

import nl.tudelft.fa.core.lobby.actor.LobbyActor;
import nl.tudelft.fa.core.lobby.message.LobbyResponse;
import nl.tudelft.fa.core.race.GrandPrix;
import nl.tudelft.fa.core.team.Team;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * This class represents the state of a {@link LobbyActor} instance.
 *
 * @author Fabian Mastenbroek
 */
public final class Lobby implements LobbyResponse {
    /**
     * The unique identifier of the lobby.
     */
    private final String id;

    /**
     * The status of the lobby.
     */
    private final LobbyStatus status;

    /**
     * The configuration of the lobby.
     */
    private final LobbyConfiguration configuration;

    /**
     * The {@link Team}s in this lobby.
     */
    private final Set<Team> teams;

    /**
     * The schedule of the races.
     */
    private final List<GrandPrix> schedule;

    /**
     * Construct a {@link Lobby} instance.
     *
     * @param id The unique identifier of the lobby.
     * @param status The status of the lobby.
     * @param configuration The configuration of the lobby.
     * @param teams The teams in the lobby.
     * @param schedule The schedule of the races.
     */
    public Lobby(String id, LobbyStatus status, LobbyConfiguration configuration, Set<Team> teams,
                 List<GrandPrix> schedule) {
        this.id = id;
        this.status = status;
        this.configuration = configuration;
        this.teams = teams;
        this.schedule = schedule;
    }

    /**
     * Return the unique identifier of the lobby.
     *
     * @return The unique identifier of the lobby.
     */
    public String getId() {
        return id;
    }

    /**
     * Return the status of the lobby.
     *
     * @return The status of the lobby.
     */
    public LobbyStatus getStatus() {
        return status;
    }

    /**
     * Return the configuration of the lobby.
     *
     * @return The configuration of the lobby.
     */
    public LobbyConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Return the teams in the lobby.
     *
     * @return The teams in the lobby.
     */
    public Set<Team> getTeams() {
        return teams;
    }

    /**
     * Return the schedule of the races of the lobby.
     *
     * @return The schedule of the races.
     */
    public List<GrandPrix> getSchedule() {
        return schedule;
    }

    /**
     * Test whether this {@link Lobby} is equal to the given object.
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
        Lobby that = (Lobby) other;
        return Objects.equals(id, that.id)
            && Objects.equals(status, that.status)
            && Objects.equals(configuration, that.configuration)
            && Objects.equals(teams, that.teams)
            && Objects.equals(schedule, that.schedule);
    }

    /**
     * Return the hash code of this object.
     *
     * @return The hash code of this object as integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, status, configuration, teams, schedule);
    }

    /**
     * Return a string representation of this configuration.
     *
     * @return A string representation of this configuration.
     */
    @Override
    public String toString() {
        return String.format("Lobby(id=%s, status=%s, configuration=%s, teams=%s, schedule=%s)",
            id, status, configuration, teams, schedule);
    }
}

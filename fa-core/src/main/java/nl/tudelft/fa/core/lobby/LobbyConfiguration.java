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
import nl.tudelft.fa.core.lobby.schedule.LobbyScheduleFactory;

import java.time.Duration;
import java.util.Objects;

/**
 * This class contains the configuration of a {@link LobbyActor} actor.
 *
 * @author Fabian Mastenbroek
 */
public class LobbyConfiguration {
    /**
     * The maximum amount of teams in this lobby.
     */
    private final int maxTeams;

    /**
     * The duration of the intermission.
     */
    private final Duration intermission;

    /**
     * The preparation duration players have before the game.
     */
    private final Duration preparation;

    /**
     * The {@link LobbyScheduleFactory} to use.
     */
    private final LobbyScheduleFactory scheduleFactory;

    /**
     * Construct a {@link LobbyConfiguration} instance.
     *
     * @param maxTeams The maximum amount of teams in this lobby.
     * @param intermission The duration of the intermission.
     * @param preparation The preparation time players have before the game.
     * @param scheduleFactory The {@link LobbyScheduleFactory} to use.
     */
    public LobbyConfiguration(int maxTeams, Duration intermission, Duration preparation,
                              LobbyScheduleFactory scheduleFactory) {
        this.maxTeams = maxTeams;
        this.intermission = intermission;
        this.preparation = preparation;
        this.scheduleFactory = scheduleFactory;
    }

    /**
     * Return the maximum amount of teams allowed in the lobby.
     *
     * @return An integer representing the maximum amount of teams allowed in the lobby.
     */
    public int getTeamMaximum() {
        return maxTeams;
    }

    /**
     * Return the preparation time users have before the game.
     *
     * @return The preparation time users have before the game.
     */
    public Duration getIntermission() {
        return intermission;
    }


    /**
     * Return the preparation time users have before the game.
     *
     * @return The preparation time users have before the game.
     */
    public Duration getPreparation() {
        return preparation;
    }

    /**
     * Return the {@link LobbyScheduleFactory} to generate the schedule.
     *
     * @return The {@link LobbyScheduleFactory} instance.
     */
    public LobbyScheduleFactory getScheduleFactory() {
        return scheduleFactory;
    }

    /**
     * Test whether this {@link LobbyConfiguration} is equal to the given object.
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
        LobbyConfiguration that = (LobbyConfiguration) other;
        return Objects.equals(maxTeams, that.maxTeams)
            && Objects.equals(intermission, that.intermission)
            && Objects.equals(preparation, that.preparation);
    }

    /**
     * Return the hash code of this object.
     *
     * @return The hash code of this object as integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(maxTeams, intermission, preparation);
    }

    /**
     * Return a string representation of this configuration.
     *
     * @return A string representation of this configuration.
     */
    @Override
    public String toString() {
        return String.format("LobbyConfiguration(teamMaximum=%d, intermission=%s, preparation=%s, "
            + "scheduleFactory=%s)", maxTeams, intermission, preparation, scheduleFactory);
    }
}

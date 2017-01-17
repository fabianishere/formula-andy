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

package nl.tudelft.fa.server.helper.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.tudelft.fa.core.lobby.LobbyConfiguration;
import nl.tudelft.fa.core.lobby.schedule.LobbyScheduleFactory;

import java.time.Duration;

/**
 * Mixin for the {@link LobbyConfiguration} class.
 *
 * @author Fabian Mastenbroek
 */
public abstract class LobbyConfigurationMixin {
    /**
     * Construct a {@link LobbyConfigurationMixin} instance.
     *
     * @param maxUsers The maximum amount of users in the lobby.
     * @param intermission The duration of the intermission.
     * @param preparation The preparation time in the lobby.
     * @param scheduleFactory The schedule factory to use.
     */
    @JsonCreator
    public LobbyConfigurationMixin(@JsonProperty("userMaximum") int maxUsers,
                                   @JsonProperty("intermission") Duration intermission,
                                   @JsonProperty("preparation") Duration preparation,
                                   @JsonProperty("schedule-factory")
                                           LobbyScheduleFactory scheduleFactory) {
        // no implementation. Jackson does the work
    }

    /**
     * Return the {@link LobbyScheduleFactory} to generate the schedule.
     *
     * @return The {@link LobbyScheduleFactory} instance.
     */
    @JsonIgnore
    public abstract LobbyScheduleFactory getScheduleFactory();
}

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
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.tudelft.fa.core.lobby.message.TeamConfigurationSubmission;
import nl.tudelft.fa.core.race.CarConfiguration;
import nl.tudelft.fa.core.user.User;

import java.util.Set;

/**
 * Mix-in for the {@link TeamConfigurationSubmission} class.
 *
 * @author Fabian Mastenbroek
 */
public abstract class TeamConfigurationSubmissionMixin {
    /**
     * Construct a {@link TeamConfigurationSubmission} message instance.
     *
     * @param user The user that wants to submit this configuration.
     * @param cars The configuration of the cars the user wants to submit.
     */
    @JsonCreator
    public TeamConfigurationSubmissionMixin(@JsonProperty("user") User user,
                                            @JsonProperty("cars") Set<CarConfiguration> cars) {}
}

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

package nl.tudelft.fa.client.helper.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.tudelft.fa.client.race.Circuit;
import nl.tudelft.fa.client.race.GrandPrix;

import java.time.Instant;
import java.util.UUID;

/**
 * Mix-in for the {@link GrandPrix} class.
 *
 * @author Fabian Mastenbroek
 */
public abstract class GrandPrixMixin {
    /**
     * Construct a {@link GrandPrix} instance.
     *
     * @param id The unique id of this grand prix.
     * @param circuit The race circuit at which this grand prix takes place.
     * @param date The date of the race.
     * @param laps The amount of laps in a grand prix.
     */
    @JsonCreator
    public GrandPrixMixin(@JsonProperty("id") UUID id, @JsonProperty("circuit") Circuit circuit,
                     @JsonProperty("date") Instant date,
                     @JsonProperty("laps") int laps, @JsonProperty("rainChance") int rainChance) {}
}

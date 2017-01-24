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

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.tudelft.fa.core.race.CarConfiguration;
import nl.tudelft.fa.core.team.Aerodynamicist;
import nl.tudelft.fa.core.team.Driver;
import nl.tudelft.fa.core.team.Mechanic;
import nl.tudelft.fa.core.team.Strategist;
import nl.tudelft.fa.core.team.inventory.Car;
import nl.tudelft.fa.core.team.inventory.Engine;

/**
 * Mix-in for the {@link CarConfiguration} class.
 *
 * @author Fabian Mastenbroek
 */
public abstract class CarConfigurationMixin {
    /**
     * Construct a {@link CarConfigurationMixin} instance.
     *
     * @param car the car
     * @param engine the engine the car is using
     * @param driver the driver of this car
     * @param mechanic the mechanic that set the car up
     * @param aerodynamicist the aerodynamicist that designed the car
     * @param strategist the strategist that made the strategy
     */
    public CarConfigurationMixin(@JsonProperty("car") Car car,
                                 @JsonProperty("engine") Engine engine,
                                 @JsonProperty("driver") Driver driver,
                                 @JsonProperty("mechanic") Mechanic mechanic,
                                 @JsonProperty("aerodynamicist") Aerodynamicist aerodynamicist,
                                 @JsonProperty("strategist") Strategist strategist) {}
}

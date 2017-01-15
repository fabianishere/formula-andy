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

package nl.tudelft.fa.core.lobby.message;

import nl.tudelft.fa.core.race.CarParameters;
import nl.tudelft.fa.core.team.inventory.Car;

import java.util.Objects;

/**
 *  A {@link LobbyInboundMessage} from a user that wants to submit parameters for its car.
 *
 * @author Fabian Mastenbroek
 */
public class CarParametersSubmission implements LobbyInboundMessage {
    /**
     * The {@link Car} to which the parameters should apply.
     */
    private final Car car;

    /**
     * The parameters to submit.
     */
    private final CarParameters parameters;

    /**
     * Construct a {@link CarParametersSubmission} instance.
     *
     * @param car The car to which the parameters should apply.
     * @param parameters The parameters to submit.
     */
    public CarParametersSubmission(Car car, CarParameters parameters) {
        this.car = car;
        this.parameters = parameters;
    }

    /**
     * Return the {@link Car} to which the parameters should apply.
     *
     * @return The car to which the parameters should apply.
     */
    public Car getCar() {
        return car;
    }

    /**
     * Return the parameters to submit.
     *
     * @return The parameters to submit.
     */
    public CarParameters getParameters() {
        return parameters;
    }

    /**
     * Test whether this message is equal to the given object, which means that all properties of
     * this message are equal to the properties of the other class.
     *
     * @param other The object to be tested for equality
     * @return <code>true</code> if both objects are equal, <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        CarParametersSubmission that = (CarParametersSubmission) other;
        return Objects.equals(car, that.car)
            && Objects.equals(parameters, that.parameters);
    }

    /**
     * Return the hash code of this object.
     *
     * @return The hash code of this object as integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(car, parameters);
    }

    /**
     * Return a string representation of this message.
     *
     * @return A string representation of this message.
     */
    @Override
    public String toString() {
        return String.format("CarParametersSubmission(car=%s, parameters=%s)", car, parameters);
    }
}

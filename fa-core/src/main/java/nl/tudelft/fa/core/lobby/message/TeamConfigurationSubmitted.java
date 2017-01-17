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

import nl.tudelft.fa.core.race.CarConfiguration;
import nl.tudelft.fa.core.user.User;

import java.util.Objects;
import java.util.Set;

/**
 * A {@link LobbyOutboundMessage} that indicates that a user has submitted its team configuration.
 *
 * @author Fabian Mastenbroek
 */
public final class TeamConfigurationSubmitted implements LobbyEvent {
    /**
     * The user that wants to submit this configuration.
     */
    private final User user;

    /**
     * The configurations of the cars in the team.
     */
    private final Set<CarConfiguration> cars;

    /**
     * Construct a {@link TeamConfigurationSubmitted} message instance.
     *
     * @param user The user that wants to submit this configuration.
     * @param cars The configuration of the team to submit.
     */
    public TeamConfigurationSubmitted(User user, Set<CarConfiguration> cars) {
        this.user = user;
        this.cars = cars;
    }

    /**
     * Return the {@link User} that wants to submit this configuration.
     *
     * @return The user that wants to submit the configuration.
     */
    public User getUser() {
        return user;
    }

    /**
     * Return the configuration of the cars that the user wants to submit.
     *
     * @return The configuration of the cars the user wants to submit.
     */
    public Set<CarConfiguration> getCars() {
        return cars;
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
        TeamConfigurationSubmitted that = (TeamConfigurationSubmitted) other;
        return Objects.equals(user, that.user)
            && Objects.equals(cars, that.cars);
    }

    /**
     * Return the hash code of this object.
     *
     * @return The hash code of this object as integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(user, cars);
    }

    /**
     * Return a string representation of this message.
     *
     * @return A string representation of this message.
     */
    @Override
    public String toString() {
        return String.format("TeamConfigurationSubmitted(user=%s, cars=%s)", user, cars);
    }
}

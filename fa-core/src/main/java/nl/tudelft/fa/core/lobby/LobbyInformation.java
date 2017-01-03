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

import nl.tudelft.fa.core.lobby.actor.Lobby;
import nl.tudelft.fa.core.user.User;

import java.util.Objects;
import java.util.Set;


/**
 * This class provides information about a {@link Lobby} actor.
 *
 * @author Fabian Mastenbroek
 */
public class LobbyInformation {
    /**
     * The status of the lobby.
     */
    private LobbyStatus status;

    /**
     * The configuration of the lobby.
     */
    private LobbyConfiguration configuration;

    /**
     * The {@link User}s in this lobby.
     */
    private Set<User> users;

    /**
     * Construct a {@link LobbyInformation} instance.
     *
     * @param status The status of the lobby.
     * @param configuration The configuration of the lobby.
     * @param users The users in the lobby.
     */
    public LobbyInformation(LobbyStatus status, LobbyConfiguration configuration, Set<User> users) {
        this.status = status;
        this.configuration = configuration;
        this.users = users;
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
     * Return the users of the lobby.
     *
     * @return The users of the lobby.
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * Test whether this {@link LobbyInformation} is equal to the given object.
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
        LobbyInformation that = (LobbyInformation) other;
        return Objects.equals(status, that.status)
            && Objects.equals(configuration, that.configuration)
            && Objects.equals(users, that.users);
    }

    /**
     * Return the hash code of this object.
     *
     * @return The hash code of this object as integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(status, configuration, users);
    }

    /**
     * Return a string representation of this configuration.
     *
     * @return A string representation of this configuration.
     */
    @Override
    public String toString() {
        return String.format("LobbyInformation(status=%s, configuration=%s, users=%d)",
            status, configuration, users.size());
    }
}

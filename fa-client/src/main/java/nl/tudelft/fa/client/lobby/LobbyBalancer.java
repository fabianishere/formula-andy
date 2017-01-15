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

package nl.tudelft.fa.client.lobby;

import java.util.Objects;
import java.util.Set;

/**
 * This class represents the state of a lobby balancer actor instance.
 *
 * @author Fabian Mastenbroek
 */
public final class LobbyBalancer {
    /**
     * A {@link Set} containing the lobbies the balancer manages.
     */
    private final Set<Lobby> lobbies;

    /**
     * Construct a {@link LobbyBalancer} instance.
     *
     * @param lobbies The lobbies the balancer manages.
     */
    public LobbyBalancer(Set<Lobby> lobbies) {
        this.lobbies = lobbies;
    }

    /**
     * Return the {@link Lobby} actors the balancer manages.
     *
     * @return The lobbies the balancer manages.
     */
    public Set<Lobby> getLobbies() {
        return lobbies;
    }

    /**
     * Test whether this {@link LobbyBalancer} is equal to the given object.
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
        LobbyBalancer that = (LobbyBalancer) other;
        return Objects.equals(lobbies, that.lobbies);
    }

    /**
     * Return the hash code of this object.
     *
     * @return The hash code of this object as integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(lobbies);
    }

    /**
     * Return a string representation of this configuration.
     *
     * @return A string representation of this configuration.
     */
    @Override
    public String toString() {
        return String.format("LobbyBalancer(lobbies=%s)", lobbies);
    }
}

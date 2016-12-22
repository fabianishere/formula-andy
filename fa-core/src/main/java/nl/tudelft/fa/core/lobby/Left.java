/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Fabian Mastenbroek, Christian Slothouber,
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

import akka.actor.ActorRef;
import nl.tudelft.fa.core.lobby.actor.Lobby;
import nl.tudelft.fa.core.user.User;

import java.util.Objects;

/**
 * This message indicates the {@link User} has left the {@link Lobby}.
 *
 * @author Fabian Mastenbroek
 */
public class Left {
    /**
     * The user that has left the lobby.
     */
    private User user;

    /**
     * The reference to the {@link Lobby} the user has left.
     */
    private ActorRef lobby;

    /**
     * Construct a {@link Left} message instance.
     *
     * @param user The user that has left the lobby.
     * @param lobby The reference to the lobby the user has left.
     */
    public Left(User user, ActorRef lobby) {
        this.user = user;
        this.lobby = lobby;
    }

    /**
     * Return the user that has left the lobby.
     *
     * @return The user that has left the lobby.
     */
    public User getUser() {
        return user;
    }

    /**
     * Return the reference to the lobby the user has left.
     *
     * @return The reference to the lobby the user has left.
     */
    public ActorRef getLobby() {
        return lobby;
    }

    /**
     * Test whether this message is equal to the given object.
     *
     * @param other The object to be tested for equality
     * @return <code>true</code> if both objects are equal, <code>false</code> otherwise.
     */
    public boolean equals(Object other) {
        if (other instanceof Left) {
            Left that = (Left) other;
            return this.user.equals(that.user)
                && this.lobby.equals(that.lobby);
        }
        return false;
    }

    /**
     * Return the hash code of this object.
     *
     * @return The hash code of this object as integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(user, lobby);
    }

    /**
     * Return a string representation of this message.
     *
     * @return A string representation of this message.
     */
    @Override
    public String toString() {
        return String.format("Left(user=%s, lobby=%s)", user, lobby);
    }
}

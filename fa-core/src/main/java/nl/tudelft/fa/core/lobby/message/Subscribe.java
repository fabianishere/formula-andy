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

import akka.actor.ActorRef;
import nl.tudelft.fa.core.lobby.actor.Lobby;

import java.util.Objects;

/**
 * This message is send to a {@link Lobby} actor to subscribe to the event stream of this actor.
 *
 * @author Fabian Mastenbroek
 */
public final class Subscribe {
    /**
     * The actor that wants to subscribe to the {@link Lobby}.
     */
    private ActorRef actor;

    /**
     * Construct a {@link Subscribe} instance.
     *
     * @param actor The actor that wants to subscribe to the {@link Lobby}.
     */
    public Subscribe(ActorRef actor) {
        this.actor = actor;
    }

    /**
     * Return the actor that wants to subscribe to the {@link Lobby}.
     *
     * @return The actor that wants to subscribe.
     */
    public ActorRef getActor() {
        return actor;
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
        Subscribe that = (Subscribe) other;
        return Objects.equals(actor, that.actor);
    }

    /**
     * Return the hash code of this object.
     *
     * @return The hash code of this object as integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(actor);
    }

    /**
     * Return a string representation of this message.
     *
     * @return A string representation of this message.
     */
    @Override
    public String toString() {
        return String.format("Subscribe(actor=%s)", actor);
    }
}

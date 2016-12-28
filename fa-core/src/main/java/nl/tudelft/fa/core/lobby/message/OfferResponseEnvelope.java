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


package nl.tudelft.fa.core.lobby.message;

import nl.tudelft.fa.core.lobby.actor.Lobby;
import nl.tudelft.fa.core.user.User;

import java.util.Objects;

/**
 * This class is an envelope around the {@link OfferResponse} that also carries the references
 * to the {@link User} that has sent this response.
 * This class is used by the {@link Lobby} actor to associate an {@link OfferResponse} to a
 * {@link User}.
 *
 * @author Fabian Mastenbroek
 */
public final class OfferResponseEnvelope {
    /**
     * The message contained in this envelope.
     */
    private final OfferResponse message;

    /**
     * The {@link User} that is associated with this response.
     */
    private final User user;

    /**
     * Construct a {@link OfferResponseEnvelope} instance.
     *
     * @param message The message contained in this envelope.
     * @param user The user that is associated with this message.
     */
    public OfferResponseEnvelope(OfferResponse message, User user) {
        this.message = message;
        this.user = user;
    }

    /**
     * Return the message of this envelope.
     *
     * @return The message of this envelope.
     */
    public OfferResponse getMessage() {
        return message;
    }

    /**
     * Return the {@link User} associated with the message.
     *
     * @return The user associated with the message.
     */
    public User getUser() {
        return user;
    }

    /**
     * Test whether this message is equal to the given object.
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
        OfferResponseEnvelope that = (OfferResponseEnvelope) other;
        return message == that.message
            && Objects.equals(user, that.user);
    }

    /**
     * Return the hash code of this object.
     *
     * @return The hash code of this object as integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(message, user);
    }

    /**
     * Return a string representation of the credentials.
     *
     * @return A string representation of this credentials.
     */
    @Override
    public String toString() {
        return String.format("OfferResponseEnvelope(message=%s, user=%s)", message, user);
    }
}

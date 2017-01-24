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

package nl.tudelft.fa.core.auth.message;

import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.auth.actor.Authenticator;
import nl.tudelft.fa.core.user.User;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * This message is sent to an {@link Authenticator} actor to request the user to be authenticated.
 *
 * @author Fabian Mastenbroek
 */
public final class AuthenticationRequest {
    /**
     * The username to authenticate with.
     */
    private final String username;

    /**
     * The verification function to use.
     */
    private final Predicate<User> verifier;

    /**
     * Construct a {@link AuthenticationRequest} message.
     *
     * @param username The username of the user that wants to authenticate.
     * @param verifier The verification function to use.
     */
    public AuthenticationRequest(String username, Predicate<User> verifier) {
        this.username = username;
        this.verifier = verifier;
    }

    /**
     * Construct a {@link AuthenticationRequest} message.
     *
     * @param credentials The credentials to authenticate with.
     */
    public AuthenticationRequest(Credentials credentials) {
        this(credentials.getUsername(), user -> user.getCredentials().equals(credentials));
    }

    /**
     * Return the username to authenticate with.
     *
     * @return The username to authenticate with.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Return the verifier to use.
     *
     * @return The verifier to use.
     */
    public Predicate<User> getVerifier() {
        return verifier;
    }

    /**
     * Verify this request for the given user.
     *
     * @param user The {@link User} to verify.
     * @return <code>true</code> if this request is correct for the given user, <code>false</code>
     *     otherwise.
     */
    public boolean verify(User user) {
        return verifier.test(user);
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
        AuthenticationRequest that = (AuthenticationRequest) other;
        return Objects.equals(username, that.username);
    }

    /**
     * Return the hash code of this object.
     *
     * @return The hash code of this object as integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    /**
     * Return a string representation of this message.
     *
     * @return A string representation of this message.
     */
    @Override
    public String toString() {
        return String.format("AuthenticationRequest(username=%s)", username);
    }
}

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

package nl.tudelft.fa.core.user;


import nl.tudelft.fa.core.auth.Credentials;

import java.util.UUID;

/**
 * This class represents a user account.
 *
 * @author Fabian Mastenbroek
 */
public class User {
    /**
     * The unique id of the user.
     */
    private UUID id;

    /**
     * The credentials of the user.
     */
    private Credentials credentials;

    /**
     * Construct a {@link User} instance.
     *
     * @param id The unique identifier of the user.
     * @param credentials The credentials of the user.
     */
    public User(UUID id, Credentials credentials) {
        this.id = id;
        this.credentials = credentials;
    }

    /**
     * Construct a {@link User} instance.
     */
    protected User() {}

    /**
     * Return the unique identifier of the user.
     *
     * @return The unique identifier of the user.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Return the credentials of the user.
     *
     * @return The credentials of the user.
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     * Test whether this {@link User} is equal to the given object.
     *
     * @param other The object to be tested for equality
     * @return <code>true</code> if both objects are equal, <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof User) {
            User that = (User) other;
            return this.id.equals(that.id);
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
        return id.hashCode();
    }

    /**
     * Return a string representation of this team.
     *
     * @return A string representation of this team.
     */
    @Override
    public String toString() {
        return String.format("User(id=%s, credentials=%s)", id, credentials);
    }
}

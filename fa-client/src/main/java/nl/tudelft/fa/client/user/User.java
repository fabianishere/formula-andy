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

package nl.tudelft.fa.client.user;

import java.util.Objects;
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
    private final UUID id;

    /**
     * The username of the user.
     */
    private final String username;

    /**
     * Construct a {@link User} instance.
     *
     * @param id The unique identifier of the user.
     * @param username The username of the user.
     */
    public User(UUID id, String username) {
        this.id = id;
        this.username = username;
    }

    /**
     * Return the unique identifier of the user.
     *
     * @return The unique identifier of the user.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Return the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Test whether this {@link User} is equal to the given object.
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
        User that = (User) other;
        return Objects.equals(id, that.id);
    }

    /**
     * Return the hash code of this object.
     *
     * @return The hash code of this object as integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Return a string representation of this team.
     *
     * @return A string representation of this team.
     */
    @Override
    public String toString() {
        return String.format("User(id=%s, username=%s)", id, username);
    }
}

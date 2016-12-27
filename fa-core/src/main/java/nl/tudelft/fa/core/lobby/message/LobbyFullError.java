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

import nl.tudelft.fa.core.user.User;

/**
 * This message indicates that the lobby a {@link User} is trying to join, is full.
 *
 * @author Fabian Mastenbroek
 */
public final class LobbyFullError extends JoinError {
    /**
     * The amount of users in the lobby currently.
     */
    private int users;

    /**
     * Construct a {@link LobbyFullError} message.
     *
     * @param users The amount of users in the lobby currently.
     */
    public LobbyFullError(int users) {
        super("The lobby you are trying to join is full.");
        this.users = users;
    }

    /**
     * Return the amount of users currently in the lobby.
     *
     * @return The amount of users currently in the lobby.
     */
    public int getUsers() {
        return users;
    }

    /**
     * Return a string representation of this message.
     *
     * @return A string representation of this message.
     */
    @Override
    public String toString() {
        return String.format("LobbyFullError(message=%s, users=%d)", getMessage(), users);
    }
}

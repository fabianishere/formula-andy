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

/**
 * This message represents a failure to leave a lobby, because the user was not in that lobby.
 *
 * @author Fabian Mastenbroek
 */
public final class NotInLobbyException extends LeaveException {
    /**
     * Construct a {@link NotInLobbyException} instance.
     *
     * @param lobby The lobby the exception occurred in.
     */
    public NotInLobbyException(ActorRef lobby) {
        super(lobby, "You are not in the lobby you are trying to leave.");
    }

    /**
     * Construct a {@link NotInLobbyException} instance.
     *
     * @param lobby The lobby the exception occurred in.
     * @param message The message of the error.
     */
    public NotInLobbyException(ActorRef lobby, String message) {
        super(lobby, message);
    }
}

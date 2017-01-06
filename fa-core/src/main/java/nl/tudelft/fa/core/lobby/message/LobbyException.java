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

/**
 * This message represents an exception within the {@link Lobby} actor.
 *
 * @author Fabian Mastenbroek
 */
public class LobbyException extends Exception {
    /**
     * The {@link Lobby} where the exception occurred.
     */
    private ActorRef lobby;

    /**
     * Construct a {@link LobbyException} instance.
     *
     * @param lobby The lobby where the exception occurred.
     * @param message The message of the exception.
     */
    public LobbyException(ActorRef lobby, String message) {
        super(message);
        this.lobby = lobby;
    }

    /**
     * Construct a {@link JoinException} instance.
     *
     * @param lobby The lobby where the exception occurred.
     */
    public LobbyException(ActorRef lobby) {
        super();
        this.lobby = lobby;
    }

    /**
     * Return the {@link Lobby} where the exception occurred.
     *
     * @return The reference to the lobby where the exception occurred.
     */
    public ActorRef getLobby() {
        return lobby;
    }
}

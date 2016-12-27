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

package nl.tudelft.fa.server.controller;

import static akka.http.javadsl.server.Directives.*;

import akka.actor.ActorRef;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.server.Route;
import akka.pattern.PatternsCS;

import nl.tudelft.fa.core.lobby.LobbyInformation;
import nl.tudelft.fa.core.lobby.actor.Lobby;
import nl.tudelft.fa.core.lobby.message.InformationRequest;

/**
 * The controller for a single {@link Lobby} actor.
 *
 * @author Fabian Mastenbroek
 */
public class LobbyController {
    /**
     * The reference to the {@link Lobby} actor.
     */
    private ActorRef lobby;

    /**
     * Construct a {@link LobbyController} instance.
     *
     * @param lobby The reference to the {@link Lobby} actor.
     */
    public LobbyController(ActorRef lobby) {
        this.lobby = lobby;
    }

    /**
     * Create the {@link Route} instance for this controller.
     *
     * @return The routes for this controller.
     */
    public Route createRoute() {
        // Show the information about the lobby.
        return completeOKWithFuture(PatternsCS.ask(lobby, InformationRequest.INSTANCE, 1000)
            .thenApplyAsync(LobbyInformation.class::cast), Jackson.marshaller());
    }
}

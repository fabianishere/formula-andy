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

package nl.tudelft.fa.client.lobby.controller;

import nl.tudelft.fa.client.Client;
import nl.tudelft.fa.client.auth.Credentials;
import nl.tudelft.fa.client.lobby.LobbyStatus;

import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * A controller for the lobby balancer REST API for an authorized user.
 *
 * @author Fabian Mastenbroek
 */
public class AuthorizedLobbyBalancerController extends LobbyBalancerController {
    /**
     * The credentials of the user to manage the teams of.
     */
    private final Credentials credentials;

    /**
     * Construct a {@link LobbyBalancerController} instance.
     *
     * @param client The {@link Client} to use for the connection.
     * @param credentials The credentials to authorize with.
     */
    public AuthorizedLobbyBalancerController(Client client, Credentials credentials) {
        super(client);
        this.credentials = credentials;
    }
    
    /**
     * Return the controllers of the lobbies running on the server.
     *
     * @return The lobbies running on the server.
     */
    @Override
    public CompletionStage<Set<LobbyController>> controllers() {
        return lobbies()
            .thenApply(lobbies -> lobbies.stream()
                .map(lobby -> new AuthorizedLobbyController(client, mapper, lobby.getId(),
                    credentials))
                .collect(Collectors.toSet())
            );
    }

    /**
     * Return the controller of a lobby running on the server.
     *
     * @param id The identifier of the lobby.
     * @return The controller of the lobby running on the server.
     */
    public CompletionStage<LobbyController> controller(String id) {
        return lobby(id)
            .thenApply(lobby -> new LobbyController(client, mapper, lobby.getId()))
            .thenApply(controller -> controller.authorize(credentials));
    }

    /**
     * Find an available lobby controller.
     *
     * @return An available lobby controller.
     */
    public CompletionStage<LobbyController> find() {
        return lobbies().thenApply(lobbies -> lobbies.stream()
            .filter(lobby -> lobby.getStatus().equals(LobbyStatus.INTERMISSION))
            .filter(lobby -> lobby.getUsers().size() < lobby.getConfiguration()
                .getUserMaximum())
            .findFirst()
            .get()
        )
            .thenApply(lobby -> new LobbyController(client, mapper, lobby.getId()))
            .thenApply(controller -> controller.authorize(credentials));
    }
}

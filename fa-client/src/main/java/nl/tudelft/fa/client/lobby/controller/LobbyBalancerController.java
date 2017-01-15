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

import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpMethods;
import akka.http.javadsl.model.HttpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.tudelft.fa.client.Client;
import nl.tudelft.fa.client.helper.jackson.LobbyModule;
import nl.tudelft.fa.client.lobby.Lobby;
import nl.tudelft.fa.client.lobby.LobbyBalancer;
import nl.tudelft.fa.client.lobby.LobbyStatus;

import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * A controller for the lobby balancer REST API.
 *
 * @author Fabian Mastenbroek
 */
public class LobbyBalancerController {
    /**
     * The {@link Client} to use for connection.
     */
    private final Client client;

    /**
     * The {@link ObjectMapper} to use for deserialization.
     */
    private final ObjectMapper mapper;

    /**
     * Construct a {@link LobbyBalancerController} instance.
     *
     * @param client The {@link Client} to use for the connection.
     */
    public LobbyBalancerController(Client client) {
        this.client = client;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.registerModule(new LobbyModule());
    }

    /**
     * Return the information of the lobbies running on the server.
     *
     * @return The information of the lobbies running on the server.
     */
    public CompletionStage<Set<Lobby>> lobbies() {
        final HttpRequest request = HttpRequest.create()
            .withUri(client.baseUri().addPathSegment("lobbies"))
            .withMethod(HttpMethods.GET);

        return client.http().singleRequest(request, client.materializer())
            .thenCompose(res ->
                Jackson.unmarshaller(mapper, LobbyBalancer.class)
                    .unmarshall(res.entity(), client.materializer().executionContext(),
                        client.materializer())
            )
            .thenApply(LobbyBalancer::getLobbies);
    }

    /**
     * Return the controllers of the lobbies running on the server.
     *
     * @return The lobbies running on the server.
     */
    public CompletionStage<Set<LobbyController>> controllers() {
        return lobbies()
            .thenApply(lobbies -> lobbies.stream()
                .map(lobby -> new LobbyController(client, mapper, lobby.getId()))
                .collect(Collectors.toSet())
            );
    }

    /**
     * Return the information of a lobby running on the server.
     *
     * @param id The identifier of the lobby.
     * @return The information of the lobby running on the server.
     */
    public CompletionStage<Lobby> lobby(String id) {
        final HttpRequest request = HttpRequest.create()
            .withUri(client.baseUri().addPathSegment("lobbies").addPathSegment(id))
            .withMethod(HttpMethods.GET);

        return client.http().singleRequest(request, client.materializer())
            .thenCompose(res ->
                Jackson.unmarshaller(mapper, Lobby.class)
                    .unmarshall(res.entity(), client.materializer().executionContext(),
                        client.materializer())
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
            .thenApply(lobby -> new LobbyController(client, mapper, lobby.getId()));
    }

    /**
     * Find an available lobby controller.
     *
     * @return An available lobby controller.
     */
    public CompletionStage<LobbyController> find() {
        return lobbies().thenApply(lobbies -> lobbies.stream()
            .filter(lobby -> lobby.getStatus().equals(LobbyStatus.PREPARATION))
            .filter(lobby -> lobby.getUsers().size() < lobby.getConfiguration()
                .getPlayerMaximum())
            .findFirst()
            .get()
        ).thenApply(lobby -> new LobbyController(client, mapper, lobby.getId()));
    }
}

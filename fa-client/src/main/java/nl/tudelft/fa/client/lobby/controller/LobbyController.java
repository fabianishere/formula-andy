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

import akka.Done;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpMethods;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.Uri;
import akka.http.javadsl.model.headers.Authorization;
import akka.http.javadsl.model.ws.Message;
import akka.http.javadsl.model.ws.WebSocketRequest;
import akka.http.javadsl.model.ws.WebSocketUpgradeResponse;
import akka.japi.Pair;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Keep;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.tudelft.fa.client.Client;
import nl.tudelft.fa.client.auth.Credentials;
import nl.tudelft.fa.client.helper.jackson.LobbyModule;
import nl.tudelft.fa.client.lobby.Lobby;
import nl.tudelft.fa.client.lobby.message.LobbyInboundMessage;
import nl.tudelft.fa.client.lobby.message.LobbyOutboundMessage;
import nl.tudelft.fa.client.net.codec.AbstractCodec;
import nl.tudelft.fa.client.net.codec.jackson.JacksonWebSocketCodec;
import nl.tudelft.fa.client.net.message.Ping;

import java.util.concurrent.CompletionStage;

/**
 * A controller for a single lobby.
 *
 * @author Fabian Mastenbroek
 */
public class LobbyController {
    /**
     * The {@link Client} of this controller.
     */
    protected final Client client;

    /**
     * The {@link ObjectMapper} of this controller.
     */
    protected final ObjectMapper mapper;

    /**
     * The unique identifier of the lobby.
     */
    protected final String id;

    /**
     * The {@link AbstractCodec} to decode/encode the incoming/outgoing message.
     */
    protected final AbstractCodec<Message> codec;

    /**
     * Construct a {@link LobbyController} instance.
     *
     * @param client The client of this controller.
     * @param id The identifier of the lobby.
     */
    public LobbyController(Client client, String id) {
        this(client, new ObjectMapper(), id);
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.registerModule(new LobbyModule());
    }

    /**
     * Construct a {@link LobbyController} instance.
     *
     * @param client The client of this controller.
     * @param mapper The {@link ObjectMapper} to use.
     * @param id The identifier of the lobby.
     */
    public LobbyController(Client client, ObjectMapper mapper, String id) {
        this.client = client;
        this.mapper = mapper;
        this.id = id;
        this.codec = new JacksonWebSocketCodec(mapper);
    }

    /**
     * Authorize this {@link LobbyController} instance.
     *
     * @param credentials The credentials to authorize with.
     * @return The authorized {@link LobbyController} instance.
     */
    public AuthorizedLobbyController authorize(Credentials credentials) {
        return new AuthorizedLobbyController(client, mapper, id, credentials);
    }

    /**
     * Return the {@link Lobby} instance of this controller.
     *
     * @return The {@link Lobby} instance of this controller.
     */
    public CompletionStage<Lobby> get() {
        final HttpRequest request = HttpRequest.create()
            .withUri(client.getBaseUri().addPathSegment("lobbies").addPathSegment(id))
            .withMethod(HttpMethods.GET);
        return client.http().singleRequest(request, client.materializer())
            .thenCompose(res ->
                Jackson.unmarshaller(mapper, Lobby.class)
                    .unmarshall(res.entity(), client.materializer().executionContext(),
                        client.materializer())
            );
    }

    /**
     * Return the {@link WebSocketRequest} instance to retrieve the lobby's feed.
     *
     * @return The {@link WebSocketRequest} instance to retrieve the lobby's feed.
     */
    protected WebSocketRequest feedRequest() {
        final Uri uri = client.getBaseUri()
            .scheme("ws")
            .addPathSegment("lobbies")
            .addPathSegment(id)
            .addPathSegment("feed");
        return WebSocketRequest.create(uri);
    }

    /**
     * Return the event feed of the lobby.
     *
     * <p>
     *  Please be aware that in order to keep the connection open, the client has to sent
     *  {@link Ping} messages regularly.
     * </p>
     *
     * @param request The {@link WebSocketRequest} to use.
     * @return The event feed of the lobby.
     */
    protected Flow<LobbyInboundMessage, LobbyOutboundMessage,
            CompletionStage<WebSocketUpgradeResponse>> feed(WebSocketRequest request) {
        return codec.bidiFlow()
            .reversed()
            .join(client.http().webSocketClientFlow(request), Keep.right());
    }

    /**
     * Handle the event feed of the lobby with the given handler.
     *
     * <p>
     *  Please be aware that in order to keep the connection open, the client has to sent
     *  {@link Ping} messages regularly.
     * </p>
     *
     * @param handler The flow to handle the events with.
     * @return The result of the WebSocket upgrade and a completion stage that succeeds on
     *     completion of the flow.
     */
    protected Pair<CompletionStage<WebSocketUpgradeResponse>, CompletionStage<Done>> feed(
            WebSocketRequest request, Flow<LobbyOutboundMessage, LobbyInboundMessage,
            CompletionStage<Done>> handler) {
        return client.http()
            .singleWebSocketRequest(request, codec.bidiFlow().join(handler, Keep.right()),
                client.materializer());
    }

    /**
     * Return the event feed of the lobby.
     *
     * <p>
     *  Please be aware that in order to keep the connection open, the client has to sent
     *  {@link Ping} messages regularly.
     * </p>
     *
     * @return The event feed of the lobby.
     */
    public Flow<LobbyInboundMessage, LobbyOutboundMessage,
            CompletionStage<WebSocketUpgradeResponse>> feed() {
        return feed(feedRequest());
    }

    /**
     * Handle the event feed of the lobby with the given handler.
     *
     * <p>
     *  Please be aware that in order to keep the connection open, the client has to sent
     *  {@link Ping} messages regularly.
     * </p>
     *
     * @param handler The flow to handle the events with.
     * @return The result of the WebSocket upgrade and a completion stage that succeeds on
     *     completion of the flow.
     */
    public Pair<CompletionStage<WebSocketUpgradeResponse>, CompletionStage<Done>> feed(
            Flow<LobbyOutboundMessage, LobbyInboundMessage, CompletionStage<Done>> handler) {
        return feed(feedRequest(), handler);
    }
}

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

package nl.tudelft.fa.client;

import akka.Done;
import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpMethods;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.Uri;
import akka.http.javadsl.model.headers.Authorization;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.fa.client.auth.Credentials;
import nl.tudelft.fa.client.helper.jackson.UserModule;
import nl.tudelft.fa.client.lobby.controller.AuthorizedLobbyBalancerController;
import nl.tudelft.fa.client.lobby.controller.LobbyBalancerController;
import nl.tudelft.fa.client.team.controller.TeamController;
import nl.tudelft.fa.client.user.User;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * This class provides the HTTP connection between client and server.
 *
 * @author Fabian Mastenbroek
 */
public class Client extends AbstractClient {
    /**
     * The {@link Http} instance of this client.
     */
    private final Http http;

    /**
     * The {@link Materializer} to use to materialize the requests.
     */
    private final Materializer materializer;

    /**
     * The credentials the user to authenticate with.
     */
    private final Credentials credentials;

    /**
     * The {@link ObjectMapper} of teh client.
     */
    private final ObjectMapper mapper;

    /**
     * Construct a {@link Client} instance.
     *
     * @param http The {@link Http} instance to use.
     * @param materializer The {@link Materializer} to use.
     * @param baseUri The base uri of the server.
     * @param credentials The credentials to authorize with.
     */
    public Client(Http http, Materializer materializer, Uri baseUri, Credentials credentials) {
        super(baseUri);
        this.http = http;
        this.materializer = materializer;
        this.credentials = credentials;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new UserModule());
    }

    /**
     * Construct a {@link Client} instance.
     *
     * @param system The {@link ActorSystem} to use.
     * @param baseUri The base uri of the server.
     * @param credentials The credentials to authorize with.
     */
    public Client(ActorSystem system, Uri baseUri, Credentials credentials) {
        this(Http.get(system), ActorMaterializer.create(system), baseUri, credentials);
    }

    /**
     * Return the {@link Http} instance of this client.
     *
     * @return The {@link Http} instance of the client.
     */
    public Http http() {
        return http;
    }

    /**
     * Return the {@link Materializer} of the client.
     *
     * @return The materializer of the client.
     */
    public Materializer materializer() {
        return materializer;
    }

    /**
     * Return the {@link LobbyBalancerController} instance of the client.
     *
     * @return A {@link LobbyBalancerController} instance.
     */
    @Override
    public LobbyBalancerController balancer() {
        return new AuthorizedLobbyBalancerController(this, credentials);
    }

    /**
     * Return the {@link TeamController} for the user.
     *
     * @return The team controller for the user.
     */
    @Override
    public TeamController teams() {
        return new TeamController(this, credentials);
    }

    /**
     * Register a user with the given credentials.
     *
     * @param credentials The credentials to register the user with.
     * @return A completion stage that completes with the registered user.
     */
    public CompletionStage<User> register(Credentials credentials) {
        final HttpRequest request;

        try {
            request = HttpRequest.create()
                .withUri(getBaseUri().addPathSegment("users"))
                .withMethod(HttpMethods.POST)
                .withEntity(ContentTypes.APPLICATION_JSON, mapper.writeValueAsBytes(credentials));
        } catch (JsonProcessingException exc) {
            CompletableFuture<User> future = new CompletableFuture<>();
            future.completeExceptionally(exc);
            return future;
        }

        return http.singleRequest(request, materializer)
            .thenCompose(res ->
                Jackson.unmarshaller(mapper, User.class)
                    .unmarshall(res.entity(), materializer.executionContext(), materializer)
            );
    }

    /**
     * Test whether the connection with the server is valid.
     *
     * @return A completion stage that completes if the connection is valid.
     */
    @Override
    public CompletionStage<Done> test() {
        final HttpRequest request = HttpRequest.create()
            .withUri(getBaseUri().addPathSegment("auth").addPathSegment("basic"))
            .withMethod(HttpMethods.GET)
            .addHeader(Authorization.basic(credentials.getUsername(), credentials.getPassword()));

        return http.singleRequest(request, materializer)
            .thenCompose(res -> {
                if (res.status().isFailure()) {
                    CompletableFuture<Done> future = new CompletableFuture<>();
                    future.completeExceptionally(new Exception(res.status().reason()));
                    return future;
                }

                res.entity().discardBytes(materializer);
                return CompletableFuture.completedFuture(Done.getInstance());
            });
    }

    /**
     * Create a {@link Client} instance.
     *
     * @param baseUri The base uri of the server.
     * @param credentials The credentials to authorize with.
     */
    public static Client create(Uri baseUri, Credentials credentials) {
        return new Client(ActorSystem.create(), baseUri, credentials);
    }
}

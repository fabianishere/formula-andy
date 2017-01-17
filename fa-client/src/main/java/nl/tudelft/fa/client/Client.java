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

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.Uri;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import nl.tudelft.fa.client.auth.Credentials;
import nl.tudelft.fa.client.lobby.controller.LobbyBalancerController;

/**
 * This class provides the HTTP connection between client and server.
 *
 * @author Fabian Mastenbroek
 */
public class Client {
    /**
     * The {@link Http} instance of this client.
     */
    private final Http http;

    /**
     * The {@link Materializer} to use to materialize the requests.
     */
    private final Materializer materializer;

    /**
     * The base {@link Uri} to use.
     */
    private final Uri baseUri;

    /**
     * Construct a {@link Client} instance.
     *
     * @param http The {@link Http} instance to use.
     * @param materializer The {@link Materializer} to use.
     * @param baseUri The base uri of the server.
     */
    public Client(Http http, Materializer materializer, Uri baseUri) {
        this.http = http;
        this.materializer = materializer;
        this.baseUri = baseUri;
    }

    /**
     * Construct a {@link Client} instance.
     *
     * @param system The {@link ActorSystem} to use.
     * @param baseUri The base uri of the server.
     */
    public Client(ActorSystem system, Uri baseUri) {
        this(Http.get(system), ActorMaterializer.create(system), baseUri);
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
     * Return the base {@link Uri} of the client.
     *
     * @return The base uri of the client.
     */
    public Uri baseUri() {
        return baseUri;
    }

    /**
     * Authorize this {@link Client} instance.
     *
     * @param credentials The credentials to authorize with.
     * @return The authorized client.
     */
    public AuthorizedClient authorize(Credentials credentials) {
        return new AuthorizedClient(http, materializer, baseUri, credentials);
    }

    /**
     * Return the {@link LobbyBalancerController} instance of the client.
     *
     * @return A {@link LobbyBalancerController} instance.
     */
    public LobbyBalancerController balancer() {
        return new LobbyBalancerController(this);
    }
}

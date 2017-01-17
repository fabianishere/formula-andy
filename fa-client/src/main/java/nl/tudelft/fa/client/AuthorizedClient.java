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
import nl.tudelft.fa.client.lobby.controller.AuthorizedLobbyBalancerController;
import nl.tudelft.fa.client.lobby.controller.LobbyBalancerController;
import nl.tudelft.fa.client.team.controller.TeamController;

/**
 * This class provides the HTTP connection between client and server that is authorized.
 *
 * @author Fabian Mastenbroek
 */
public class AuthorizedClient extends Client {
    /**
     * The credentials of the user to manage the teams of.
     */
    private final Credentials credentials;

    /**
     * Construct a {@link AuthorizedClient} instance.
     *
     * @param http         The {@link Http} instance to use.
     * @param materializer The {@link Materializer} to use.
     * @param baseUri      The base uri of the server.
     * @param credentials  The credentials to authorize with.
     */
    public AuthorizedClient(Http http, Materializer materializer, Uri baseUri,
                            Credentials credentials) {
        super(http, materializer, baseUri);
        this.credentials = credentials;
    }

    /**
     * Construct a {@link AuthorizedClient} instance.
     *
     * @param system      The {@link ActorSystem} to use.
     * @param baseUri     The base uri of the server.
     * @param credentials The credentials to authorize with.
     */
    public AuthorizedClient(ActorSystem system, Uri baseUri, Credentials credentials) {
        this(Http.get(system), ActorMaterializer.create(system), baseUri, credentials);
    }

    /**
     * Return the {@link LobbyBalancerController} instance of the client.
     *
     * @return A {@link LobbyBalancerController} instance.
     */
    public LobbyBalancerController balancer() {
        return new AuthorizedLobbyBalancerController(this, credentials);
    }

    /**
     * Return the {@link TeamController} for the user.
     *
     * @return The team controller for the user.
     */
    public TeamController teams() {
        return new TeamController(this, credentials);
    }
}

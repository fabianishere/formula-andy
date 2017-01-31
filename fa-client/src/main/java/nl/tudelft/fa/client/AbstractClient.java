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
import akka.http.javadsl.model.Uri;
import nl.tudelft.fa.client.auth.Credentials;
import nl.tudelft.fa.client.lobby.controller.LobbyBalancerController;
import nl.tudelft.fa.client.net.message.NotAuthorizedException;
import nl.tudelft.fa.client.team.controller.TeamController;
import nl.tudelft.fa.client.user.User;

import java.util.concurrent.CompletionStage;

/**
 * This class represents the contract a client adheres to.
 *
 * @author Fabian Mastenbroek
 */
public abstract class AbstractClient {
    /**
     * The base uri of the server.
     */
    private final Uri baseUri;

    /**
     * Construct a {@link AbstractClient} instance.
     *
     * @param baseUri The base uri of the server.
     */
    public AbstractClient(Uri baseUri) {
        this.baseUri = baseUri;
    }

    /**
     * Return the {@link LobbyBalancerController} instance of the client.
     *
     * @return A {@link LobbyBalancerController} instance.
     */
    public abstract LobbyBalancerController balancer();

    /**
     * Return the {@link TeamController} for the user that manages the teams of the user.
     *
     * @return The team controller for the user.
     * @throws NotAuthorizedException if the user is not authorized.
     */
    public abstract TeamController teams() throws NotAuthorizedException;

    /**
     * Register a user with the given credentials.
     *
     * @param credentials The credentials to register the user with.
     * @return A completion stage that completes with the registered user.
     */
    public abstract CompletionStage<User> register(Credentials credentials);

    /**
     * Test whether the connection with the server is valid.
     *
     * @return A completion stage that completes if the connection is valid.
     */
    public abstract CompletionStage<Done> test();

    /**
     * Return the base uri of the server.
     *
     * @return The base uri of the server.
     */
    public Uri getBaseUri() {
        return baseUri;
    }
}

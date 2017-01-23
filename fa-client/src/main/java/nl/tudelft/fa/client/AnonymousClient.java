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
import nl.tudelft.fa.client.lobby.controller.LobbyBalancerController;
import nl.tudelft.fa.client.net.message.NotAuthorizedException;
import nl.tudelft.fa.client.team.controller.TeamController;

/**
 * This class provides the HTTP connection between client and server that is anonymous.
 *
 * @author Fabian Mastenbroek
 */
public class AnonymousClient extends AbstractClient {
    /**
     * The underlying {@link Client} to use.
     */
    private final Client client;

    /**
     * Construct a {@link AnonymousClient} instance.
     *
     * @param http         The {@link Http} instance to use.
     * @param materializer The {@link Materializer} to use.
     * @param baseUri      The base uri of the server.
     */
    public AnonymousClient(Http http, Materializer materializer, Uri baseUri) {
        super(baseUri);
        this.client = new Client(http, materializer, baseUri, null);
    }

    /**
     * Construct a {@link AnonymousClient} instance.
     *
     * @param system      The {@link ActorSystem} to use.
     * @param baseUri     The base uri of the server.
     */
    public AnonymousClient(ActorSystem system, Uri baseUri) {
        this(Http.get(system), ActorMaterializer.create(system), baseUri);
    }

    /**
     * Return the {@link LobbyBalancerController} instance of the client.
     *
     * @return A {@link LobbyBalancerController} instance.
     */
    @Override
    public LobbyBalancerController balancer() {
        return new LobbyBalancerController(client);
    }

    /**
     * Return the {@link TeamController} for the user.
     *
     * @return The team controller for the user.
     */
    @Override
    public TeamController teams() throws NotAuthorizedException {
        throw new NotAuthorizedException();
    }

    /**
     * Create a {@link AnonymousClient} instance.
     *
     * @param baseUri The base uri of the server.
     */
    public static AnonymousClient create(Uri baseUri) {
        return new AnonymousClient(ActorSystem.create(), baseUri);
    }
}

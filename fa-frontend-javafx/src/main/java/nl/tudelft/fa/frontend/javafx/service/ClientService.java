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

package nl.tudelft.fa.frontend.javafx.service;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.Uri;
import akka.stream.javadsl.Flow;
import nl.tudelft.fa.client.AbstractClient;
import nl.tudelft.fa.client.AnonymousClient;
import nl.tudelft.fa.client.Client;
import nl.tudelft.fa.client.auth.Credentials;
import nl.tudelft.fa.client.lobby.controller.LobbyBalancerController;
import nl.tudelft.fa.client.lobby.controller.LobbyController;
import nl.tudelft.fa.client.lobby.message.LobbyInboundMessage;
import nl.tudelft.fa.client.lobby.message.LobbyOutboundMessage;
import nl.tudelft.fa.client.net.message.NotAuthorizedException;
import nl.tudelft.fa.client.team.controller.TeamController;
import nl.tudelft.fa.frontend.javafx.net.SessionActor;
import nl.tudelft.fa.frontend.javafx.net.SessionStage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * This service provides the {@link Client} instance for the controllers.
 *
 * @author Fabian Mastenbroek
 */
public class ClientService extends AbstractClient {
    /**
     * The underlying {@link Client} instance to do the operations.
     */
    private AbstractClient client;

    /**
     * The reference to the {@link SessionActor} handling the session with a lobby.
     */
    private ActorRef session;

    /**
     * The {@link ActorSystem} to use.
     */
    private ActorSystem system;

    /**
     * Construct a {@link ClientService} instance.
     *
     * @param baseUri The base uri of the server.
     */
    public ClientService(Uri baseUri) {
        super(baseUri);
        this.system = ActorSystem.create();
        this.client = new AnonymousClient(system, baseUri);
    }

    /**
     * Authorize the user with the given credentials.
     *
     * @param credentials The credentials to authorize with.
     */
    public void authorize(Credentials credentials) {
        client = new Client(system, getBaseUri(), credentials);
        // TODO throw on invalid credentials
    }

    /**
     * Open a session for the given {@link LobbyController} instance.
     *
     * @param controller The controller of the lobby to open a session for.
     * @return The reference to the lobby session actor.
     */
    public CompletionStage<ActorRef> open(LobbyController controller) {
        session = system.actorOf(SessionActor.props());

        Flow<LobbyOutboundMessage, LobbyInboundMessage, CompletionStage<Done>> flow = Flow
            .fromGraph(new SessionStage(session))
            .mapMaterializedValue(mat -> CompletableFuture.completedFuture(Done.getInstance()));
        return controller.feed(flow)
            .first()
            .thenApply(done -> session);
    }

    /**
     * Return the current active session with a lobby.
     *
     * @return The current active session with a lobby.
     */
    public ActorRef session() {
        return session;
    }

    /**
     * Return the {@link ActorSystem} of this instance.
     *
     * @return The {@link ActorSystem} of this instance.
     */
    public ActorSystem system() {
        return system;
    }

    @Override
    public LobbyBalancerController balancer() {
        return client.balancer();
    }

    @Override
    public TeamController teams() throws NotAuthorizedException {
        return client.teams();
    }
}

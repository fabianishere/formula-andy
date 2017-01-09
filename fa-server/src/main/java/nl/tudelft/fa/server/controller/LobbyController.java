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

package nl.tudelft.fa.server.controller;

import static akka.http.javadsl.server.Directives.*;
import static akka.http.javadsl.server.PathMatchers.uuidSegment;
import static scala.compat.java8.JFunction.func;

import akka.NotUsed;
import akka.actor.ActorNotFound;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.ws.Message;
import akka.http.javadsl.server.Route;
import akka.japi.pf.PFBuilder;
import akka.pattern.PatternsCS;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.tudelft.fa.core.lobby.Lobby;
import nl.tudelft.fa.core.lobby.LobbyBalancer;
import nl.tudelft.fa.core.lobby.actor.LobbyActor;
import nl.tudelft.fa.core.lobby.actor.LobbyBalancerActor;
import nl.tudelft.fa.core.lobby.message.RequestInformation;
import nl.tudelft.fa.server.helper.jackson.LobbyModule;
import scala.concurrent.duration.FiniteDuration;

import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * This controller acts as an interface between a {@link LobbyBalancerActor} and the REST API which
 * controls the lobbies.
 *
 * @author Fabian Mastenbroek
 */
public class LobbyController {
    /**
     * The {@link ActorSystem} to use.
     */
    private final ActorSystem system;

    /**
     * The reference to the {@link LobbyBalancerActor} actor.
     */
    private final ActorRef balancer;

    /**
     * The {@link ObjectMapper} to use for serialization.
     */
    private final ObjectMapper mapper;

    /**
     * Construct a {@link LobbyController} instance.
     *
     * @param system The {@link ActorSystem}  of the balancer.
     * @param balancer The reference to the {@link LobbyBalancerActor} actor.
     */
    public LobbyController(ActorSystem system, ActorRef balancer) {
        this.system = system;
        this.balancer = balancer;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new LobbyModule());
        this.mapper.registerModule(new JavaTimeModule()); // Duration (de)serialization
    }

    /**
     * Create the {@link Route} instance for this controller.
     *
     * @return The routes for this controller.
     */
    public Route createRoute() {
        // Show the information about the balancer
        return route(
            pathEndOrSingleSlash(this::list),
            pathPrefix(uuidSegment(), this::lobby)
        );
    }

    /**
     * Return the {@link Route} instance to list the {@link LobbyActor} actor instances of this
     * balancer.
     *
     * @return The route that lists the instances.
     */
    public Route list() {
        return route(
            get(() ->
                completeOKWithFuture(PatternsCS.ask(balancer, RequestInformation.INSTANCE, 1000)
                        .thenApplyAsync(LobbyBalancer.class::cast),
                    Jackson.marshaller(mapper))
            )
        );
    }

    /**
     * Return the middleware {@link Route} instance that resolves a lobby given a {@link UUID}.
     * In-case the lobby could not be resolved, the route will be rejected.
     *
     * @param id The unique identifier of the lobby.
     * @param producer A function that produces a route based on the resolved lobby.
     */
    public Route middleware(UUID id, Function<ActorRef, Route> producer) {
        final CompletionStage<ActorRef> cs = system
            .actorSelection(balancer.path().child(id.toString()))
            .resolveOneCS(FiniteDuration.create(1, "second"));

        return onComplete(cs, res -> res
            .map(func(producer::apply))
            .recover(new PFBuilder<Throwable, Route>()
                .match(ActorNotFound.class, ex -> reject())
                .build()
            )
            .get()
        );
    }

    /**
     * Return the {@link Route} instance for a {@link LobbyActor}.
     *
     * @param id The unique identifier of the lobby.
     */
    public Route lobby(UUID id) {
        return middleware(id, lobby -> route(
            pathEndOrSingleSlash(() -> information(lobby)),
            path("feed", () -> feed(lobby))
        ));
    }

    /**
     * Return the {@link Route} instance for the feed of a {@link LobbyActor}.
     *
     * @param lobby The lobby to get the feed of.
     */
    public Route feed(ActorRef lobby) {
        return get(() -> handleWebSocketMessages(feedHandler(lobby)));
    }

    /**
     * Return the {@link Flow} that handles the messages in the lobby's feed.
     *
     * @param lobby The lobby to get the feed of.
     * @return The {@link Flow} that handles the messages in the lobby's feed.
     */
    public static Flow<Message, Message, NotUsed> feedHandler(ActorRef lobby) {
        return Flow.fromSinkAndSource(Sink.ignore(), Source.empty());
    }

    /**
     * Return the {@link Route} instance that shows the information of a single {@link LobbyActor}
     * instance.
     *
     * @param lobby The lobby to show the information of.
     */
    public Route information(ActorRef lobby) {
        final CompletionStage<Lobby> cs = PatternsCS.ask(lobby, RequestInformation.INSTANCE, 1000)
            .thenApply(Lobby.class::cast);
        return completeOKWithFuture(cs, Jackson.marshaller(mapper));
    }
}

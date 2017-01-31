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
import static akka.pattern.PatternsCS.ask;

import akka.actor.ActorRef;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.server.directives.SecurityDirectives;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.fa.core.auth.actor.Authenticator;
import nl.tudelft.fa.core.auth.message.AuthenticationRequest;
import nl.tudelft.fa.core.auth.message.AuthenticationSuccess;
import nl.tudelft.fa.core.user.User;
import nl.tudelft.fa.server.helper.jackson.LobbyModule;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import javax.persistence.EntityManager;

/**
 * A controller that allows a user to query his teams.
 *
 * @author Fabian Mastenbroek
 */
public class TeamController {
    /**
     * The reference to the {@link Authenticator} actor.
     */
    private final ActorRef authenticator;

    /**
     * The {@link EntityManager} to use.
     */
    private final EntityManager entityManager;

    /**
     * The {@link ObjectMapper} to use for serialization.
     */
    private final ObjectMapper mapper;

    /**
     * Construct a {@link TeamController} instance.
     *
     * @param authenticator The reference to the {@link Authenticator} actor.
     * @param entityManager The {@link EntityManager} to use.
     */
    public TeamController(ActorRef authenticator, EntityManager entityManager) {
        this.authenticator = authenticator;
        this.entityManager = entityManager;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new LobbyModule(entityManager));
    }

    /**
     * Create the {@link Route} instance for this controller.
     *
     * @return The routes for this controller.
     */
    public Route createRoute() {
        // Show the information about the balancer
        return authenticate(user -> route(
            pathEndOrSingleSlash(() -> list(user)),
            pathPrefix(uuidSegment(), id -> middleware(id, team -> route(
                pathEndOrSingleSlash(() -> information(team))
            )))
        ));
    }


    /**
     * Return the middleware {@link Route} instance that authenticates the user using HTTP Basic
     * authentication.
     *
     * @param inner The inner route producer to use on success.
     * @return The {@link Route} instance that authenticates the user.
     */
    public Route authenticate(Function<User, Route> inner) {
        return authenticateBasicAsync("fa", this::authenticate, inner);
    }

    /**
     * Authenticate the user with the {@link SecurityDirectives.ProvidedCredentials} instance.
     *
     * @param opt The optional credentials to authenticate with.
     * @return The {@link CompletionStage} of the authentication returning the user.
     */
    public CompletionStage<Optional<User>> authenticate(
            Optional<SecurityDirectives.ProvidedCredentials> opt) {
        return opt.map(credentials ->
            ask(authenticator, new AuthenticationRequest(credentials.identifier(), user ->
                credentials.verify(user.getCredentials().getPassword())), 1000)
                .thenApply(AuthenticationSuccess.class::cast)
                .thenApply(AuthenticationSuccess::getUser)
                .thenApply(Optional::of)
                .exceptionally(exc -> Optional.empty())
        ).orElse(CompletableFuture.completedFuture(Optional.empty()));
    }

    /**
     * Return the middleware {@link Route} instance that resolves a team given a {@link UUID}.
     * In-case that team could not be resolved, the route will be rejected.
     *
     * @param id The unique identifier of the team.
     * @param producer A function that produces a route based on the resolved team.
     */
    public Route middleware(UUID id, Function<nl.tudelft.fa.core.team.Team, Route> producer) {
        Optional<nl.tudelft.fa.core.team.Team> team = entityManager
            .createQuery("SELECT t FROM Teams t where t.id = :id",
                nl.tudelft.fa.core.team.Team.class)
            .setParameter("id", id)
            .getResultList()
            .stream()
            .findFirst();

        return team.map(producer).orElse(reject());
    }

    /**
     * Return the {@link Route} instance to list the {@link nl.tudelft.fa.core.team.Team} instances
     * of the user.
     *
     * @param user The user to list the {@link nl.tudelft.fa.core.team.Team}s of.
     * @return The route that lists the teams.
     */
    public Route list(User user) {
        return route(
            get(() ->
                completeOK(entityManager
                    .createQuery("SELECT t FROM Teams t where t.owner = :owner",
                        nl.tudelft.fa.core.team.Team.class)
                    .setParameter("owner", user)
                    .getResultList(), Jackson.marshaller(mapper))
            )
        );
    }

    /**
     * Return the {@link Route} instance that shows the information of a single team.
     *
     * @param team The team to show the information of.
     * @return The route that shows the information of a team.
     */
    public Route information(nl.tudelft.fa.core.team.Team team) {
        return completeOK(team, Jackson.marshaller(mapper));
    }
}

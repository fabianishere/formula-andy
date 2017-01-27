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

package nl.tudelft.fa.server;

import static akka.http.javadsl.server.Directives.*;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.Route;
import nl.tudelft.fa.core.auth.actor.Authenticator;
import nl.tudelft.fa.core.lobby.actor.LobbyBalancerActor;
import nl.tudelft.fa.server.controller.LobbyController;
import nl.tudelft.fa.server.controller.TeamController;
import nl.tudelft.fa.server.model.Information;

import java.lang.management.ManagementFactory;
import javax.persistence.EntityManager;

/**
 * This class provides the main {@link Route}s for the Formula Andy Akka HTTP REST API.
 *
 * @author Fabian Mastenbroek
 */
public class RestService {
    /**
     * The name of the engine.
     */
    public static final String ENGINE_NAME = "Formula Andy X";

    /**
     * The version of the engine.
     */
    public static final String ENGINE_VERSION = "1.0-SNAPSHOT";

    /**
     * The {@link ActorSystem} of this {@link RestService}.
     */
    private final ActorSystem system;

    /**
     * The controller that manages the lobbies.
     */
    private final LobbyController lobbies;

    /**
     * The controller that manages the teams.
     */
    private final TeamController teams;

    /**
     * Construct a {@link RestService} instance.
     *
     * @param system The {@link ActorSystem} instance to use.
     * @param authenticator The reference to the {@link Authenticator} actor.
     * @param balancer The reference to the {@link LobbyBalancerActor}.
     * @param entityManager Th e{@link EntityManager} to use.
     */
    public RestService(ActorSystem system, ActorRef authenticator, ActorRef balancer,
                       EntityManager entityManager) {
        this.system = system;
        this.lobbies = new LobbyController(system, authenticator, balancer, entityManager);
        this.teams = new TeamController(authenticator, entityManager);
    }

    /**
     * Create the main {@link Route} for the REST API server.
     *
     * @return The route of the REST API.
     */
    public Route createRoute() {
        return route(
            path("information", this::information),
            pathPrefix("lobbies", lobbies::createRoute),
            pathPrefix("teams", teams::createRoute)
        );
    }

    /**
     * This route shows the information of this REST API server.
     *
     * @return The route that shows the information.
     */
    private Route information() {
        return complete(
            StatusCodes.OK,
            new Information(ENGINE_NAME, ENGINE_VERSION,
                ManagementFactory.getRuntimeMXBean().getUptime()),
            Jackson.marshaller()
        );
    }
}

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

package nl.tudelft.fa.core.lobby.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import nl.tudelft.fa.core.lobby.Lobby;
import nl.tudelft.fa.core.lobby.LobbyBalancer;
import nl.tudelft.fa.core.lobby.LobbyConfiguration;
import nl.tudelft.fa.core.lobby.LobbyStatus;
import nl.tudelft.fa.core.lobby.message.*;
import nl.tudelft.fa.core.user.User;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.util.*;

/**
 * A {@link LobbyBalancerActor} actor balances users between {@link LobbyActor} actors to create
 * balanced games. It will spin up new instances if needed.
 *
 * @author Fabian Mastenbroek
 */
public class LobbyBalancerActor extends AbstractActor {
    /**
     * The configuration to use when spinning up new {@link LobbyActor} actors.
     */
    private final LobbyConfiguration configuration;

    /**
     * The minimum amount of instances that should be running at all times.
     */
    private final int min;

    /**
     * The maximum amount of instances that are allowed to be active.
     */
    private final int max;

    /**
     * The instances this balancer manages.
     */
    private final Map<ActorRef, Lobby> instances;

    /**
     * The available instances.
     */
    private final Map<ActorRef, Lobby> available;

    /**
     * The {@link LoggingAdapter} of this class.
     */
    private final LoggingAdapter log = Logging.getLogger(context().system(), this);

    /**
     * Construct a {@link LobbyBalancerActor} instance.
     *
     * @param configuration The configuration of the instances.
     * @param min The minimum amount of instances that should be running at all times.
     * @param max The maximum amount of instances that are allowed to be running.
     */
    private LobbyBalancerActor(LobbyConfiguration configuration, int min, int max) {
        this.configuration = configuration;
        this.min = min;
        this.max = max;
        this.instances = new HashMap<>(max);
        this.available = new HashMap<>(max);
    }

    /**
     * This method is invoked before the start of this actor.
     */
    @Override
    public void preStart() {
        // Spin up minimum amount of instances
        for (int i = 0; i < min; i++) {
            createLobby();
        }
    }

    /**
     * This method defines the initial actor behavior, it must return a partial function with the
     * actor logic.
     *
     * @return The initial actor behavior as a partial function.
     */
    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return ReceiveBuilder
            .match(RequestInformation.class, req -> inform())
            .match(Join.class, this::join)
            .match(UserJoined.class, req -> joined(req.getUser(), sender()))
            .match(UserLeft.class, req -> left(req.getUser(), sender()))
            .match(Lobby.class, this::update)
            .match(Refresh.class, req -> refresh())
            .build();
    }

    /**
     * Inform an actor about the current state of this {@link LobbyActor}.
     */
    private void inform() {
        sender().tell(new LobbyBalancer(Collections.unmodifiableMap(instances)), self());
    }

    /**
     * Route a {@link Join} to a fitting {@link LobbyActor} actor.
     *
     * @param req The join request to handle.
     */
    private void join(Join req) {
        final ActorRef ref;

        if (!available.isEmpty()) {
            ref = instances.keySet().iterator().next();
        } else if (instances.size() < max) {
            log.info("Spinning up new lobby to meet demand");
            ref = createLobby();
        } else {
            log.warning("Balancer exhausted. Cannot fulfil request {} ", req);
            sender().tell(new LobbyBalancerExhaustedException(self()), self());
            return;
        }

        log.debug("Routing request {} from {} to {}", req, sender(), ref);

        // Create a mediator to handle the request
        ActorRef mediator = context().actorOf(LobbyBalancerMediator.props(self(), req));
        ref.tell(req, mediator);

        // Update our cache until we get confirmation
        Lobby info = instances.get(ref);
        Set<User> users = new HashSet<>(info.getUsers());
        users.add(req.getUser());
        info = new Lobby(ref.path().name(), info.getStatus(), info.getConfiguration(), users);
        instances.put(ref, info);
        available.put(ref, info);
    }

    /**
     * This method is invoked whenever a user has joined a lobby.
     *
     * @param user The user that has joined a lobby.
     * @param lobby The lobby the user has joined.
     */
    private void joined(User user, ActorRef lobby) {
        Lobby info = instances.get(lobby);
        Set<User> users = new HashSet<>(info.getUsers());
        users.add(user);
        update(new Lobby(lobby.path().name(), info.getStatus(), info.getConfiguration(), users));
    }

    /**
     * This method is invoked whenever a user has left a lobby.
     *
     * @param user The user that has left a lobby.
     * @param lobby The lobby the user has left.
     */
    private void left(User user, ActorRef lobby) {
        Lobby info = instances.get(lobby);
        Set<User> users = new HashSet<>(info.getUsers());
        users.remove(user);
        update(new Lobby(lobby.path().name(), info.getStatus(), info.getConfiguration(), users));
    }

    /**
     * Update the information of each {@link LobbyActor} this balancer manages.
     *
     * @param information The information that has been received.
     */
    private void update(Lobby information) {
        if (information.getUsers().size() == 0 && instances.size() > min) {
            log.debug("Lobby {} is empty and will be killed", sender());

            // Kill the lobby if it is empty
            context().stop(sender());
            instances.remove(sender());
            available.remove(sender());
            return;
        } else if (information.getUsers().size()
                == information.getConfiguration().getPlayerMaximum()) {
            log.debug("Lobby {} is full and will be forgotten", sender());

            // Forget the lobby if it is full
            available.remove(sender());
        } else {
            // Update the information of the available lobby
            available.put(sender(), information);
        }
        instances.put(sender(), information);
    }

    /**
     * Refresh the caches of this {@link LobbyBalancerActor}.
     */
    private void refresh() {
        instances.forEach((instance, info) -> instance.tell(RequestInformation.INSTANCE, self()));
    }

    /**
     * Spin up a new {@link LobbyActor} instance.
     *
     * @return The reference to the {@link LobbyActor} actor that has been created.
     */
    private ActorRef createLobby() {
        ActorRef ref = context().actorOf(LobbyActor.props(configuration));
        Lobby info = new Lobby(ref.path().name(), LobbyStatus.PREPARATION, configuration,
            Collections.emptySet());
        ref.tell(new Subscribe(self()), self());
        instances.put(ref, info);
        available.put(ref, info);
        return ref;
    }

    /**
     * Create {@link Props} instance for an actor of this type.
     *
     * @param configuration The configuration of the lobby.
     * @param min The minimum amount of instances that should be running at all times.
     * @param max The maximum amount of instances that are allowed to be active.
     * @return A Props for creating this actor, which can then be further configured
     *         (e.g. calling `.withDispatcher()` on it)
     */
    public static Props props(LobbyConfiguration configuration, int min, int max) {
        return Props.create(LobbyBalancerActor.class,
            () -> new LobbyBalancerActor(configuration, min, max));
    }

    /**
     * Create {@link Props} instance for an actor of this type.
     *
     * <p>
     *     This balancer will create at most 10 lobbies and at least 2 lobbies are active are
     *     active at all times.
     * </p>
     *
     * @param configuration The configuration of the lobby.
     * @return A Props for creating this actor, which can then be further configured
     *         (e.g. calling `.withDispatcher()` on it)
     */
    public static Props props(LobbyConfiguration configuration) {
        return props(configuration, 2, 10);
    }
}

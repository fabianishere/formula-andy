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
import nl.tudelft.fa.core.lobby.LobbyConfiguration;
import nl.tudelft.fa.core.lobby.LobbyInformation;
import nl.tudelft.fa.core.lobby.LobbyStatus;
import nl.tudelft.fa.core.lobby.message.*;
import nl.tudelft.fa.core.user.User;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.util.*;

/**
 * This actor represents a game lobby with multiple users.
 *
 * @author Fabian Mastenbroek
 */
public class Lobby extends AbstractActor {
    /**
     * The configuration of the lobby.
     */
    private LobbyConfiguration configuration;

    /**
     * The users that have joined this lobby.
     */
    private Map<User, ActorRef> users;

    /**
     * The event bus this {@link Lobby} uses to publish updates.
     */
    private ActorRef bus;

    /**
     * The {@link LoggingAdapter} of this class.
     */
    private final LoggingAdapter log = Logging.getLogger(context().system(), this);

    /**
     * Construct a {@link Lobby} instance.
     *
     * @param configuration The configuration of the lobby.
     */
    private Lobby(LobbyConfiguration configuration) {
        this.configuration = configuration;
        this.users = new HashMap<>(configuration.getPlayerMaximum());
        this.bus = context().actorOf(LobbyEventBus.props(), "event-bus");
    }

    /**
     * This method defines the initial actor behavior, it must return a partial function with the
     * actor logic.
     *
     * @return The initial actor behavior as a partial function.
     */
    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return preparation();
    }

    /**
     * This method defines the behavior of the actor when the lobby is in preparation.
     *
     * @return The preparation actor behavior as a partial function.
     */
    private PartialFunction<Object, BoxedUnit> preparation() {
        return ReceiveBuilder
            .match(RequestInformation.class, req -> inform(LobbyStatus.PREPARATION))
            .match(Join.class, req -> join(req.getUser(), req.getHandler()))
            .match(Leave.class, req -> leave(req.getUser()))
            .match(Subscribe.class, req -> bus.tell(req, sender()))
            .match(Unsubscribe.class, req -> bus.tell(req, sender()))
            .build();
    }

    /**
     * Inform an actor about the current state of this {@link Lobby}.
     *
     * @param status The current status of the lobby.
     */
    private void inform(LobbyStatus status) {
        sender().tell(getInformation(status), self());
    }

    /**
     * Let a {@link User} join this lobby by responding with either a {@link JoinException} message
     * or a {@link JoinSuccess} message.
     *
     * @param user The user that wants to join.
     * @param handler The handler of the user.
     */
    private void join(User user, ActorRef handler) {
        if (users.size() >= configuration.getPlayerMaximum()) {
            log.warning("The user {} failed to join because the lobby is full");

            // The lobby has reached its maximum capacity
            sender().tell(new LobbyFullException(self(), users.size()), self());
            return;
        }

        // Put the user in the lobby
        users.put(user, handler);

        // Inform the requesting actor
        sender().tell(new JoinSuccess(user, self()), self());

        // Tell all subscribers about the change
        bus.tell(new UserJoined(user), self());

        log.debug("The user {} has joined the lobby", user);
    }

    /**
     * Leave the lobby.
     *
     * @param user The user that wants to leave the lobby.
     */
    private void leave(User user) {
        ActorRef ref = users.remove(user);

        // Determine whether the user was in the lobby
        if (ref == null) {
            log.warning("The user {} tried to leave the lobby, but is not in the lobby",
                user);
            sender().tell(new NotInLobbyException(self()), self());
            return;
        }

        // Inform the requesting actor
        sender().tell(new LeaveSuccess(user), self());

        // Tell all subscribers about the change
        bus.tell(new UserLeft(user), self());

        log.debug("The user {} has left the lobby", user);
    }

    /**
     * Return the {@link LobbyInformation} of this lobby.
     *
     * @param status The status of the lobby.
     * @return The information of this lobby.
     */
    private LobbyInformation getInformation(LobbyStatus status) {
        return new LobbyInformation(status, configuration, users.keySet());
    }

    /**
     * Create {@link Props} instance for an actor of this type.
     *
     * @param configuration The configuration of the lobby.
     * @return A Props for creating this actor, which can then be further configured
     *         (e.g. calling `.withDispatcher()` on it)
     */
    public static Props props(LobbyConfiguration configuration) {
        return Props.create(Lobby.class, () -> new Lobby(configuration));
    }
}

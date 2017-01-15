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
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import nl.tudelft.fa.core.lobby.Lobby;
import nl.tudelft.fa.core.lobby.LobbyConfiguration;
import nl.tudelft.fa.core.lobby.LobbyStatus;
import nl.tudelft.fa.core.lobby.message.*;
import nl.tudelft.fa.core.user.User;
import scala.PartialFunction;
import scala.concurrent.duration.FiniteDuration;
import scala.runtime.BoxedUnit;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This actor represents a game lobby with multiple users.
 *
 * @author Fabian Mastenbroek
 */
public class LobbyActor extends AbstractActor {
    /**
     * The configuration of the lobby.
     */
    private final LobbyConfiguration configuration;

    /**
     * The users that have joined this lobby.
     */
    private final Map<User, ActorRef> users;

    /**
     * The event bus this {@link LobbyActor} uses to publish updates.
     */
    private final ActorRef bus;

    /**
     * The unique identifier of this lobby.
     */
    private final String id;

    /**
     * The {@link LoggingAdapter} of this class.
     */
    private final LoggingAdapter log = Logging.getLogger(context().system(), this);

    /**
     * Construct a {@link LobbyActor} instance.
     *
     * @param configuration The configuration of the lobby.
     */
    private LobbyActor(LobbyConfiguration configuration) {
        this.configuration = configuration;
        this.users = new HashMap<>(configuration.getUserMaximum());
        this.bus = context().actorOf(LobbyEventBus.props(), "event-bus");
        this.id = self().path().name();
    }

    /**
     * This method defines the initial actor behavior, it must return a partial function with the
     * actor logic.
     *
     * @return The initial actor behavior as a partial function.
     */
    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return intermission();
    }

    /**
     * This method defines the base actor behavior, it must return a partial function with the
     * actor logic.
     *
     * @return The base actor behavior as a partial function.
     */
    private PartialFunction<Object, BoxedUnit> base() {
        return ReceiveBuilder
            .match(Subscribe.class, req -> bus.tell(req, sender()))
            .match(Unsubscribe.class, req -> bus.tell(req, sender()))
            .match(Terminated.class, msg -> handleTermination(msg.actor()))
            .build();
    }

    /**
     * This method defines the behavior of the actor when the lobby is in intermission.
     *
     * @return The intermission actor behavior as a partial function.
     */
    private PartialFunction<Object, BoxedUnit> intermission() {
        // Schedule the game intermission
        context().system().scheduler().scheduleOnce(
            FiniteDuration.create(configuration.getIntermission().toNanos(),
                TimeUnit.NANOSECONDS), self(),
            new LobbyStatusChanged(LobbyStatus.INTERMISSION, LobbyStatus.PREPARATION),
            context().dispatcher(),
            self()
        );

        return ReceiveBuilder
            .match(RequestInformation.class, req -> inform(LobbyStatus.INTERMISSION))
            .match(Join.class, req -> join(req.getUser(), req.getHandler()))
            .match(Leave.class, req -> leave(req.getUser()))
            .match(LobbyStatusChanged.class, this::transitToPreparation)
            .build()
            .orElse(base());
    }

    /**
     * This method defines the behavior of the actor when the lobby is in preparation.
     *
     * @return The preparation actor behavior as a partial function.
     */
    private PartialFunction<Object, BoxedUnit> preparation() {
        // Schedule the game preparation
        context().system().scheduler().scheduleOnce(
            FiniteDuration.create(configuration.getPreparation().toNanos(),
                TimeUnit.NANOSECONDS), self(),
            new LobbyStatusChanged(LobbyStatus.INTERMISSION, LobbyStatus.PREPARATION),
            context().dispatcher(),
            self()
        );

        return ReceiveBuilder
            .match(RequestInformation.class, req -> inform(LobbyStatus.PREPARATION))
            .match(LobbyStatusChanged.class, this::transitToProgression)
            .build()
            .orElse(base());
    }

    /**
     * This method defines the behavior of the actor when the lobby is progressing.
     *
     * @return The progressing actor behavior as a partial function.
     */
    private PartialFunction<Object, BoxedUnit> progression() {
        return ReceiveBuilder
            .match(RequestInformation.class, req -> inform(LobbyStatus.PROGRESSION))
            .match(LobbyStatusChanged.class, this::transitToIntermission)
            .build()
            .orElse(base());
    }

    /**
     * Inform an actor about the current state of this {@link LobbyActor}.
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
        if (users.size() >= configuration.getUserMaximum()) {
            log.warning("The user {} failed to join because the lobby is full");

            // The lobby has reached its maximum capacity
            sender().tell(new LobbyFullException(self(), users.size()), self());
            return;
        }

        // watch the handler
        context().watch(handler);

        // Put the user in the lobby
        users.put(user, handler);

        // Inform the requesting actor
        sender().tell(JoinSuccess.INSTANCE, self());

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

        // unwatch the handler
        context().unwatch(ref);

        // Inform the requesting actor
        sender().tell(LeaveSuccess.INSTANCE, self());

        // Tell all subscribers about the change
        bus.tell(new UserLeft(user), self());

        log.debug("The user {} has left the lobby", user);
    }

    /**
     * Handle the termination of the given user handler.
     *
     * @param handler The handler that has been terminated.
     */
    private void handleTermination(ActorRef handler) {
        users.entrySet()
            .stream()
            .filter(entry -> handler.equals(entry.getValue()))
            .iterator()
            .forEachRemaining(entry -> leave(entry.getKey()));
    }

    /**
     * Transit to the preparation state.
     *
     * @param event The event that occurred.
     */
    private void transitToPreparation(LobbyStatusChanged event) {
        if (users.size() > 0) {
            log.info("Changing lobby status from {} to {}", event.getPrevious(),
                event.getStatus());
            bus.tell(event, self());
            context().become(preparation());
            return;
        }

        log.info("Lobby kept in intermission because not enough users");
        context().become(intermission());
    }

    /**
     * Transit to the progression state.
     *
     * @param event The event that occurred.
     */
    private void transitToProgression(LobbyStatusChanged event) {
        log.info("Changing lobby status from {} to {}", event.getPrevious(),
                event.getStatus());
        bus.tell(event, self());
        context().become(progression());
    }

    /**
     * Transit to the intermission state.
     *
     * @param event The event that occurred.
     */
    private void transitToIntermission(LobbyStatusChanged event) {
        log.info("Changing lobby status from {} to {}", event.getPrevious(),
            event.getStatus());
        bus.tell(event, self());
        context().become(intermission());
    }

    /**
     * Return the {@link Lobby} of this lobby.
     *
     * @param status The status of the lobby.
     * @return The information of this lobby.
     */
    private Lobby getInformation(LobbyStatus status) {
        return new Lobby(id, status, configuration, users.keySet());
    }

    /**
     * Create {@link Props} instance for an actor of this type.
     *
     * @param configuration The configuration of the lobby.
     * @return A Props for creating this actor, which can then be further configured
     *         (e.g. calling `.withDispatcher()` on it)
     */
    public static Props props(LobbyConfiguration configuration) {
        return Props.create(LobbyActor.class, () -> new LobbyActor(configuration));
    }
}

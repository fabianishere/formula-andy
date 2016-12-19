/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Fabian Mastenbroek, Christian Slothouber,
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

package nl.tudelft.fa.core.lobby;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

import nl.tudelft.fa.core.user.User;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;


/**
 * This actor represents a game lobby with multiple players.
 *
 * @author Fabian Mastenbroek
 */
public class Lobby extends AbstractActor {
    /**
     * The unique identifier of this lobby.
     */
    private UUID id;

    /**
     * The configuration of the lobby.
     */
    private LobbyConfiguration configuration;

    /**
     * The players that have joined this lobby.
     */
    private Map<ActorRef, User> players;

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
        this.players = new HashMap<>(configuration.getPlayerMaximum());
    }

    /**
     * This method is invoked before the start of this actor.
     */
    @Override
    public void preStart() {
        this.id = UUID.fromString(self().path().name());
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
            .match(Inform.class, req -> inform(LobbyStatus.PREPARATION))
            .match(Join.class, this::join)
            .match(Leave.class, this::leave)
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
     * Handle a {@link Join} request.
     *
     * @param req The join request to handle.
     */
    private void join(Join req) {
        if (players.size() >= configuration.getPlayerMaximum()) {
            sender().tell(new LobbyFull(players.size()), self());
            return;
        }

        players.put(sender(), req.getUser());
        LobbyInformation information = getInformation(LobbyStatus.PREPARATION);
        Joined event = new Joined(information);

        for (ActorRef user : players.keySet()) {
            user.tell(event, self());
        }
        context().parent().tell(information, self());
    }

    /**
     * Handle a {@link Leave} request.
     *
     * @param req The leave request to handle.
     */
    private void leave(Leave req) {
        User user = players.remove(sender());

        if (user == null) {
            // The user is not in the lobby
            log.warning("The actor {} tried to leave the lobby, but is not in the lobby", sender());
            return;
        }

        LobbyInformation information = getInformation(LobbyStatus.PREPARATION);
        Left event = new Left(user, self());

        for (ActorRef ref : players.keySet()) {
            ref.tell(event, self());
        }
        sender().tell(event, self());
        context().parent().tell(information, self());
    }

    /**
     * Return the {@link LobbyInformation} of this lobby.
     *
     * @param status The status of the lobby.
     * @return The information of this lobby.
     */
    private LobbyInformation getInformation(LobbyStatus status) {
        return new LobbyInformation(id, status, configuration,
            new HashSet<>(players.values()));
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
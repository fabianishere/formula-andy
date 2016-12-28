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

package nl.tudelft.fa.core.lobby.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import akka.pattern.PatternsCS;
import nl.tudelft.fa.core.lobby.LobbyConfiguration;
import nl.tudelft.fa.core.lobby.LobbyInformation;
import nl.tudelft.fa.core.lobby.LobbyStatus;
import nl.tudelft.fa.core.lobby.message.*;
import nl.tudelft.fa.core.user.User;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.util.*;
import java.util.concurrent.CompletionStage;


/**
 * This actor represents a game lobby with multiple users.
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
     * The users that have joined this lobby.
     */
    private Map<User, ActorRef> users;

    /**
     * The users that have been offered spots in this lobby.
     */
    private Set<User> reservations;

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
        this.reservations = new HashSet<>();
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
            .match(InformationRequest.class, req -> inform(LobbyStatus.PREPARATION))
            .match(JoinRequest.class, this::offer)
            .match(LeaveRequest.class, this::leave)
            .match(OfferResponseEnvelope.class, this::join)
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
     * Handle a {@link JoinRequest} message and respond to it with either a {@link JoinError}
     * or a {@link Offer}.
     *
     * @param req The join request to handle.
     */
    private void offer(JoinRequest req) {
        if (users.size() + reservations.size() >= configuration.getPlayerMaximum()) {
            sender().tell(new LobbyFullError(users.size()), self());
            return;
        }

        // Ask the user for a response to the Offer
        CompletionStage<OfferResponseEnvelope> cs = PatternsCS.ask(sender(), Offer.INSTANCE, 100000)
            .thenApplyAsync(OfferResponse.class::cast)
            .handleAsync((res, ex) -> res != null ? res : OfferResponse.DECLINE)
            .thenApplyAsync(res -> new OfferResponseEnvelope(res, req.getUser()));

        // Pipe result to self
        PatternsCS.pipe(cs, context().dispatcher()).to(self(), sender());

        // Reserve spot in the lobby for now
        reservations.add(req.getUser());
    }

    /**
     * Handle a {@link OfferResponseEnvelope} message and let the user join the lobby.
     *
     * @param res The response sent by the {@link User} wrapped in an envelope.
     */
    private void join(OfferResponseEnvelope res) {
        log.info("Received response to offer {}", res);

        // Clear the reserved spot
        reservations.remove(res.getUser());

        if (res.getMessage() == OfferResponse.DECLINE) {
            // If the user declined, we don't do anything further
            return;
        }

        // Put the user in the lobby
        users.put(res.getUser(), sender());

        LobbyInformation information = getInformation(LobbyStatus.PREPARATION);
        JoinSuccess event = new JoinSuccess(information);

        for (User user : users.keySet()) {
            users.get(user).tell(event, self());
        }

        context().system().eventStream().publish(information);
    }

    /**
     * Handle a {@link LeaveRequest} request.
     *
     * @param req The leave request to handle.
     */
    private void leave(LeaveRequest req) {
        ActorRef ref = users.remove(req.getUser());

        if (ref == null) {
            // The user is not in the lobby
            log.warning("The user {} tried to leave the lobby, but is not in the lobby",
                req.getUser().getCredentials().getUsername());
            sender().tell(new NotInLobbyError(), self());
            return;
        }

        LobbyInformation information = getInformation(LobbyStatus.PREPARATION);
        LeaveSuccess event = new LeaveSuccess(req.getUser(), self());

        for (User user : users.keySet()) {
            users.get(user).tell(event, self());
        }

        sender().tell(event, self());
        context().system().eventStream().publish(information);
    }

    /**
     * Return the {@link LobbyInformation} of this lobby.
     *
     * @param status The status of the lobby.
     * @return The information of this lobby.
     */
    private LobbyInformation getInformation(LobbyStatus status) {
        return new LobbyInformation(id, status, configuration, users.keySet());
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

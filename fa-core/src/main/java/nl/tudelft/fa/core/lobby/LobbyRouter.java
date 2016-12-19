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

import java.util.*;

/**
 * A {@link LobbyRouter} distributes {@link User}s across multiple {@link Lobby} actors.
 *
 * @author Fabian Mastenbroek
 */
public class LobbyRouter extends AbstractActor {
    /**
     * The {@link LoggingAdapter} of this class.
     */
    private final LoggingAdapter log = Logging.getLogger(context().system(), this);

    /**
     * The configuration of the lobbies.
     */
    private LobbyConfiguration configuration;

    /**
     * The {@link Lobby} actors currently available.
     */
    private Map<ActorRef, Integer> lobbies;

    /**
     * Construct a {@link LobbyRouter} instance.
     *
     * @param configuration The configuration of the lobbies.
     */
    private LobbyRouter(LobbyConfiguration configuration) {
        this.configuration = configuration;
        this.lobbies = new HashMap<>();
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
            .match(Join.class, this::join)
            .match(LobbyInformation.class, this::update)
            .build();
    }

    /**
     * Handle a {@link Join} request.
     *
     * @param req The join request to handle.
     */
    private void join(Join req) {
        log.info("Join request {} received", req);
        for (ActorRef lobby : lobbies.keySet()) {
            int spots = lobbies.get(lobby);
            if (spots > 0) {
                lobby.tell(req, sender());
                lobbies.put(lobby, spots);
                return;
            }
        }

        ActorRef lobby = context().actorOf(Lobby.props(configuration),
            UUID.randomUUID().toString());
        lobbies.put(lobby, configuration.getPlayerMaximum() - 1);
        lobby.tell(req, sender());
    }

    /**
     * Update the information of a lobby.
     *
     * @param information The new information of the lobby.
     */
    private void update(LobbyInformation information) {
        if (!information.getStatus().equals(LobbyStatus.PREPARATION)
            || information.getUsers().size() >= information.getConfiguration().getPlayerMaximum()) {

            log.info("Lobby {} is full or not in preparation", sender());
            lobbies.remove(sender());
            return;
        }

        lobbies.put(sender(), information.getConfiguration().getPlayerMaximum()
            - information.getUsers().size());
        context().parent().tell(information, sender());
    }


    /**
     * Create {@link Props} instance for an actor of this type.
     *
     * @param configuration The configuration of the lobbies.
     * @return A Props for creating this actor, which can then be further configured
     *         (e.g. calling `.withDispatcher()` on it)
     */
    public static Props props(LobbyConfiguration configuration) {
        return Props.create(LobbyRouter.class, () -> new LobbyRouter(configuration));
    }
}

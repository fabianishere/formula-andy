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
import nl.tudelft.fa.core.race.GrandPrix;
import nl.tudelft.fa.core.team.Team;
import scala.PartialFunction;
import scala.concurrent.duration.FiniteDuration;
import scala.runtime.BoxedUnit;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This actor represents a game lobby with multiple teams.
 *
 * @author Fabian Mastenbroek
 */
public class LobbyActor extends AbstractActor {
    /**
     * The configuration of the lobby.
     */
    private final LobbyConfiguration configuration;

    /**
     * The teams that have joined this lobby.
     */
    private final Map<Team, ActorRef> teams;

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
     * The remaining schedule of the lobby.
     */
    private LinkedList<GrandPrix> schedule;

    /**
     * The reference to the {@link LobbyRaceSimulationActor} instance.
     */
    private ActorRef simulator;

    /**
     * Construct a {@link LobbyActor} instance.
     *
     * @param configuration The configuration of the lobby.
     */
    private LobbyActor(LobbyConfiguration configuration) {
        this.configuration = configuration;
        this.teams = new HashMap<>(configuration.getTeamMaximum());
        this.bus = context().actorOf(LobbyEventBus.props(), "event-bus");
        this.id = self().path().name();

        this.schedule = new LinkedList<>(configuration.getScheduleFactory().generate());
        this.simulator = context().actorOf(LobbyRaceSimulationActor.props(bus, schedule.peek()));
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
            .match(TeamConfigurationSubmission.class,
                msg -> sender().equals(teams.get(msg.getTeam())),
                msg -> simulator.tell(msg, sender()))
            .match(CarParametersSubmission.class,
                msg -> sender().equals(teams.get(msg.getTeam())),
                msg -> simulator.tell(msg, sender()))
            .match(Chat.class, msg -> bus.tell(new ChatEvent(msg.getTeam(), msg.getMessage()),
                sender()))
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

        // Create a countdown for the users
        context().system().actorOf(LobbyCountdownActor.props(bus, configuration.getIntermission(),
            Duration.ofSeconds(10)));

        return ReceiveBuilder
            .match(RequestInformation.class, req -> inform(LobbyStatus.INTERMISSION))
            .match(Join.class, req -> join(req.getTeam(), req.getHandler()))
            .match(Leave.class, req -> leave(req.getTeam()))
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
        // Schedule the game progression
        context().system().scheduler().scheduleOnce(
            FiniteDuration.create(configuration.getPreparation().toNanos(),
                TimeUnit.NANOSECONDS), self(),
            new LobbyStatusChanged(LobbyStatus.PREPARATION, LobbyStatus.PROGRESSION),
            context().dispatcher(),
            self()
        );

        // Create a countdown for the users
        context().system().actorOf(LobbyCountdownActor.props(bus, configuration.getIntermission(),
            Duration.ofSeconds(10)));

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
        context().watch(simulator);
        simulator.tell("start", self()); // start the simulation
        return ReceiveBuilder
            .match(RequestInformation.class, req -> inform(LobbyStatus.PROGRESSION))
            .match(LobbyStatusChanged.class, this::transitToIntermission)
            .match(Terminated.class, msg -> self().tell(
                new LobbyStatusChanged(LobbyStatus.PROGRESSION, LobbyStatus.INTERMISSION), self()))
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
     * Let a {@link Team} join this lobby by responding with either a {@link JoinException} message
     * or a {@link JoinSuccess} message.
     *
     * @param team The team that wants to join.
     * @param handler The handler of the user.
     */
    private void join(Team team, ActorRef handler) {
        if (teams.size() >= configuration.getTeamMaximum()) {
            log.warning("The team {} failed to join because the lobby is full");

            // The lobby has reached its maximum capacity
            sender().tell(new LobbyFullException(self(), teams.size()), self());
            return;
        }

        // watch the handler
        context().watch(handler);

        // Put the user in the lobby
        teams.put(team, handler);

        // Inform the requesting actor
        sender().tell(JoinSuccess.INSTANCE, self());

        // Tell all subscribers about the change
        bus.tell(new TeamJoined(team), self());

        log.debug("The team {} has joined the lobby", team);
    }

    /**
     * Leave the lobby.
     *
     * @param team The team that wants to leave the lobby.
     */
    private void leave(Team team) {
        ActorRef ref = teams.remove(team);
        leave(team, ref);
    }

    /**
     * Leave the lobby.
     *
     * @param team The team that wants to leave the lobby.
     * @param ref The reference to the handler of the user.
     */
    private void leave(Team team, ActorRef ref) {
        // Determine whether the user was in the lobby
        if (ref == null) {
            log.warning("The team {} tried to leave the lobby, but is not in the lobby",
                team);
            sender().tell(new NotInLobbyException(self()), self());
            return;
        }

        // unwatch the handler
        context().unwatch(ref);

        // Inform the requesting actor
        sender().tell(LeaveSuccess.INSTANCE, self());

        // Tell all subscribers about the change
        bus.tell(new TeamLeft(team), self());

        log.debug("The team {} has left the lobby", team);
    }

    /**
     * Handle the termination of the given user handler.
     *
     * @param handler The handler that has been terminated.
     */
    private void handleTermination(ActorRef handler) {
        teams.entrySet().removeIf(entry -> {
            if (entry.getValue().equals(handler)) {
                leave(entry.getKey(), entry.getValue());
                return true;
            }

            return false;
        });
    }

    /**
     * Transit to the preparation state.
     *
     * @param event The event that occurred.
     */
    private void transitToPreparation(LobbyStatusChanged event) {
        if (teams.size() > 0) {
            log.info("Changing lobby status from {} to {}", event.getPrevious(),
                event.getStatus());
            bus.tell(event, self());
            context().become(preparation());
            return;
        }

        log.info("Lobby kept in intermission because not enough teams");
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
        schedule.poll(); // remove the GrandPrix from the schedule
        context().become(intermission());
    }

    /**
     * Return the {@link Lobby} of this lobby.
     *
     * @param status The status of the lobby.
     * @return The information of this lobby.
     */
    private Lobby getInformation(LobbyStatus status) {
        return new Lobby(id, status, configuration, teams.keySet(), schedule);
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

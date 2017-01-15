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
import akka.japi.pf.ReceiveBuilder;
import nl.tudelft.fa.core.game.CarSimulator;
import nl.tudelft.fa.core.game.RaceSimulator;
import nl.tudelft.fa.core.lobby.message.CarParametersSubmission;
import nl.tudelft.fa.core.lobby.message.TeamConfigurationSubmission;
import nl.tudelft.fa.core.race.CarConfiguration;
import nl.tudelft.fa.core.race.CarParameters;
import nl.tudelft.fa.core.race.GrandPrix;
import nl.tudelft.fa.core.race.TeamConfiguration;
import nl.tudelft.fa.core.team.inventory.Car;
import nl.tudelft.fa.core.user.User;
import scala.PartialFunction;
import scala.concurrent.duration.FiniteDuration;
import scala.runtime.BoxedUnit;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * The {@link LobbyRaceSimulationActor} class is in charge of running the {@link RaceSimulator}.
 *
 * @author Fabian Mastenbroek
 */
public class LobbyRaceSimulationActor extends AbstractActor {
    /**
     * The reference to the event bus where the results are published.
     */
    private final ActorRef bus;

    /**
     * The {@link GrandPrix} of this simulation.
     */
    private final GrandPrix grandPrix;

    /**
     * The cars participating in this lobby.
     */
    private final Map<Car, User> cars;

    /**
     * The configurations of the cars.
     */
    private final Map<Car, CarConfiguration> configurations;

    /**
     * The parameters of the cars.
     */
    private final Map<Car, CarParameters> parameters;

    /**
     * Construct a {@link LobbyRaceSimulationActor} instance.
     *
     * @param bus The reference to the event bus where the results are published.
     * @param grandPrix The {@link GrandPrix} of this simulation.
     */
    private LobbyRaceSimulationActor(ActorRef bus, GrandPrix grandPrix) {
        this.bus = bus;
        this.grandPrix = grandPrix;
        this.cars = new HashMap<>();
        this.configurations = new HashMap<>();
        this.parameters = new HashMap<>();
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
            .match(TeamConfigurationSubmission.class, msg -> {
                submitConfiguration(msg.getUser(), msg.getConfiguration());
                bus.tell(msg, sender());
            })
            .match(CarParametersSubmission.class, msg -> submitParameters(msg.getCar(),
                msg.getParameters()))
            .matchEquals("start", msg -> context().become(running()))
            .build();
    }

    /**
     * This method defines the running actor behavior, it must return a partial function with the
     * actor logic.
     *
     * @return The running actor behavior as a partial function.
     */
    private PartialFunction<Object, BoxedUnit> running() {
        // schedule the tick
        context().system().scheduler().schedule(FiniteDuration.Zero(),
            FiniteDuration.create(1, TimeUnit.SECONDS), self(), "tick",
            context().dispatcher(), self());

        // setup the car simulator
        final RaceSimulator simulator = simulator();

        return ReceiveBuilder
            .match(CarParametersSubmission.class, msg -> submitParameters(msg.getCar(),
                msg.getParameters()))
            .matchEquals("tick", msg -> {
                // TODO calculate next race cycle
                // TODO publish results to lobby
            })
            .build();
    }

    /**
     * Create a {@link RaceSimulator} from the given state.
     *
     * @return A {@link RaceSimulator} created from the given state.
     */
    private RaceSimulator simulator() {
        return new RaceSimulator(
            configurations.entrySet()
                .stream()
                .map(entry -> new CarSimulator(entry.getValue(), parameters.get(entry.getKey())))
                .collect(Collectors.toList()),
            grandPrix
        );
    }

    /**
     * Submit the {@link CarConfiguration} of a user.
     *
     * @param user The user that wants to submit the configuration.
     * @param configuration The configuration to use.
     */
    private void submitConfiguration(User user, TeamConfiguration configuration) {
        configuration.getConfigurations().forEach(conf -> {
            cars.put(conf.getCar(), user);
            configurations.put(conf.getCar(), conf);
        });
    }

    /**
     * Submit the {@link CarParameters} of a user..
     *
     * @param car The car to apply these parameters to.
     * @param parameters The parameters to use.
     */
    private void submitParameters(Car car, CarParameters parameters) {
        this.parameters.put(car, parameters);
    }

    /**
     * Create {@link Props} instance for an actor of this type.
     *
     * @param bus The reference to the event bus where the results are published.
     * @param grandPrix The grand prix to simulate.
     * @return A Props for creating this actor, which can then be further configured
     *         (e.g. calling `.withDispatcher()` on it)
     */
    public static Props props(ActorRef bus, GrandPrix grandPrix) {
        return Props.create(LobbyRaceSimulationActor.class, () ->
            new LobbyRaceSimulationActor(bus, grandPrix));
    }
}

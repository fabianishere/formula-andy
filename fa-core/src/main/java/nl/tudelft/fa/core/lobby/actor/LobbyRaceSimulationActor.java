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
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import nl.tudelft.fa.core.lobby.message.CarParametersSubmission;
import nl.tudelft.fa.core.lobby.message.RaceSimulationStarted;
import nl.tudelft.fa.core.lobby.message.TeamConfigurationSubmission;
import nl.tudelft.fa.core.lobby.message.TeamConfigurationSubmitted;
import nl.tudelft.fa.core.race.*;
import nl.tudelft.fa.core.team.Team;
import nl.tudelft.fa.core.team.inventory.Car;
import nl.tudelft.fa.core.team.manager.ComputerControllerManager;
import scala.PartialFunction;
import scala.concurrent.duration.FiniteDuration;
import scala.runtime.BoxedUnit;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * The cars participating in the simulation.
     */
    private final Map<Car, CarSimulator> cars;

    /**
     * The {@link LoggingAdapter} of this class.
     */
    private final LoggingAdapter log = Logging.getLogger(context().system(), this);

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
            .match(TeamConfigurationSubmission.class, msg -> submitConfiguration(msg.getTeam(),
                msg.getCars()))
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
        log.info("Starting race simulation");
        FiniteDuration interval =  FiniteDuration.create(1, TimeUnit.SECONDS);

        // schedule the tick
        Cancellable tick = context().system().scheduler().schedule(FiniteDuration.Zero(), interval,
            self(), "tick", context().dispatcher(), self());

        // setup the simulator
        int amount = Math.max(22 - cars.size(), 0);
        ComputerControllerManager manager = new ComputerControllerManager();
        List<CarSimulator> bots = Stream
            .generate(manager::createConfiguration)
            .map(conf -> new CarSimulator(conf, null))
            .limit(amount)
            .collect(Collectors.toList());

        List<CarSimulator> simulators = new ArrayList<>();
        simulators.addAll(cars.values());
        simulators.addAll(bots);

        RaceSimulator simulator = new RaceSimulator(simulators, grandPrix);

        // submit the parameters for the bots
        bots.forEach(bot -> bot.setParameters(manager.createParameters(simulator.isRaining())));

        // setup the car simulator
        final Iterator<RaceSimulationResult> results = simulator.iterator();

        // tell the bus the simulation is going to start
        bus.tell(new RaceSimulationStarted(simulators.stream()
            .map(CarSimulator::getConfiguration).collect(Collectors.toSet())), self());

        return ReceiveBuilder
            .match(CarParametersSubmission.class, msg -> submitParameters(msg.getCar(),
                msg.getParameters()))
            .matchEquals("tick", msg -> {
                if (results.hasNext()) {
                    bus.tell(results.next(), self());
                    return;
                }

                log.info("Racing simulation has been finished");
                tick.cancel();
                context().stop(self());
            })
            .build();
    }

    /**
     * Submit the {@link CarConfiguration} of a user.
     *
     * @param team The team that wants to submit the configuration.
     * @param cars The car configurations to use.
     */
    private void submitConfiguration(Team team, Set<CarConfiguration> cars) {
        cars.forEach(conf -> {
            if (conf.getCar() != null) {
                this.cars.put(conf.getCar(), new CarSimulator(conf, null));
            }
        });
        bus.tell(new TeamConfigurationSubmitted(team, cars), sender());
        log.info("Team {} has submitted its team configuration", team);
    }

    /**
     * Submit the {@link CarParameters} of a user..
     *
     * @param car The car to apply these parameters to.
     * @param parameters The parameters to use.
     */
    private void submitParameters(Car car, CarParameters parameters) {
        if (car != null) {
            CarSimulator simulator = cars.getOrDefault(car, new CarSimulator(null, null));
            simulator.setParameters(parameters);
            cars.put(car, simulator);
            log.info("Parameters submitted for car {}", car);
        }
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

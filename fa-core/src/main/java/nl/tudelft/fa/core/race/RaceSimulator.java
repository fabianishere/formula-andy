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

package nl.tudelft.fa.core.race;

import nl.tudelft.fa.core.team.inventory.Car;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The {@link RaceSimulator} simulates a {@link GrandPrix}.
 *
 * @author Christian Slothouber
 * @author Fabian Mastenbroek
 */
public class RaceSimulator implements Iterable<RaceSimulationResult> {
    /**
     * The grand prix that is simulated.
     */
    private final GrandPrix grandPrix;

    /**
     * The car simulators that simulate each car.
     */
    private final Map<Car, CarSimulator> simulators;

    /**
     * A flag that indicates whether it is raining.
     */
    private final boolean raining;

    /**
     * The {@link Random} instance to use.
     */
    private final Random random;

    /**
     * Construct a {@link RaceSimulator} instance.
     *
     * @param simulators The car simulators that simulate each car.
     * @param grandPrix The grand prix to simulate.
     * @param random The {@link Random} instance to use.
     */
    public RaceSimulator(Map<Car, CarSimulator> simulators, GrandPrix grandPrix, Random random) {
        this.simulators = simulators;
        this.random = random;
        this.grandPrix = grandPrix;
        this.raining = grandPrix != null && grandPrix.isItRaining(random);
    }

    /**
     * Construct a {@link RaceSimulator} instance.
     *
     * @param simulators The car simulators that simulate each car.
     * @param grandPrix The grand prix to simulate.
     */
    public RaceSimulator(Map<Car, CarSimulator> simulators, GrandPrix grandPrix) {
       this(simulators, grandPrix, new Random());
    }

    /**
     * Construct a {@link RaceSimulator} instance.
     *
     * @param simulators The car simulators that simulate each car.
     * @param grandPrix The grand prix to simulate.
     */
    public RaceSimulator(Collection<CarSimulator> simulators, GrandPrix grandPrix) {
        this(simulators.stream()
                .collect(Collectors.toMap(cs -> cs.getConfiguration().getCar(),
                    Function.identity())), grandPrix, new Random());
    }

    /**
     * Return the cars that participate in the simulation with their respective simulator.
     *
     * @return The cars that participate in the simulation with their respective simulator.
     */
    public Collection<CarSimulator> getSimulators() {
        return simulators.values();
    }

    /**
     * Return the {@link GrandPrix} that is simulated.
     *
     * @return The grand prix that is being simulated.
     */
    public GrandPrix getGrandPrix() {
        return grandPrix;
    }

    /**
     * Return the flag that indicates whether it is raining.
     *
     * @return <code>true</code> if it is raining currently, <code>false</code> otherwise.
     */
    public boolean isRaining() {
        return raining;
    }

    /**
     * Start the first race cycle and calculate the results for each car.
     *
     * @return A map containing the result of the first cycle.
     */
    public RaceSimulationResult start() {
        List<CarSimulationResult> results = simulators.values()
            .stream()
            .map(cs -> {
                boolean finished = 0 >= grandPrix.getCircuit().getLength();
                if (cs.hasCrashed(raining, true, random)) {
                    return new CarSimulationResult(cs.getConfiguration(), 0,
                        true, finished);
                }
                return new CarSimulationResult(cs.getConfiguration(),
                    cs.calculateDelta(random), false, finished);
            })
            .collect(Collectors.toList());
        return createResult(results);
    }

    /**
     * Run the next race cycle and calculate the results for each car.
     *
     * @param previous The previous results of the simulation.
     * @return A map containing the result of the next cycle.
     */
    public RaceSimulationResult next(RaceSimulationResult previous) {
        final List<CarSimulationResult> results = new ArrayList<>();

        // split the results in finished cars and racing cars
        final Map<Boolean, List<CarSimulationResult>> split = previous.getResults()
            .stream()
            .collect(Collectors.partitioningBy(CarSimulationResult::hasFinished));

        // add the finished users to the results
        results.addAll(split.get(true));

        // calculate the results of the racing cars
        split.get(false)
            .stream()
            .map(result -> {
                final CarSimulator cs = simulators.get(result.getConfiguration().getCar());
                if (cs.hasCrashed(raining, cs.isNearby(result, previous, 100), random)) {
                    return result.crash();
                }
                return result
                    .increaseDistance(cs.calculateDelta(random))
                    .finishOn(grandPrix.getCircuit().getLength());
            })
            .sorted(Comparator.comparingDouble(CarSimulationResult::getDistanceTraveled).reversed())
            .forEachOrdered(results::add);

        return createResult(results);
    }

    /**
     * Determine whether the race is finished by the given results and return
     * a {@link RaceSimulationResult} instance.
     *
     * @param results The results to wrap.
     * @return The results of the simulation.
     */
    private RaceSimulationResult createResult(List<CarSimulationResult> results) {
        boolean finished = results.stream()
            .map(result -> result.hasCrashed() || result.hasFinished())
            .reduce(Boolean::logicalAnd)
            .orElse(true);
        return new RaceSimulationResult(results, finished);
    }

    /**
     * Return an {@link Iterator} that returns the results of each car per race cycle.
     *
     * @return An iterator returning the results of each car per race cycle.
     */
    @Override
    public Iterator<RaceSimulationResult> iterator() {
        return new Iterator<RaceSimulationResult>() {
            /**
             * A map containing the previous simulation results.
             */
            private RaceSimulationResult previous;

            /**
             * Determine whether the iterator has more elements.
             * {@inheritDoc}
             *
             * @return <code>true</code> if the iterator has more elements, <code>false</code>
             *     otherwise.
             */
            @Override
            public boolean hasNext() {
                return previous == null || !previous.isFinished();
            }

            /**
             * Return the results of the next race cycle.
             *
             * @return The results of the next race cycle.
             */
            @Override
            public RaceSimulationResult next() {
                previous = previous == null ? start() : RaceSimulator.this.next(previous);
                return previous;
            }
        };
    }
}

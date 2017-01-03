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

package nl.tudelft.fa.core.game;


import nl.tudelft.fa.core.race.GrandPrix;

import java.util.List;
import java.util.Random;

/**
 * A class representing a race,
 * @author F.C. Slothouber
 */
public class RaceSimulator {

    /**
     * The list of cars racing.
     */
    private List<CarSimulator> carSimulators;

    /**
     * The grand prix.
     */
    private GrandPrix grandPrix;

    /**
     * Boolean representing if it is raining or not.
     */
    private boolean raining;

    /**
     * Constructor.
     * @param carSimulators List of the racing cars.
     * @param grandPrix The grand prix.
     */
    public RaceSimulator(List<CarSimulator> carSimulators, GrandPrix grandPrix) {
        this.carSimulators = carSimulators;
        this.grandPrix = grandPrix;
        this.raining = this.grandPrix.isItRaining(new Random());
    }

    /**
     * Returns the list of racing cars.
     * @return The List of carSimulators.
     */
    public List<CarSimulator> getCarSimulators() {
        return carSimulators;
    }

    /**
     * Returns the grand prix.
     * @return The grand prix.
     */
    public GrandPrix getGrandPrix() {
        return grandPrix;
    }

    /**
     * Generates the next race cycle. The moved distance and the crashes.
     */
    public void getNextRaceCycle() {
        for (CarSimulator cs: carSimulators) {
            if (cs.getCarParameters().getCrashed()) {
                continue;
            }

            if (!cs.crashedThisCycle(raining, cs.closeDriver(carSimulators, 100), new Random())) {
                cs.getCarParameters().increaseTraveledDistance(cs.getMovedDistance(new Random()));
            } else {
                cs.getCarParameters().setCrashed(true);
            }
        }
    }
}

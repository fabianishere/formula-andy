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

package nl.tudelft.fa.core.model.team;

/**
 * Class representing a driver
 * @version
 * @author F.C. Slothouber
 */
public class Driver extends Member {

    private double speed;
    private double raceCraft;
    private double strategy;

    /**
     * Constructor
     * @param name name of driver
     * @param salary salary of driver
     * @param speed numeric representation of speed
     * @param racecraft numeric representation of race craft
     * @param strategy numeric representation of strategy
     */
    public Driver(String name, int salary, String id, double speed, double racecraft, double strategy) {
        super (name, salary, id);
        this.speed = speed;
        this.raceCraft = racecraft;
        this.strategy = strategy;
    }

    /**
     * Getter of speed
     * @return returns double speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Setter speed
     * @param speed new speed value
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Getter race craft
     * @return return double racecraft
     */
    public double getRacecraft() {
        return raceCraft;
    }

    /**
     * Setter race craft
     * @param racecraft new race craft value
     */
    public void setRacecraft(double racecraft) {
        this.raceCraft = racecraft;
    }

    /**
     * Getter strategy
     * @return returns double strategy
     */
    public double getStrategy() {
        return strategy;
    }

    /**
     * Setter strategy
     * @param strategy new strategy value
     */
    public void setStrategy(double strategy) {
        this.strategy = strategy;
    }
}

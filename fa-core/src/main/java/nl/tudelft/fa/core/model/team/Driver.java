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
 * A {@link Member} of the {@link Team} that drives the Formula 1 {@link Car}s.
 *
 * @author Christian Slothouber
 */
public class Driver extends Member {
    /**
     * A numeric representation of the speed of the driver.
     */
    private double speed;

    /**
     * A numeric representation of the race craft of the driver.
     */
    private double racecraft;

    /**
     * A numeric representation of the strategy of the driver.
     */
    private double strategy;

    /**
     * Construct a {@link Driver} instance.
     *
     * @param name The name of the driver
     * @param salary The salary of the driver
     * @param id The id of the driver.
     * @param speed A numeric representation of the speed of the driver.
     * @param racecraft A numeric representation of the race craft of the driver.
     * @param strategy A numeric representation of the strategy of the driver.
     */
    public Driver(String name, int salary, String id, double speed, double racecraft,
                  double strategy) {
        super(name, salary, id);
        this.speed = speed;
        this.racecraft = racecraft;
        this.strategy = strategy;
    }

    /**
     * Return a numeric representation of the speed of the driver.
     *
     * @return A numeric representation of the speed of the driver.
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Setter speed.
     * @param speed new speed value
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Return a numeric representation of the race craft of the driver.
     *
     * @return A numeric representation of the race craft of the driver.
     */
    public double getRacecraft() {
        return racecraft;
    }

    /**
     * Setter race craft.
     * @param racecraft new race craft value
     */
    public void setRacecraft(double racecraft) {
        this.racecraft = racecraft;
    }

    /**
     * Return a numeric representation of the strategy of the driver.
     * @return A numeric representation of the strategy of the driver.
     */
    public double getStrategy() {
        return strategy;
    }

    /**
     * Setter strategy.
     * @param strategy new strategy value
     */
    public void setStrategy(double strategy) {
        this.strategy = strategy;
    }
}
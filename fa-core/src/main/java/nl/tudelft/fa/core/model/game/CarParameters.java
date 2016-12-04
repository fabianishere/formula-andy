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

package nl.tudelft.fa.core.model.game;

import nl.tudelft.fa.core.model.team.Tire;

/**
 * This class represents the tunable parameters of a {@link nl.tudelft.fa.core.model.team.Car}
 * during a race.
 *
 * @author Christian Slothouber
 */
public class CarParameters {
    /**
     * The risk of the car setup.
     */
    private int mechanicRisk;

    /**
     * The risk of the car design.
     */
    private int aerodynamicistRisk;

    /**
     * The risk of the strategy.
     */
    private int strategistRisk;

    /**
     * The tire that is being used.
     */
    private Tire tire;

    /**
     * Construct a {@link CarParameters} instance.
     *
     * @param mechanicRisk The risk of the car setup.
     * @param aerodynamicistRisk The risk of the car design.
     * @param strategistRisk The risk of the strategy.
     * @param tire The tire that is being used.
     */
    public CarParameters(int mechanicRisk, int aerodynamicistRisk, int strategistRisk, Tire tire) {
        this.mechanicRisk = mechanicRisk;
        this.aerodynamicistRisk = aerodynamicistRisk;
        this.strategistRisk = strategistRisk;
        this.tire = tire;
    }

    /**
     * Getter of setup risk.
     * @return return mechanic risk
     */
    public int getMechanicRisk() {
        return mechanicRisk;
    }

    /**
     * Setter mechanicrisk.
     * @param mechanicRisk new risk value
     */
    public void setMechanicRisk(int mechanicRisk) {
        this.mechanicRisk = mechanicRisk;
    }

    /**
     * Getter car design risk.
     * @return retuns risk
     */
    public int getAerodynamicistRisk() {
        return aerodynamicistRisk;
    }

    /**
     * Setter car design risk.
     * @param aerodynamicistRisk new risk value
     */
    public void setAerodynamicistRisk(int aerodynamicistRisk) {
        this.aerodynamicistRisk = aerodynamicistRisk;
    }

    /**
     * Getter strategy risk.
     * @return returns risk value
     */
    public int getStrategistRisk() {
        return strategistRisk;
    }

    /**
     * Setter strategy risk.
     * @param strategistRisk new risk value
     */
    public void setStrategistRisk(int strategistRisk) {
        this.strategistRisk = strategistRisk;
    }

    /**
     * Getter current tire setup.
     * @return returns tire
     */
    public Tire getTire() {
        return tire;
    }

    /**
     * Setter tire setup.
     * @param tire new tire
     */
    public void setTire(Tire tire) {
        this.tire = tire;
    }
}

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

/**
 * Class representing a Grand Prix.
 * @version 01 12 2016
 * @author F.C. Slothouber
 */
public class GrandPrix {

    private Circuit circuit;
    private String date;

    /**
     * Construct a {@link GrandPrix} instance.
     * @param circuit the race circuit
     * @param date date of the race
     */
    public GrandPrix(Circuit circuit, String date) {
        this.circuit = circuit;
        this.date = date;
    }

    /**
     * Getter circuit.
     * @return returns circuit
     */
    public Circuit getCircuit() {
        return circuit;
    }

    /**
     * Setter circuit.
     * @param circuit new circuit
     */
    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;
    }

    /**
     * Getter date.
     * @return returns date
     */
    public String getDate() {
        return date;
    }

    /**
     * Setter date.
     * @param date new date
     */
    public void setDate(String date) {
        this.date = date;
    }
}

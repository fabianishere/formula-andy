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
 * Class representing a race circuit
 * @version 01 12 2016
 * @author F.C. Slothouber
 */
public class Circuit {

    private String name;
    private String country;
    private int laps;

    /**
     * Constructor
     * @param name name of circuit
     * @param country location of circuit
     * @param laps amount of laps each race
     */
    public Circuit(String name, String country, int laps) {
        this.name = name;
        this.country = country;
        this.laps = laps;
    }

    /**
     * Getter name
     * @return return name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter name
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter country
     * @return retuns country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Setter country
     * @param country new country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Getter laps
     * @return returns amount of laps
     */
    public int getLaps() {
        return laps;
    }

    /**
     * Setter laps
     * @param laps new amount of laps
     */
    public void setLaps(int laps) {
        this.laps = laps;
    }
}

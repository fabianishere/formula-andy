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
 * a Class representing a Forumla 1 tire
 * @version 30 11 2016
 * @author F.C. Slothouber
 */
public class Tire {

    private String brand;
    private String type;
    private double durability;
    private double grip;

    /**
     * Constructor
     * @param type Type of tire e.g. "Ultra Soft" or "Intermidiate"
     * @param durability numeric representation of durability
     * @param grip numeric representation of grip
     */
    public Tire(String type, double durability, double grip) {
        this.type = type;
        this.durability = durability;
        this.grip = grip;
    }

    /**
     * Getter of brand
     * @return returns String brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Setter of brand
     * @param brand new brandname
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Getter of tire type
     * @return return textual representation of type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter of tire type
     * @param type new tire type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter of durability
     * @return returns double durability
     */
    public double getDurability() {
        return durability;
    }

    /**
     * Setter of durability
     * @param durability new durability value
     */
    public void setDurability(double durability) {
        this.durability = durability;
    }

    /**
     * Getter of grip
     * @return returns double grip
     */
    public double getGrip() {
        return grip;
    }

    /**
     * Setter of grip
     * @param grip new grip value
     */
    public void setGrip(double grip) {
        this.grip = grip;
    }

    /**
     * equals method
     * @param other Object to tested for equality
     * @return return true if equal else false
     */
    public boolean equals(Object other){
        if (other instanceof Tire){
            Tire that = (Tire) other;
            return this.type.equals(that.type);
        } return false;
    }
}

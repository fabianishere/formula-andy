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

package nl.tudelft.fa.core.store.message;

import nl.tudelft.fa.core.store.StockItem;
import nl.tudelft.fa.core.store.actor.StoreActor;
import nl.tudelft.fa.core.team.Member;
import nl.tudelft.fa.core.team.Team;

import java.util.Objects;

/**
 * A message sent to the {@link StoreActor} to purchase staff.
 *
 * @author Fabian Mastenbroek
 */
public final class PurchaseStaff {
    /**
     * The team that wants to do this purchase.
     */
    private final Team team;

    /**
     * The item to buy.
     */
    private final StockItem<? extends Member> item;

    /**
     * The quantity to buy.
     */
    private final int quantity;

    /**
     * Construct a {@link PurchaseStaff} instance.
     *
     * @param team The team that wants to do this purchase.
     * @param item The item to buy.
     * @param quantity The quantity to buy.
     */
    public PurchaseStaff(Team team, StockItem<? extends Member> item, int quantity) {
        this.team = team;
        this.item = item;
        this.quantity = quantity;
    }

    /**
     * Return the team that wants to do this purchase.
     *
     * @return The team that wants to do this purchase.
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Return the item that is being bought.
     *
     * @return The item that is being bought.
     */
    public StockItem<? extends Member> getItem() {
        return item;
    }

    /**
     * Return the quantity to buy.
     *
     * @return The quantity to buy.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Test whether this message is equal to the given object, which means that all properties of
     * this message are equal to the properties of the other class.
     *
     * @param other The object to be tested for equality
     * @return <code>true</code> if both objects are equal, <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        PurchaseStaff that = (PurchaseStaff) other;
        return Objects.equals(team, that.team)
            && Objects.equals(item, that.item)
            && Objects.equals(quantity, that.quantity);
    }

    /**
     * Return the hash code of this object.
     *
     * @return The hash code of this object as integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(team, item, quantity);
    }

    /**
     * Return a string representation of this message.
     *
     * @return A string representation of this message.
     */
    @Override
    public String toString() {
        return String.format("PurchaseStaff(team=%s, item=%s, quantity=%d)", team, item, quantity);
    }
}

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

package nl.tudelft.fa.core.store;

import nl.tudelft.fa.core.team.Member;
import nl.tudelft.fa.core.team.inventory.InventoryItem;

import java.util.Objects;
import java.util.Set;

/**
 * This class represents the stock of a store.
 *
 * @author Fabian Mastenbroek
 */
public final class Stock {
    /**
     * The staff the store has in stock.
     */
    private final Set<StockItem<? extends Member>> staff;

    /**
     * The inventory the store has in stock.
     */
    private final Set<StockItem<? extends InventoryItem>> inventory;

    /**
     * Construct a {@link Stock} instance.
     *
     * @param staff The staff the store has in stock.
     * @param inventory The inventory the store has in stock.
     */
    public Stock(Set<StockItem<? extends Member>> staff,
                 Set<StockItem<? extends InventoryItem>> inventory) {
        this.staff = staff;
        this.inventory = inventory;
    }

    /**
     * Return the staff the store has in stock.
     *
     * @return The staff the store has in stock.
     */
    public Set<StockItem<? extends Member>> getStaff() {
        return staff;
    }

    /**
     * Return the inventory the store has in stock.
     *
     * @return The inventory the store has in stock.
     */
    public Set<StockItem<? extends InventoryItem>> getInventory() {
        return inventory;
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
        Stock that = (Stock) other;
        return Objects.equals(staff, that.staff)
            && Objects.equals(inventory, that.inventory);
    }

    /**
     * Return the hash code of this object.
     *
     * @return The hash code of this object as integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(staff, inventory);
    }

    /**
     * Return a string representation of this message.
     *
     * @return A string representation of this message.
     */
    @Override
    public String toString() {
        return String.format("Stock(staff=%s, inventory=%d)", staff, inventory);
    }
}

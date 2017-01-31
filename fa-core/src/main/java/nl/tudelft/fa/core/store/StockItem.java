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

import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

/**
 * This class represents an item stock.
 *
 * @author Fabian Mastenbroek
 */
public abstract class StockItem<T> implements Iterable<T> {
    /**
     * The unique identifier of this item.
     */
    private final UUID id;

    /**
     * The quantity of this item in stock.
     */
    private final int quantity;

    /**
     * Construct a {@link StockItem} instance.
     *
     * @param id The unique identifier of this item.
     * @param quantity The quantity of this item.
     */
    public StockItem(UUID id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    /**
     * Return the unique identifier of this item.
     *
     * @return The unique identifier of this item.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Return the quantity of this item.
     *
     * @return The quantity of this item.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Get a single item from stock.
     *
     * @return A single item from stock.
     */
    public abstract T get();


    /**
     * Return an {@link Iterator} that returns an item from the stock until the stock is exhausted.
     *
     * @return An iterator returning an item from the stock until the stock is exhausted.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            /**
             * The remaining items in the quantity.
             */
            private int remaining = quantity;

            /**
             * Determine whether the iterator has more elements.
             * {@inheritDoc}
             *
             * @return <code>true</code> if the iterator has more elements, <code>false</code>
             *     otherwise.
             */
            @Override
            public boolean hasNext() {
                return remaining > 0;
            }

            /**
             * Return the results of the next race cycle.
             *
             * @return The results of the next race cycle.
             */
            @Override
            public T next() {
                remaining--;
                return get();
            }
        };
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
        StockItem that = (StockItem) other;
        return Objects.equals(id, that.id);
    }

    /**
     * Return the hash code of this object.
     *
     * @return The hash code of this object as integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Return a string representation of this message.
     *
     * @return A string representation of this message.
     */
    @Override
    public String toString() {
        return String.format("StockItem(id=%s, quantity=%d)", id, quantity);
    }
}

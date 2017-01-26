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

import nl.tudelft.fa.core.store.actor.StoreActor;

/**
 * This message is sent by the {@link StoreActor} if the budget of the team is not enough to
 * purchase the item.
 *
 * @author Fabian Mastenbroek
 */
public class NotEnoughBudgetException extends StoreException {
    /**
     * The budget of the team.
     */
    private final int budget;

    /**
     * The budget required to purchase.
     */
    private final int required;

    /**
     * Construct a {@link NotEnoughBudgetException}.
     *
     * @param budget The budget of the team.
     * @param required The budget required to purchase.
     */
    public NotEnoughBudgetException(int budget, int required) {
        super("The budget of your team is not enough to purchase this item");
        this.budget = budget;
        this.required = required;
    }

    /**
     * Return the budget of the team.
     *
     * @return The budget of the team.
     */
    public int getBudget() {
        return budget;
    }

    /**
     * Return the budget required to purchase.
     *
     * @return The budget required to purchase.
     */
    public int getRequired() {
        return required;
    }
}

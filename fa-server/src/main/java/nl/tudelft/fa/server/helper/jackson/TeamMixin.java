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

package nl.tudelft.fa.server.helper.jackson;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import nl.tudelft.fa.core.team.Member;
import nl.tudelft.fa.core.team.Team;
import nl.tudelft.fa.core.team.inventory.InventoryItem;

import java.util.List;

/**
 * Mix-in for the {@link Team} class.
 *
 * @author Fabian Mastenbroek
 */
public abstract class TeamMixin {

    /**
     * Return the staff members of the team.
     *
     * @return A list of the members of this team.
     */
    @JsonManagedReference
    public abstract List<Member> getStaff();

    /**
     * Return the inventory of the team.
     *
     * @return The inventory of this team.
     */
    @JsonManagedReference
    public abstract List<InventoryItem> getInventory();
}

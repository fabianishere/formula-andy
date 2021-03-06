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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import nl.tudelft.fa.core.team.Mechanic;

import java.io.IOException;
import java.util.UUID;
import javax.persistence.EntityManager;

/**
 * A deserializer for the {@link Mechanic} class.
 *
 * @author Fabian Mastenbroek
 */
public class MechanicDeserializer extends StdDeserializer<Mechanic> {
    /**
     * The entity manager to use.
     */
    private final EntityManager entityManager;

    /**
     * Construct a {@link MechanicDeserializer} instance.
     *
     * @param entityManager The {@link EntityManager} to deserialize the car with.
     */
    protected MechanicDeserializer(EntityManager entityManager) {
        super(Mechanic.class);
        this.entityManager = entityManager;
    }

    /**
     * Deserialize the given JSON tree into a {@link Mechanic} object.
     * {@inheritDoc}
     *
     * @param parser The {@link JsonParser} to use.
     * @param ctx The {@link DeserializationContext} to use.
     * @throws IOException on failure.
     */
    @Override
    public Mechanic deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        return entityManager.find(Mechanic.class, UUID.fromString(node.get("id").asText()));
    }
}

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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import nl.tudelft.fa.core.race.CarSimulationResult;
import nl.tudelft.fa.core.team.inventory.Car;

import java.io.IOException;
import java.util.Map;


/**
 * This class serializes the results of a simulation.
 *
 * @author Fabian Mastenbroek
 */
public class RaceSimulationResultsSerializer extends StdSerializer<Map<Car, CarSimulationResult>> {

    /**
     * Construct a {@link RaceSimulationResultsSerializer} instance.
     */
    public RaceSimulationResultsSerializer() {
        this(null);
    }

    /**
     * Construct a {@link RaceSimulationResultsSerializer} instance.
     *
     * @param cls The class to serialize.
     */
    public RaceSimulationResultsSerializer(Class<Map<Car, CarSimulationResult>> cls) {
        super(cls);
    }

    @Override
    public void serialize(Map<Car, CarSimulationResult> value, JsonGenerator gen,
                          SerializerProvider provider) throws IOException {
        gen.writeObject(value.values());
    }
}

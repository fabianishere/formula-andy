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
import nl.tudelft.fa.core.lobby.Lobby;
import nl.tudelft.fa.core.lobby.LobbyBalancer;

import java.io.IOException;


/**
 * This class serializes the {@link LobbyBalancer} class for the REST API.
 *
 * @author Fabian Mastenbroek
 */
public class LobbyBalancerInformationSerializer extends StdSerializer<LobbyBalancer> {

    /**
     * Construct a {@link LobbyBalancerInformationSerializer} instance.
     */
    public LobbyBalancerInformationSerializer() {
        this(null);
    }

    /**
     * Construct a {@link LobbyBalancerInformationSerializer} instance.
     *
     * @param cls The class to serialize.
     */
    public LobbyBalancerInformationSerializer(Class<LobbyBalancer> cls) {
        super(cls);
    }

    @Override
    public void serialize(LobbyBalancer value, JsonGenerator gen,
                          SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeArrayFieldStart("lobbies");
        for (Lobby lobby : value.getLobbies().values()) {
            gen.writeObject(lobby);
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
}

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

package nl.tudelft.fa.client.net.codec;

import akka.NotUsed;
import akka.stream.javadsl.BidiFlow;
import akka.stream.javadsl.Flow;
import nl.tudelft.fa.client.lobby.message.LobbyInboundMessage;
import nl.tudelft.fa.client.lobby.message.LobbyOutboundMessage;

/**
 * Implementations of the {@link AbstractCodec} class provide a {@link BidiFlow}
 * that decodes/encodes messages.
 *
 * @author Fabian Mastenbroek
 * @param <T> The type the codec will encode/decode the message into/from.
 */
public abstract class AbstractCodec<T> {
    /**
     * Create the {@link BidiFlow} that encodes objects received from the lobby's event bus
     * and decodes messages received from the WebSocket.
     *
     * @return The {@link BidiFlow} that encodes/decodes the messages.
     */
    public BidiFlow<T, LobbyOutboundMessage, LobbyInboundMessage, T, NotUsed> bidiFlow() {
        return BidiFlow.fromFlows(decodeFlow(), encodeFlow());
    }

    /**
     * Create the {@link Flow} that encodes objects received from the lobby's event bus into the
     * appropriate message objects.
     *
     * @return The {@link Flow} that will encode outgoing messages.
     */
    public abstract Flow<LobbyInboundMessage, T, NotUsed> encodeFlow();

    /**
     * Create the {@link Flow} that decodes messages of type <code>T</code> into
     * {@link LobbyInboundMessage} instances.
     *
     * @return The {@link Flow} that will decode incoming messages.
     */
    public abstract Flow<T, LobbyOutboundMessage, NotUsed> decodeFlow();
}

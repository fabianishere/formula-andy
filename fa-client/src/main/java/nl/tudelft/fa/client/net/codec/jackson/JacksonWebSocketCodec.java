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

package nl.tudelft.fa.client.net.codec.jackson;

import akka.NotUsed;
import akka.http.javadsl.model.ws.BinaryMessage;
import akka.http.javadsl.model.ws.Message;
import akka.http.javadsl.model.ws.TextMessage;
import akka.stream.javadsl.BidiFlow;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Source;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.fa.client.lobby.message.LobbyInboundMessage;
import nl.tudelft.fa.client.lobby.message.LobbyOutboundMessage;
import nl.tudelft.fa.client.net.codec.AbstractCodec;

import java.io.IOException;

/**
 * The {@link JacksonWebSocketCodec} class defines the {@link BidiFlow} which encodes outgoing
 * WebSocket messages and decodes incoming messages with help from the Jackson library.
 *
 * @author Fabian Mastenbroek
 */
public class JacksonWebSocketCodec extends AbstractCodec<Message> {
    // TODO generialize this class and share logic between client and server

    /**
     * The {@link ObjectMapper} to use for encoding/decoding.
     */
    private final ObjectMapper mapper;

    /**
     * Construct a {@link JacksonWebSocketCodec} instance.
     *
     * @param mapper The Jackson {@link ObjectMapper} to use.
     */
    public JacksonWebSocketCodec(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Construct a {@link JacksonWebSocketCodec} instance.
     */
    public JacksonWebSocketCodec() {
        this(new ObjectMapper());
    }

    /**
     * Create the {@link Flow} that encodes objects received from the lobby's event bus into
     * {@link Message} objects.
     *
     * @return The {@link Flow} that will encode outgoing messages.
     */
    @Override
    public Flow<LobbyInboundMessage, Message, NotUsed> encodeFlow() {
        return Flow.<LobbyInboundMessage>create()
            .map(mapper::writeValueAsString)
            .map(TextMessage::create);
    }

    /**
     * Create the {@link Flow} that decodes messages received from a WebSocket and
     *
     * @return The {@link Flow} that will decode incoming messages.
     */
    @Override
    public Flow<Message, LobbyOutboundMessage, NotUsed> decodeFlow() {
        return Flow.<Message>create()
            .flatMapConcat(this::decode);
    }

    /**
     * Decode the given {@link Message} into a {@link Source} that provides a
     * {@link LobbyOutboundMessage} instance.
     *
     * @param message The message to decode.
     * @return The {@link Source} that provides the {@link LobbyOutboundMessage} instance.
     */
    private Source<LobbyOutboundMessage, NotUsed> decode(Message message) {
        return message.isText() ? decode(message.asTextMessage()) :
            decode(message.asBinaryMessage());
    }

    /**
     * Decode the given {@link TextMessage} into a {@link Source} that provides a
     * {@link LobbyOutboundMessage} instance.
     *
     * @param message The message to decode.
     * @return The {@link Source} that provides the {@link LobbyOutboundMessage} instance.
     */
    private Source<LobbyOutboundMessage, NotUsed> decode(TextMessage message) {
        if (message.isStrict()) {
            try {
                return Source.single(mapper.readValue(message.getStrictText(),
                    LobbyOutboundMessage.class));
            } catch (IOException cause) {
                cause.printStackTrace();
                // XXX Don't pass on error until Akka can handle inner stream exceptions.
                // XXX See https://github.com/akka/akka/issues/20173
                // return Source.failed(cause);
                return Source.empty();
            }
        }
        return Source.empty()
            .concat(message.getStreamedText())
            .map(val -> mapper.readValue(val, LobbyOutboundMessage.class));
    }

    /**
     * Decode the given {@link BinaryMessage} into a {@link Source} that provides a
     * {@link LobbyOutboundMessage} instance.
     *
     * @param message The message to decode.
     * @return The {@link Source} that provides the {@link LobbyOutboundMessage} instance.
     */
    private Source<LobbyOutboundMessage, NotUsed> decode(BinaryMessage message) {
        if (message.isStrict()) {
            try {
                return Source.single(mapper.readValue(message.getStrictData().toArray(),
                    LobbyOutboundMessage.class));
            } catch (IOException cause) {
                cause.printStackTrace();
                // XXX Don't pass on error until Akka can handle inner stream exceptions.
                // XXX See https://github.com/akka/akka/issues/20173
                // return Source.failed(cause);
                return Source.empty();
            }
        }
        return Source.empty()
            .concat(message.getStreamedData())
            .map(val -> mapper.readValue(val.toArray(), LobbyOutboundMessage.class));
    }
}

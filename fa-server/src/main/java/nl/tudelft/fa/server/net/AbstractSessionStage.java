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

package nl.tudelft.fa.server.net;

import akka.stream.BidiShape;
import akka.stream.Inlet;
import akka.stream.Outlet;
import akka.stream.stage.GraphStage;
import nl.tudelft.fa.core.lobby.message.LobbyInboundMessage;
import nl.tudelft.fa.core.lobby.message.LobbyOutboundMessage;

/**
 * A {@link GraphStage} that attaches the user's session to inbound messages.
 *
 * @author Fabian Mastenbroek
 */
public abstract class AbstractSessionStage extends GraphStage<BidiShape<LobbyInboundMessage,
        LobbyInboundMessage, LobbyOutboundMessage, LobbyOutboundMessage>> {

    /**
     * The first inlet of this graph stage.
     */
    public final Inlet<LobbyInboundMessage> inA = Inlet.create(String.format("%s.inA",
        getClass().getName()));

    /**
     * The second inlet of this graph stage.
     */
    public final Inlet<LobbyOutboundMessage> inB = Inlet.create(String.format("%s.inB",
        getClass().getName()));

    /**
     * The first outlet of this graph stage.
     */
    public final Outlet<LobbyInboundMessage> outA = Outlet.create(String.format("%s.outA",
        getClass().getName()));

    /**
     * The outlet of this graph stage.
     */
    public final Outlet<LobbyOutboundMessage> outB = Outlet.create(String.format("%s.outB",
        getClass().getName()));

    /**
     * The {@link BidiShape} of this graph stage.
     */
    private final BidiShape<LobbyInboundMessage, LobbyInboundMessage, LobbyOutboundMessage,
            LobbyOutboundMessage> shape = BidiShape.of(inA, outA, inB, outB);

    /**
     * Return the shape of this graph stage.
     * {@inheritDoc}
     *
     * @return The shape of this graph stage.
     */
    @Override
    public BidiShape<LobbyInboundMessage, LobbyInboundMessage, LobbyOutboundMessage,
            LobbyOutboundMessage> shape() {
        return shape;
    }
}

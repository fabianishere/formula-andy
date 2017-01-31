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

import akka.japi.pf.UnitPFBuilder;
import akka.stream.Attributes;
import akka.stream.Shape;
import akka.stream.stage.AbstractInHandler;
import akka.stream.stage.AbstractOutHandler;
import akka.stream.stage.GraphStageLogic;
import akka.stream.stage.GraphStageLogicWithLogging;
import nl.tudelft.fa.core.lobby.message.*;
import nl.tudelft.fa.core.user.User;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * An {@link AbstractSessionStage} where a user has been authorized and is allowed to receive/sent
 * all messages.
 *
 * @see UnauthorizedSessionStage if the user is just a spectator that should not be allowed to sent
 *     messages to the lobby.
 * @author Fabian Mastenbroek
 */
public class AuthorizedSessionStage extends AbstractSessionStage {
    /**
     * The user of this session.
     */
    private final User user;

    /**
     * Construct a {@link AuthorizedSessionStage} instance.
     *
     * @param user The user of this session.
     */
    public AuthorizedSessionStage(User user) {
        this.user = user;
    }

    /**
     * Create the logic of this graph stage.
     *
     * @param inheritedAttributes The inherited attributes.
     * @return The logic of this graph stage.
     */
    @Override
    public GraphStageLogic createLogic(Attributes inheritedAttributes) {
        return new AuthorizedSessionStageLogic(shape());
    }

    /**
     * Return the user of this session.
     *
     * @return The user of this session.
     */
    public User getUser() {
        return user;
    }

    /**
     * This class defines the logic for a stage where the user's session is attached to the inbound
     * messages.
     *
     * @author Fabian Mastenbroek
     */
    public class AuthorizedSessionStageLogic extends GraphStageLogicWithLogging {
        /**
         * This partial function that handles the messages.
         */
        private final PartialFunction<LobbyInboundMessage, BoxedUnit> handler =
            new UnitPFBuilder<LobbyInboundMessage>()
                .match(LobbyInboundMessage.class, msg -> push(outA, msg))
                .build();

        /**
         * Construct a {@link AuthorizedSessionStageLogic} instance.
         *
         * @param shape The shape of the stage.
         */
        public AuthorizedSessionStageLogic(Shape shape) {
            super(shape);
        }

        {
            setHandler(inA, new AbstractInHandler() {
                @Override
                public void onPush() throws Exception {
                    handler.apply(grab(inA));
                }
            });

            setHandler(outA, new AbstractOutHandler() {
                @Override
                public void onPull() throws Exception {
                    pull(inA);
                }
            });

            setHandler(inB, new AbstractInHandler() {
                @Override
                public void onPush() throws Exception {
                    push(outB, grab(inB));
                }
            });

            setHandler(outB, new AbstractOutHandler() {
                @Override
                public void onPull() throws Exception {
                    pull(inB);
                }
            });
        }
    }
}

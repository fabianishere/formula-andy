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
import akka.stream.stage.*;
import nl.tudelft.fa.core.lobby.message.LobbyInboundMessage;
import nl.tudelft.fa.core.lobby.message.RequestInformation;
import nl.tudelft.fa.server.net.message.NotAuthorizedException;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * An {@link AbstractSessionStage} where a user has not been authorized and should not be allowed
 * to participate in the lobby, only spectate.
 *
 * @see AuthorizedSessionStage if the user should be allowed to participate in the lobby.
 * @author Fabian Mastenbroek
 */
public class UnauthorizedSessionStage extends AbstractSessionStage {
    /**
     * Create the logic of this graph stage.
     *
     * @param inheritedAttributes The inherited attributes.
     * @return The logic of this graph stage.
     */
    @Override
    public GraphStageLogic createLogic(Attributes inheritedAttributes) {
        return new UnauthorizedSessionStageLogic(shape());
    }

    /**
     * This class defines the logic for a stage where inbound messages except some primitives
     * are dropped and not sent to the lobby.
     *
     * @author Fabian Mastenbroek
     */
    public class UnauthorizedSessionStageLogic extends GraphStageLogicWithLogging {
        /**
         * The partial function that handles the inbound messages.
         */
        private final PartialFunction<LobbyInboundMessage, BoxedUnit> receive =
            new UnitPFBuilder<LobbyInboundMessage>()
                .match(RequestInformation.class, this::pushMessage)
                .match(LobbyInboundMessage.class, this::rejectMessage)
                .build();

        /**
         * Construct a {@link UnauthorizedSessionStageLogic} instance.
         *
         * @param shape The shape of the stage.
         */
        public UnauthorizedSessionStageLogic(Shape shape) {
            super(shape);
        }

        /**
         * Push the given message to the next stage.
         *
         * @param message The message to push.
         */
        private void pushMessage(LobbyInboundMessage message) {
            push(outA, message);
            pull(inA);
        }

        /**
         * Reject the given message.
         *
         * @param message The message to reject.
         */
        private void rejectMessage(LobbyInboundMessage message) {
            log().debug("Message {} disregarded. Session is unauthorized.", message);
            push(outB, new NotAuthorizedException());
        }

        {
            setHandler(inA, new AbstractInHandler() {
                @Override
                public void onPush() throws Exception {
                    receive.apply(grab(inA));
                }
            });

            setHandler(outA, new AbstractOutHandler() {
                @Override
                public void onPull() throws Exception {
                    if (!hasBeenPulled(inA)) {
                        pull(inA);
                    }
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
                    if (!hasBeenPulled(inB)) {
                        pull(inB);
                    }
                }
            });
        }
    }
}

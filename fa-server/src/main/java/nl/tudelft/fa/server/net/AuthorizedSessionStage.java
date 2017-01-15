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

import akka.japi.pf.PFBuilder;
import akka.stream.Attributes;
import akka.stream.Shape;
import akka.stream.stage.*;
import nl.tudelft.fa.core.lobby.message.Join;
import nl.tudelft.fa.core.lobby.message.Leave;
import nl.tudelft.fa.core.lobby.message.LobbyInboundMessage;
import nl.tudelft.fa.core.user.User;
import scala.PartialFunction;

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
         * This partial function attaches the session to messages.
         */
        private final PartialFunction<LobbyInboundMessage, LobbyInboundMessage> attach =
            new PFBuilder<LobbyInboundMessage, LobbyInboundMessage>()
                .match(Join.class, msg -> new Join(user, msg.getHandler()))
                .match(Leave.class, msg -> new Leave(user))
                .matchAny(msg -> msg)
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
                    push(outA, attach.apply(grab(inA)));
                }

                @Override
                public void onUpstreamFinish() {
                    // prevent completing the stage if the upstream has finished
                    // because we will still receive messages from the lobby's event bus
                    // to send downstream.
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

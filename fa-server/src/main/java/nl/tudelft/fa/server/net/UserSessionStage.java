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

import akka.actor.ActorRef;
import akka.stream.Attributes;
import akka.stream.Shape;
import akka.stream.stage.AbstractInHandler;
import akka.stream.stage.GraphStageLogic;
import akka.stream.stage.InHandler;
import akka.stream.stage.OutHandler;
import nl.tudelft.fa.core.user.User;

/**
 * A {@link SessionStage} where the user has been authenticated. This stage will pass through
 * messages from the event bus to the output and messages from the input to the lobby.
 *
 * @see AnonymousSessionStage if you want to disregard inbound messages
 * @author Fabian Mastenbroek
 */
public class UserSessionStage extends SessionStage {
    /**
     * The user of this session.
     */
    private final User user;

    /**
     * Construct a {@link UserSessionStage} instance.
     *
     * @param lobby The reference to the lobby between which the communication takes place.
     * @param user The user of this session.
     */
    public UserSessionStage(ActorRef lobby, User user) {
        super(lobby);
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
        return new UserSessionStageLogic(shape());
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
     * This class defines the logic for a stage where all events received from the lobby's event bus
     * are communicated to the stage's outlet, but input is not processed.
     *
     * @author Fabian Mastenbroek
     */
    public class UserSessionStageLogic extends SessionStage.SessionStageLogic {
        /**
         * Construct a {@link UserSessionStageLogic} instance.
         *
         * @param shape The shape of the stage.
         */
        public UserSessionStageLogic(Shape shape) {
            super(shape);
        }

        /**
         * Return the {@link OutHandler} of this stage.
         *
         * @return The {@link OutHandler} of this stage.
         */
        @Override
        protected InHandler getInHandler() {
            return new AbstractInHandler() {
                @Override
                public void onPush() throws Exception {
                    log().info("Received message");
                    // tell lobby
                    getLobby().tell(grab(in), stageActor().ref());

                    // pull next message
                    pull(in);
                }
            };
        }
    }
}

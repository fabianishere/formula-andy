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

package nl.tudelft.fa.frontend.javafx.net;

import static scala.compat.java8.JFunction.func;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.japi.pf.ReceiveBuilder;
import akka.stream.*;
import akka.stream.stage.*;
import nl.tudelft.fa.client.lobby.message.LobbyInboundMessage;
import nl.tudelft.fa.client.lobby.message.LobbyOutboundMessage;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * A {@link GraphStage} that provides a bi-directional communication channel with a
 * {@link SessionActor}.
 *
 * @author Fabian Mastenbroek
 */
public class SessionStage extends GraphStage<FlowShape<LobbyOutboundMessage, LobbyInboundMessage>> {
    /**
     * The inlet of this graph stage.
     */
    public final Inlet<LobbyOutboundMessage> in = Inlet.create(String.format("%s.in",
        getClass().getName()));

    /**
     * The outlet of this graph stage.
     */
    public final Outlet<LobbyInboundMessage> out = Outlet.create(String.format("%s.out",
        getClass().getName()));

    /**
     * The {@link FlowShape} of this graph stage.
     */
    private final FlowShape<LobbyOutboundMessage, LobbyInboundMessage> shape =
        FlowShape.of(in, out);

    /**
     * The reference to the {@link SessionActor} between which the communication takes place.
     */
    private final ActorRef session;

    /**
     * Construct a {@link SessionStage} instance.
     *
     * @param session The reference to the session between which the communication takes place.
     */
    public SessionStage(ActorRef session) {
        this.session = session;
    }

    /**
     * Return the shape of this graph stage.
     * {@inheritDoc}
     *
     * @return The shape of this graph stage.
     */
    @Override
    public FlowShape<LobbyOutboundMessage, LobbyInboundMessage> shape() {
        return shape;
    }

    /**
     * Create the logic of this graph stage.
     *
     * @param inheritedAttributes The inherited attributes.
     * @return The logic of this graph stage.
     */
    @Override
    public GraphStageLogic createLogic(Attributes inheritedAttributes) {
        return new SessionStageLogic(shape());
    }

    /**
     * Return the reference to the {@link SessionActor} instance that is communication with this
     * session.
     *
     * @return The reference to the session actor.
     */
    public ActorRef getSession() {
        return session;
    }

    /**
     * This class defines the logic for a stage where all events received from the lobby's event bus
     * are communicated to the stage's outlet, and all input received from the stage's inlet is
     * passed to the lobby.
     *
     * @author Fabian Mastenbroek
     */
    public class SessionStageLogic extends GraphStageLogicWithLogging {
        /**
         * This partial function handles the messages received as actor.
         */
        private final PartialFunction<Object, BoxedUnit> receive =  ReceiveBuilder
            .match(Terminated.class, msg -> completeStage())
            .match(LobbyInboundMessage.class, msg -> emit(out, msg))
            .build();

        /**
         * Construct a {@link SessionStageLogic} instance.
         *
         * @param shape The shape of the stage.
         */
        public SessionStageLogic(Shape shape) {
            super(shape);
        }

        /**
         * This method is invoked before the start of this actor.
         */
        @Override
        public void preStart() {
            StageActor self = getStageActor(func(
                tuple -> receive.isDefinedAt(tuple._2) ? receive.apply(tuple._2) : BoxedUnit.UNIT));
            ActorRef ref = self.ref();

            // request first message
            pull(in);

            // subscribe to the lobby
            session.tell(ref, ref);

            // watch the lobby for termination
            self.watch(session);
        }

        {
            setHandler(in, new AbstractInHandler() {
                @Override
                public void onPush() throws Exception {
                    // tell lobby
                    session.tell(grab(in), stageActor().ref());

                    // pull next message
                    pull(in);
                }

                @Override
                public void onUpstreamFinish() {
                    // prevent completing the stage if the upstream has finished
                    // because we will still receive messages from the lobby's event bus
                    // to send downstream.
                }
            });

            setHandler(out, new AbstractOutHandler() {
                @Override
                public void onPull() throws Exception {
                    // just wait for messages to be published in the event bus
                }

                @Override
                public void onDownstreamFinish() {
                    // prevent completing the stage if the downstream has finished
                    // because we will still receive messages from the lobby's event bus
                    // to send downstream.
                }
            });
        }
    }
}


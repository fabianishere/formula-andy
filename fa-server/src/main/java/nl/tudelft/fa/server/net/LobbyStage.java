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

import static scala.compat.java8.JFunction.func;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.japi.pf.PFBuilder;
import akka.japi.pf.ReceiveBuilder;
import akka.stream.*;
import akka.stream.stage.*;
import nl.tudelft.fa.core.lobby.actor.LobbyActor;
import nl.tudelft.fa.core.lobby.message.Join;
import nl.tudelft.fa.core.lobby.message.LobbyInboundMessage;
import nl.tudelft.fa.core.lobby.message.LobbyOutboundMessage;
import nl.tudelft.fa.core.lobby.message.Subscribe;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * A {@link GraphStage} that provides a bi-directional communication channel with a
 * {@link LobbyActor}.
 *
 * @author Fabian Mastenbroek
 */
public class LobbyStage extends GraphStage<FlowShape<LobbyInboundMessage, LobbyOutboundMessage>> {
    /**
     * The inlet of this graph stage.
     */
    public final Inlet<LobbyInboundMessage> in = Inlet.create(String.format("%s.in",
        getClass().getName()));

    /**
     * The outlet of this graph stage.
     */
    public final Outlet<LobbyOutboundMessage> out = Outlet.create(String.format("%s.out",
        getClass().getName()));

    /**
     * The {@link FlowShape} of this graph stage.
     */
    private final FlowShape<LobbyInboundMessage, LobbyOutboundMessage> shape =
        FlowShape.of(in, out);

    /**
     * The reference to the {@link LobbyActor} between which the communication takes place.
     */
    private final ActorRef lobby;

    /**
     * Construct a {@link LobbyStage} instance.
     *
     * @param lobby The reference to the lobby between which the communication takes place.
     */
    public LobbyStage(ActorRef lobby) {
        this.lobby = lobby;
    }

    /**
     * Return the shape of this graph stage.
     * {@inheritDoc}
     *
     * @return The shape of this graph stage.
     */
    @Override
    public FlowShape<LobbyInboundMessage, LobbyOutboundMessage> shape() {
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
        return new LobbyStageLogic(shape());
    }

    /**
     * Return the reference to the {@link LobbyActor} instance that is communication with this
     * session.
     *
     * @return The reference to the lobby actor.
     */
    public ActorRef getLobby() {
        return lobby;
    }

    /**
     * This class defines the logic for a stage where all events received from the lobby's event bus
     * are communicated to the stage's outlet, and all input received from the stage's inlet is
     * passed to the lobby.
     *
     * @author Fabian Mastenbroek
     */
    public class LobbyStageLogic extends GraphStageLogicWithLogging {
        /**
         * This partial function attaches the session handler to messages.
         */
        private final PartialFunction<LobbyInboundMessage, LobbyInboundMessage> attach =
            new PFBuilder<LobbyInboundMessage, LobbyInboundMessage>()
                .match(Join.class, msg -> new Join(msg.getUser(), stageActor().ref()))
                .matchAny(msg -> msg)
                .build();

        /**
         * Construct a {@link LobbyStageLogic} instance.
         *
         * @param shape The shape of the stage.
         */
        public LobbyStageLogic(Shape shape) {
            super(shape);
        }

        /**
         * This method is invoked before the start of this actor.
         */
        @Override
        public void preStart() {
            PartialFunction<Object, BoxedUnit> receive = this.receive();
            StageActor self = getStageActor(func(tuple -> receive.apply(tuple._2)));
            ActorRef ref = self.ref();

            // request first message
            pull(in);

            // subscribe to the lobby
            lobby.tell(new Subscribe(ref), ref);

            // watch the lobby for termination
            self.watch(lobby);
        }

        /**
         * This method defines the initial actor behavior, it must return a partial function with
         * the actor logic.
         *
         * @return The initial actor behavior as a partial function.
         */
        private PartialFunction<Object, BoxedUnit> receive() {
            return ReceiveBuilder
                .match(Terminated.class, msg -> completeStage())
                .match(LobbyOutboundMessage.class, msg -> push(out, msg))
                .build();
        }

        /**
         * Return the {@link InHandler} of this stage.
         *
         * @return The {@link InHandler} of this stage.
         */
        protected InHandler getInHandler() {
            return new AbstractInHandler() {
                @Override
                public void onPush() throws Exception {
                    // tell lobby
                    lobby.tell(attach.apply(grab(in)), stageActor().ref());

                    // pull next message
                    pull(in);
                }
            };
        }

        /**
         * Return the {@link OutHandler} of this stage.
         *
         * @return The {@link OutHandler} of this stage.
         */
        protected OutHandler getOutHandler() {
            return new AbstractOutHandler() {
                @Override
                public void onPull() throws Exception {
                    // just wait for messages to be published in the event bus
                }
            };
        }

        {
            setHandler(in, getInHandler());
            setHandler(out, getOutHandler());
        }
    }
}

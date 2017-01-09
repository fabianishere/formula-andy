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

package nl.tudelft.fa.core.lobby.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import akka.stream.actor.AbstractActorPublisher;
import akka.stream.actor.ActorPublisher;
import akka.stream.actor.ActorPublisherMessage;
import nl.tudelft.fa.core.lobby.message.Subscribe;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * A {@link ActorPublisher} that publishes all the event published by the event bus to an Akka
 * stream.
 *
 * @author Fabian Mastenbroek
 */
public class LobbyEventBusPublisher extends AbstractActorPublisher<Object> {
    /**
     * The {@link ActorRef} to the {@link LobbyActor} this publisher is subscribed to.
     */
    private final ActorRef lobby;

    /**
     * The maximum size of the buffer.
     */
    private final int bufferMaximumSize = 100;

    /**
     * The buffer to hold the messages.
     */
    private final Queue<Object> buffer = new ArrayBlockingQueue<>(bufferMaximumSize);

    /**
     * Construct a {@link LobbyEventBusPublisher} instance.
     *
     * @param lobby The reference to the lobby to publish the event bus of.
     */
    private LobbyEventBusPublisher(ActorRef lobby) {
        this.lobby = lobby;
        this.lobby.tell(new Subscribe(self()), self());
    }

    /**
     * This method defines the initial actor behavior, it must return a partial function with the
     * actor logic.
     *
     * @return The initial actor behavior as a partial function.
     */
    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return ReceiveBuilder
            .match(ActorPublisherMessage.Request.class, request -> deliverBuffer())
            .match(ActorPublisherMessage.Cancel.class, cancel -> context().stop(self()))
            .match(Object.class, msg -> sender().equals(lobby), msg -> {
                buffer.add(msg);
                deliverBuffer();
            })
            .build();
    }

    /**
     * Deliver the buffer to the stream.
     */
    private void deliverBuffer() {
        while (!buffer.isEmpty() && isActive() && totalDemand() > 0) {
            onNext(buffer.poll());
        }
    }

    /**
     * Create {@link Props} instance for an actor of this type.
     *
     * @param lobby The reference to the lobby to publish the event bus of.
     * @return A Props for creating this actor, which can then be further configured
     *         (e.g. calling `.withDispatcher()` on it)
     */
    public static Props props(ActorRef lobby) {
        return Props.create(LobbyEventBusPublisher.class,
            () -> new LobbyEventBusPublisher(lobby));
    }
}

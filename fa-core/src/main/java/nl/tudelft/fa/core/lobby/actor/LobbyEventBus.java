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

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.japi.pf.ReceiveBuilder;
import nl.tudelft.fa.core.lobby.message.LobbyEvent;
import nl.tudelft.fa.core.lobby.message.Subscribe;
import nl.tudelft.fa.core.lobby.message.Unsubscribe;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.util.HashSet;
import java.util.Set;

/**
 * An event bus actor to which a {@link LobbyActor} publishes its events.
 *
 * @author Fabian Mastenbroek
 */
public class LobbyEventBus extends AbstractActor {
    /**
     * The subscriptions of this event bus.
     */
    private Set<ActorRef> subscriptions = new HashSet<>();

    /**
     * Construct a {@link LobbyEventBus} instance.
     */
    private LobbyEventBus() {}

    /**
     * This method defines the initial actor behavior, it must return a partial function with the
     * actor logic.
     *
     * @return The initial actor behavior as a partial function.
     */
    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return ReceiveBuilder
            .match(Subscribe.class, req -> subscribe(req.getActor()))
            .match(Unsubscribe.class, req -> unsubscribe(req.getActor()))
            .match(Terminated.class, msg -> unsubscribe(msg.actor()))
            .match(LobbyEvent.class, this::publish)
            .build();
    }

    /**
     * Subscribe to this {@link LobbyActor}
     *
     * @param actor The actor that wants to subscribe to this lobby.
     */
    private void subscribe(ActorRef actor) {
        context().watch(actor);
        subscriptions.add(actor);
    }

    /**
     * Unsubscribe from this {@link LobbyActor}
     *
     * @param actor The actor that wants to unsubscribe from this lobby.
     */
    private void unsubscribe(ActorRef actor) {
        context().unwatch(actor);
        subscriptions.remove(actor);
    }

    /**
     * Publish the given event to the subscribers of this event bus.
     *
     * @param event The event to publish.
     */
    private void publish(LobbyEvent event) {
        subscriptions.forEach(subscriber -> subscriber.tell(event, sender()));
    }


    /**
     * Create {@link Props} instance for an actor of this type.
     *
     * @return A Props for creating this actor, which can then be further configured
     *         (e.g. calling `.withDispatcher()` on it)
     */
    public static Props props() {
        return Props.create(LobbyEventBus.class);
    }
}

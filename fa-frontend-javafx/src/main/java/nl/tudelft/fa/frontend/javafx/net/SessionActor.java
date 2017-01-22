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

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.japi.pf.ReceiveBuilder;
import nl.tudelft.fa.client.lobby.message.LobbyInboundMessage;
import nl.tudelft.fa.client.lobby.message.LobbyOutboundMessage;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.util.HashSet;
import java.util.Set;

/**
 * This actor handles the incoming {@link LobbyOutboundMessage} instances.
 *
 * @author Fabian Mastenbroek
 */
public class SessionActor extends AbstractActor {
    /**
     * The subscribers of this session.
     */
    private final Set<ActorRef> subscribers;

    /**
     * Construct a {@link SessionActor}.
     */
    public SessionActor() {
        this.subscribers = new HashSet<>();
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
            .match(ActorRef.class, this::subscribe)
            .match(Terminated.class, msg -> unsubscribe(msg.actor()))
            .match(LobbyOutboundMessage.class, msg ->
                subscribers.forEach(sub -> sub.tell(msg, sender())))
            .match(LobbyInboundMessage.class, msg ->
                subscribers.forEach(sub -> sub.tell(msg, sender())))
            .build();
    }

    /**
     * Subscribe to this session.
     *
     * @param ref The reference to the actor that wants to subscribe.
     */
    private void subscribe(ActorRef ref) {
        subscribers.add(ref);
        context().watch(ref);
    }

    /**
     * Unsubscribe from this session.
     *
     * @param ref The reference to the actor that wants to unsubscribe.
     */
    private void unsubscribe(ActorRef ref) {
        subscribers.remove(ref);
        context().unwatch(ref);
    }

    /**
     * Create {@link Props} instance for an actor of this type.
     *
     * @return A Props for creating this actor, which can then be further configured
     *         (e.g. calling `.withDispatcher()` on it)
     */
    public static Props props() {
        return Props.create(SessionActor.class);
    }
}

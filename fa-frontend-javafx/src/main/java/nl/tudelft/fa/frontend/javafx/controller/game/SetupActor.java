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

package nl.tudelft.fa.frontend.javafx.controller.game;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import nl.tudelft.fa.client.lobby.LobbyStatus;
import nl.tudelft.fa.client.lobby.message.LobbyStatusChanged;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * The actor that handles the logic for the setup screen.
 *
 * @author Fabian Mastenbroek
 */
public class SetupActor extends AbstractActor {
    /**
     * The controller of the actor.
     */
    private final SetupScreenController controller;

    /**
     * The {@link LoggingAdapter} of this class.
     */
    private final LoggingAdapter log = Logging.getLogger(context().system(), this);

    /**
     * Construct a {@link SetupActor} instance.
     *
     * @param controller The controller of the actor.
     */
    public SetupActor(SetupScreenController controller) {
        this.controller = controller;
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
            .match(LobbyStatusChanged.class,
                msg -> msg.getStatus().equals(LobbyStatus.PREPARATION),
                msg -> {
                    log.info("Lobby is going in preparation!");
                })
            .build();
    }

    /**
     * Create {@link Props} instance for an actor of this type.
     *
     * @param controller The controller of this actor.
     * @return A Props for creating this actor, which can then be further configured
     *         (e.g. calling `.withDispatcher()` on it)
     */
    public static Props props(SetupScreenController controller) {
        return Props.create(SetupActor.class, () -> new SetupActor(controller));
    }
}

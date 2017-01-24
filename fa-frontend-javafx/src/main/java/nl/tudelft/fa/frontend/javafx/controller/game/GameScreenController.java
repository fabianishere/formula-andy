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
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import nl.tudelft.fa.client.lobby.message.LobbyStatusChanged;
import nl.tudelft.fa.client.race.CarSimulationResult;
import nl.tudelft.fa.client.race.RaceSimulationResult;
import nl.tudelft.fa.frontend.javafx.Main;
import nl.tudelft.fa.frontend.javafx.controller.AbstractController;
import nl.tudelft.fa.frontend.javafx.service.ClientService;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javax.inject.Inject;

/**
 * The controller for the game screen.
 *
 * @author Christian Slothouber
 * @author Fabian Mastenbroek
 */
public class GameScreenController extends AbstractController implements Initializable {
    /**
     * The reference to the location of the view of this controller.
     */
    public static final URL VIEW = Main.class.getResource("view/game/game.fxml");

    /**
     * The injected client service.
     */
    @Inject
    private ClientService service;

    /**
     * The table where the results are showed.
     */
    @FXML
    private TableView<CarSimulationResult> table;

    /**
     * This method is called when the screen is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        ActorRef ref = service.system().actorOf(Props.create(GameActor.class,
            GameActor::new).withDispatcher("javafx-dispatcher"));
        service.session().tell(ref, ref);
    }

    /**
     * The actor that handles the logic for the setup screen.
     *
     * @author Fabian Mastenbroek
     */
    public class GameActor extends AbstractActor {
        /**
         * The {@link LoggingAdapter} of this class.
         */
        private final LoggingAdapter log = Logging.getLogger(context().system(), this);

        /**
         * This method defines the initial actor behavior, it must return a partial function with
         * the actor logic.
         *
         * @return The initial actor behavior as a partial function.
         */
        @Override
        public PartialFunction<Object, BoxedUnit> receive() {
            return ReceiveBuilder
                .match(RaceSimulationResult.class, msg -> {
                    table.setItems(FXCollections.observableList(msg.getResults().stream().sorted(
                        Comparator.comparingDouble(CarSimulationResult::getDistanceTraveled)
                            .reversed())
                        .collect(Collectors.toList())));
                })
                .match(LobbyStatusChanged.class, msg -> {
                    log.info("Game is over. Going back to setup!");
                    show(SetupScreenController.VIEW);
                })
                .build();
        }
    }
}

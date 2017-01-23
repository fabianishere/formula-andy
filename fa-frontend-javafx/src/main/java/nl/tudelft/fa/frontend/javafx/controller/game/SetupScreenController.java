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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import nl.tudelft.fa.client.lobby.LobbyStatus;
import nl.tudelft.fa.client.lobby.message.CarParametersSubmission;
import nl.tudelft.fa.client.lobby.message.LobbyStatusChanged;
import nl.tudelft.fa.client.lobby.message.TeamConfigurationSubmission;
import nl.tudelft.fa.client.race.CarConfiguration;
import nl.tudelft.fa.client.team.Team;
import nl.tudelft.fa.client.team.inventory.Car;
import nl.tudelft.fa.frontend.javafx.Main;
import nl.tudelft.fa.frontend.javafx.controller.AbstractController;
import nl.tudelft.fa.frontend.javafx.controller.StoreController;
import nl.tudelft.fa.frontend.javafx.service.ClientService;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

import javax.inject.Inject;

/**
 * The controller for the setup screen.
 *
 * @author Fabian Mastenbroek
 * @author Christian Slothouber
 * @author Laetitia Molkenboer
 */
public class SetupScreenController extends AbstractController implements Initializable {
    /**
     * The reference to the location of the view of this controller.
     */
    public static final URL VIEW = Main.class.getResource("view/game/setup.fxml");

    /**
     * The injected client service.
     */
    @Inject
    private ClientService service;

    /**
     * The controller for the first car configuration.
     */
    @FXML
    private CarConfigurationController firstController;

    /**
     * The controller for the second car configuration.
     */
    @FXML
    private CarConfigurationController secondController;

    /**
     * The status of the lobby.
     */
    @FXML
    private Label status;

    /**
     * This method is invoked when the store button is pressed.
     *
     * @throws Exception if the controller failed to load the store view.
     */
    @FXML
    protected void store() throws Exception {
        show(StoreController.VIEW);
    }

    /**
     * This method is invoked when the submit button is pressed.
     */
    @FXML
    private void submit() {
        logger.info("Submitting configuration and parameters");

        service.session().tell(getConfiguration(), ActorRef.noSender());
        service.session().tell(getParameters(firstController), ActorRef.noSender());
        service.session().tell(getParameters(secondController), ActorRef.noSender());
    }

    /**
     * Set the team for this controller.
     *
     * @param team The team to set.
     */
    public void setTeam(Team team) {
        firstController.setTeam(team);
        secondController.setTeam(team);
    }

    /**
     * Return the configuration of the team.
     *
     * @return The configuration of the team.
     */
    public TeamConfigurationSubmission getConfiguration() {
        return new TeamConfigurationSubmission(null, new HashSet<CarConfiguration>() {
            {
                add(firstController.getConfiguration());
                add(secondController.getConfiguration());
            }
        });
    }

    /**
     * Return the parameters for a car.
     *
     * @param controller The controller to use.
     * @return The parameters for a car.
     */
    public CarParametersSubmission getParameters(CarConfigurationController controller) {
        return new CarParametersSubmission(null, controller.getConfiguration().getCar(),
            controller.getParameters());
    }

    /**
     * This method is called when the screen is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ActorRef ref = service.system().actorOf(Props.create(SetupScreenActor.class,
            SetupScreenActor::new).withDispatcher("javafx-dispatcher"));
        service.session().tell(ref, ref);
    }

    /**
     * The actor that handles the logic for the setup screen.
     *
     * @author Fabian Mastenbroek
     */
    public class SetupScreenActor extends AbstractActor {
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
                .match(LobbyStatusChanged.class,
                    msg -> msg.getStatus().equals(LobbyStatus.PREPARATION),
                    msg -> {
                        log.info("Lobby is going in preparation!");
                        status.setText(msg.getStatus().toString());
                    })
                .match(LobbyStatusChanged.class,
                    msg -> msg.getStatus().equals(LobbyStatus.PROGRESSION),
                    msg -> {
                        log.info("Race is starting in lobby!");
                        show(GameScreenController.VIEW);
                        context().stop(self());
                    })
                .build();
        }
    }
}

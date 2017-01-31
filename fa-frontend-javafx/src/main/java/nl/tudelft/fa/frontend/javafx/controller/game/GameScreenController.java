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
import com.jfoenix.controls.JFXSnackbar;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import nl.tudelft.fa.client.lobby.Lobby;
import nl.tudelft.fa.client.lobby.message.LobbyStatusChanged;
import nl.tudelft.fa.client.lobby.message.RequestInformation;
import nl.tudelft.fa.client.race.CarSimulationResult;
import nl.tudelft.fa.client.race.GrandPrix;
import nl.tudelft.fa.client.race.RaceSimulationResult;
import nl.tudelft.fa.client.team.Team;
import nl.tudelft.fa.frontend.javafx.Main;
import nl.tudelft.fa.frontend.javafx.controller.AbstractController;
import nl.tudelft.fa.frontend.javafx.service.ClientService;
import nl.tudelft.fa.frontend.javafx.service.TeamService;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
     * The injected team service.
     */
    @Inject
    private TeamService teamService;

    /**
     * The table where the results are showed.
     */
    @FXML
    private TableView<CarSimulationResult> table;

    /**
     * The position column.
     */
    @FXML
    private TableColumn<CarSimulationResult, Number> position;

    /**
     * The name column.
     */
    @FXML
    private TableColumn<CarSimulationResult, CarSimulationResult> name;

    /**
     * The laps column.
     */
    @FXML
    private TableColumn<CarSimulationResult, String> laps;

    /**
     * The snackbar to use.
     */
    @FXML
    private JFXSnackbar snackbar;

    /**
     * The {@link Lobby} we are in.
     */
    private Lobby lobby;

    /**
     * This method is called when the screen is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        /* Initialize snackbar */
        snackbar.registerSnackbarContainer((StackPane) root);

        ActorRef ref = service.system().actorOf(Props.create(GameActor.class,
            GameActor::new).withDispatcher("javafx-dispatcher"));
        service.session().tell(ref, ref);
        service.session().tell(RequestInformation.INSTANCE, ref);

        position.setCellValueFactory(cell -> new SimpleIntegerProperty(
            table.getItems().indexOf(cell.getValue()) + 1));

        name.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue()));
        name.setCellFactory(view -> new TableCell<CarSimulationResult, CarSimulationResult>() {
            @Override
            protected void updateItem(CarSimulationResult item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    if (item.hasFinished()) {
                        Text icon = GlyphsDude.createIcon(MaterialDesignIcon.FLAG_CHECKERED);
                        icon.setFill(Color.WHITE);
                        setGraphic(icon);
                    } else if (item.hasCrashed()) {
                        Text icon = GlyphsDude.createIcon(MaterialDesignIcon.ALERT_OUTLINE);
                        icon.setFill(Color.WHITE);
                        setGraphic(icon);
                    } else {
                        setGraphic(null);
                    }
                    setText(toName(item));
                }
            }
        });
        laps.setCellValueFactory(cell -> new SimpleStringProperty(toLaps(cell.getValue())));
    }

    /**
     * Convert a car to name.
     *
     * @param result The car simulation result.
     * @return The name of the driver and team.
     */
    private String toName(CarSimulationResult result) {
        Optional<String> team = lobby.getTeams().stream()
            .filter(t -> t.getInventory().contains(result.getConfiguration().getCar()))
            .findFirst()
            .map(Team::getName);

        return String.format("%s (%s)", result.getConfiguration().getDriver().getName(),
            team.orElse("Team Bot"));
    }

    /**
     * Convert the distance traveled to the amount of laps.
     *
     * @param result The car simulation result.
     * @return The laps the car has driven.
     */
    private String toLaps(CarSimulationResult result) {

        GrandPrix grandPrix = lobby.getSchedule().get(0);
        int laps = Math.min((int) result.getDistanceTraveled() * grandPrix.getLaps()
            / grandPrix.getCircuit().getLength(), grandPrix.getLaps());

        return String.format("%d/%d %s", laps, grandPrix.getLaps(),
            result.hasCrashed() ? "(CRASHED)" : "");
    }

    /**
     * Find the results of the cars of the user's team.
     *
     * @param results The results to search trough.
     * @return The results of the team.
     */
    private Stream<CarSimulationResult> getResultsForTeam(List<CarSimulationResult> results) {
        Team team = teamService.teamProperty().get();
        return results.stream()
            .filter(result -> team.getInventory().contains(result.getConfiguration().getCar()));
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
                    table.setItems(FXCollections.observableList(msg.getResults()));

                    if (msg.isFinished()) {
                        Stream<CarSimulationResult> results = getResultsForTeam(msg.getResults());
                        String message = "The race has finished: " + results.map(result ->
                            String.format("#%d %s (%s)", msg.getResults().indexOf(result) + 1,
                                result.getConfiguration(). getDriver().getName(),
                                teamService.teamProperty().get().getName()))
                            .collect(Collectors.joining(", "));
                        snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(message));
                    }
                })
                .match(LobbyStatusChanged.class, msg -> {
                    log.info("Game is over. Going back to setup!");
                })
                .match(Lobby.class, msg -> lobby = msg)
                .build();
        }
    }
}

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

package nl.tudelft.fa.frontend.javafx.controller.lobby;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import com.jfoenix.controls.JFXSnackbar;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import nl.tudelft.fa.client.lobby.Lobby;
import nl.tudelft.fa.client.lobby.LobbyStatus;
import nl.tudelft.fa.client.lobby.message.*;
import nl.tudelft.fa.frontend.javafx.Main;
import nl.tudelft.fa.frontend.javafx.controller.AbstractController;
import nl.tudelft.fa.frontend.javafx.controller.StartScreenController;
import nl.tudelft.fa.frontend.javafx.controller.team.TeamSelectionController;
import nl.tudelft.fa.frontend.javafx.dispatch.JavaFxExecutorService;
import nl.tudelft.fa.frontend.javafx.service.ClientService;
import nl.tudelft.fa.frontend.javafx.service.TeamService;
import scala.PartialFunction;
import scala.concurrent.duration.FiniteDuration;
import scala.runtime.BoxedUnit;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.inject.Inject;

/**
 * A controller for the lobby selection view.
 *
 * @author Fabian Mastenbroek
 * @author Christian Slothouber
 * @author Laetitia Molkenboer
 */
public class LobbySelectionController extends AbstractController {
    /**
     * The reference to the location of the view of this controller.
     */
    public static final URL VIEW = Main.class.getResource("view/lobby/selection.fxml");

    /**
     * The injected client service.
     */
    @Inject
    private ClientService client;

    /**
     * The injected team service.
     */
    @Inject
    private TeamService teamService;

    /**
     * The table from which the team is selected.
     */
    @FXML
    private TableView<Lobby> table;

    /**
     * The name column.
     */
    @FXML
    private TableColumn<Lobby, String> name;

    /**
     * The players in the lobby column.
     */
    @FXML
    private TableColumn<Lobby, String> users;

    /**
     * The status of the lobby.
     */
    @FXML
    private TableColumn<Lobby, String> status;

    /**
     * The window node.
     */
    @FXML
    private GridPane window;

    /**
     * The snackbar to use.
     */
    @FXML
    private JFXSnackbar snackbar;

    /**
     * This method is called when the screen is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        /* Initialize snackbar */
        snackbar.registerSnackbarContainer((StackPane) root);

        /* Cell value factories */
        name.setCellValueFactory(item -> new SimpleStringProperty(String.format("LOBBY #%d",
            item.getTableView().getItems().indexOf(item.getValue()) + 1)));

        users.setCellValueFactory(item -> new SimpleStringProperty(String.format("%d/%d USERS",
            item.getValue().getTeams().size(),
            item.getValue().getConfiguration().getTeamMaximum())));

        status.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getStatus()
            .toString()));

        table.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                Lobby lobby = table.getSelectionModel().getSelectedItem();
                join(lobby);
            }
        });

        /* Fetch the available lobbies every 2 seconds */
        client.system().scheduler().schedule(FiniteDuration.Zero(),
            FiniteDuration.create(2, TimeUnit.SECONDS), this::fetchLobbies,
            client.system().dispatcher());


        Platform.runLater(() -> {
            if (!teamService.teamProperty().isNull().get()) {
                return;
            }

            try {
                push(TeamSelectionController.VIEW);
            } catch (IOException exc) {
                logger.error("Failed to select team", exc);
                snackbar.fireEvent(new JFXSnackbar.SnackbarEvent("Failed to select team: "
                    + exc.getMessage()));
            }
        });
    }

    /**
     * Fetch the lobbies that can be joined.
     */
    private void fetchLobbies() {
        client.balancer().lobbies().thenAcceptAsync(lobbies -> {
            table.setItems(FXCollections.observableList(lobbies.stream()
                .collect(Collectors.toList())));
        }, JavaFxExecutorService.INSTANCE);
    }

    /**
     * Join the given {@link Lobby} instance.
     *
     * @param lobby The lobby to join.
     */
    private void join(Lobby lobby) {
        if (!LobbyStatus.INTERMISSION.equals(lobby.getStatus())
                || lobby.getTeams().size() >= lobby.getConfiguration().getTeamMaximum()) {
            logger.error("Failed to join lobby: lobby is full or not available.");
            snackbar.fireEvent(new JFXSnackbar.SnackbarEvent("Failed to join lobby:"
                + " lobby is full or not available."));
            return;
        }

        logger.info("Opening session for selected lobby");

        client.open(lobby).whenCompleteAsync((session, exc) -> {
            if (exc != null) {
                logger.error("Failed to join lobby", exc);
                snackbar.fireEvent(new JFXSnackbar.SnackbarEvent("Failed to join lobby: "
                    + exc.getMessage()));
            }

            logger.info("Session successfully opened!");

            ActorRef ref = client.system().actorOf(Props.create(LobbySelectionActor.class,
                    LobbySelectionActor::new).withDispatcher("javafx-dispatcher"));

            logger.info("Joining lobby now");

            // Tell the session about the handler
            session.tell(ref, ref);
            // Send the join request
            session.tell(new Join(teamService.teamProperty().getValue()), ActorRef.noSender());
        }, JavaFxExecutorService.INSTANCE);

    }

    /**
     * The actor that handles the logic for the lobby selection screen.
     *
     * @author Fabian Mastenbroek
     */
    public class LobbySelectionActor extends AbstractActor {
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
                .match(JoinSuccess.class, msg -> {
                    context().stop(self());
                    show(LobbyMainController.VIEW);
                })
                .match(JoinException.class, exc -> {
                    log.error("Failed to join lobby", exc);
                    snackbar.fireEvent(new JFXSnackbar.SnackbarEvent("Failed to join lobby: "
                        + exc.getMessage()));
                })
                .build();
        }
    }
}

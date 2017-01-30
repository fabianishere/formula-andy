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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import nl.tudelft.fa.client.lobby.Lobby;
import nl.tudelft.fa.client.lobby.LobbyStatus;
import nl.tudelft.fa.client.lobby.message.Join;
import nl.tudelft.fa.client.lobby.message.JoinException;
import nl.tudelft.fa.client.lobby.message.JoinSuccess;
import nl.tudelft.fa.frontend.javafx.Main;
import nl.tudelft.fa.frontend.javafx.controller.AbstractController;
import nl.tudelft.fa.frontend.javafx.controller.StartScreenController;
import nl.tudelft.fa.frontend.javafx.controller.team.TeamSelectionController;
import nl.tudelft.fa.frontend.javafx.dispatch.JavaFxExecutorService;
import nl.tudelft.fa.frontend.javafx.service.ClientService;
import nl.tudelft.fa.frontend.javafx.service.TeamService;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.inject.Inject;

/**
 * A controller for the main lobby view.
 *
 * @author Fabian Mastenbroek
 * @author Christian Slothouber
 * @author Laetitia Molkenboer
 */
public class LobbyMainController extends AbstractController {
    /**
     * The reference to the location of the view of this controller.
     */
    public static final URL VIEW = Main.class.getResource("view/lobby/main.fxml");

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
    }
}

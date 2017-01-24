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

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import nl.tudelft.fa.client.net.message.NotAuthorizedException;
import nl.tudelft.fa.client.team.Team;
import nl.tudelft.fa.frontend.javafx.Main;
import nl.tudelft.fa.frontend.javafx.controller.AbstractController;
import nl.tudelft.fa.frontend.javafx.controller.StartScreenController;
import nl.tudelft.fa.frontend.javafx.dispatch.JavaFxExecutorService;
import nl.tudelft.fa.frontend.javafx.service.ClientService;

import java.net.URL;
import java.util.ResourceBundle;
import javax.inject.Inject;

/**
 * The controller for the view where the team is selected.
 *
 * @author Fabian Mastenbroek
 * @author Christian Slothouber
 * @author Laetitia Molkenboer
 */
public class GameLoadController extends AbstractController implements Initializable {
    /**
     * The reference to the location of the view of this controller.
     */
    public static final URL VIEW = Main.class.getResource("view/game/load.fxml");

    /**
     * The current connection with the server.
     */
    @Inject
    private ClientService service;

    /**
     * The teams the user can choose.
     */
    @FXML
    private ComboBox<Team> team;

    /**
     * This method is called when the back-button is pressed.
     *
     * @param event The event
     * @throws Exception If it fails the method throws an Exception.
     */
    @FXML
    protected void back(ActionEvent event) throws Exception {
        show(StartScreenController.VIEW);
    }

    /**
     * This method is called when the next-button is pressed.
     *
     * @param event The event
     * @throws Exception If it fails the method throws an Exception.
     */
    @FXML
    protected void next(ActionEvent event) throws Exception {
        show(SetupScreenController.VIEW);
        SetupScreenController controller = loader.getController();
        controller.setTeam(team.getValue());
    }

    /**
     * This method is called when the screen is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            service.teams().list().whenCompleteAsync((teams, throwable) -> {
                team.setItems(FXCollections.observableList(teams));

                if (throwable != null) {
                    logger.error("Failed to retrieve user's teams from the server", throwable);
                }
            }, JavaFxExecutorService.INSTANCE);
        } catch (NotAuthorizedException e) {
            logger.error("The user was not authorized to load this view", e);
        }
    }
}

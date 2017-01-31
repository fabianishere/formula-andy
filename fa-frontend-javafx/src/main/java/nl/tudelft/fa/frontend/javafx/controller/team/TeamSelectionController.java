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

package nl.tudelft.fa.frontend.javafx.controller.team;

import javafx.fxml.FXML;
import javafx.scene.layout.TilePane;
import nl.tudelft.fa.client.net.message.NotAuthorizedException;
import nl.tudelft.fa.frontend.javafx.Main;
import nl.tudelft.fa.frontend.javafx.controller.AbstractController;
import nl.tudelft.fa.frontend.javafx.dispatch.JavaFxExecutorService;
import nl.tudelft.fa.frontend.javafx.service.ClientService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.inject.Inject;

/**
 * The controller for the team selection screen.
 *
 * @author Fabian Mastenbroek
 */
public class TeamSelectionController extends AbstractController {
    /**
     * The reference to the location of the view of this controller.
     */
    public static final URL VIEW = Main.class.getResource("view/team/selection.fxml");

    /**
     * The injected client service.
     */
    @Inject
    private ClientService service;

    /**
     * The {@link TilePane} that contains the teams.
     */
    @FXML
    private TilePane items;

    /**
     * This method is called when the screen is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        try {
            service.teams().list().thenAcceptAsync(teams -> teams.forEach(team -> {
                loader.setLocation(TeamSelectionItemController.VIEW);
                loader.setController(null);
                loader.setRoot(null);

                try {
                    items.getChildren().add(0, loader.load());
                    TeamSelectionItemController controller = loader.getController();
                    controller.setTeam(team);
                } catch (IOException exc) {
                    logger.error("Failed to load team", exc);
                }
            }), JavaFxExecutorService.INSTANCE);
        } catch (NotAuthorizedException exc) {
            logger.error("Failed to fetch teams", exc);
        }
    }
}

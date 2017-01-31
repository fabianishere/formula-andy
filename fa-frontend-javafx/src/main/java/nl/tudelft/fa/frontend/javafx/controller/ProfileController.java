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

package nl.tudelft.fa.frontend.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import nl.tudelft.fa.client.team.Team;
import nl.tudelft.fa.frontend.javafx.controller.team.TeamSelectionController;
import nl.tudelft.fa.frontend.javafx.service.TeamService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

/**
 * This controller manages the profile view.
 *
 * @author Fabian Mastenbroek
 */
public class ProfileController extends AbstractController {
    /**
     * The team service.
     */
    @Inject
    private TeamService teamService;

    /**
     * The name of the team.
     */
    @FXML
    private Label name;

    /**
     * The image of the team.
     */
    @FXML
    private ImageView image;

    /**
     * The budget of the team.
     */
    @FXML
    private Label budget;

    /**
     * This method is called when the screen is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        Team team = teamService.teamProperty().get();

        if (team != null) {
            update(team);
        }

        teamService.teamProperty().addListener((observable, prev, next) -> update(next));
    }

    /**
     * Open the team selection view.
     */
    @FXML
    private void select() throws IOException {
        push(TeamSelectionController.VIEW);
    }

    /**
     * Update the view with the given team.
     *
     * @param team The team to update the view with.
     */
    private void update(Team team) {
        name.setText(team.getName());
        budget.setText("$" + team.getBudget());
    }
}

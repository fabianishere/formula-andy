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

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import nl.tudelft.fa.client.team.Driver;
import nl.tudelft.fa.client.team.Mechanic;
import nl.tudelft.fa.client.team.Member;
import nl.tudelft.fa.client.team.Team;
import nl.tudelft.fa.client.team.inventory.InventoryItem;
import nl.tudelft.fa.frontend.javafx.service.ClientService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    public static final URL VIEW = SetupScreenController.class.getResource("../setup-screen.fxml");

    /**
     * The {@link ClientService} that provides the connection with the server.
     */
    @Inject
    private ClientService client;

    @FXML
    private ComboBox<Mechanic> mechanic1;

    @FXML
    private ComboBox<Mechanic> mechanic2;

    @FXML
    private ComboBox<Driver> driver1;

    @FXML
    private ComboBox<Driver> driver2;

    @FXML
    protected void store(ActionEvent event) throws Exception {
        show(event, StoreController.VIEW);
    }

    @FXML
    protected void next(ActionEvent event) throws Exception {
        show(event, GameScreenController.VIEW);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final Team team;
        try {
            team = client.teams().list().toCompletableFuture().get().get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        initializeStaff(mechanic1, Mechanic.class, team);
        initializeStaff(mechanic2, Mechanic.class, team);

        initializeStaff(driver1, Driver.class, team);
        initializeStaff(driver2, Driver.class, team);
    }

    private <T extends Member> void initializeStaff(ComboBox<T> box, Class<T> target, Team team) {
        List<T> items = team.getStaff().stream()
            .filter(target::isInstance)
            .map(target::cast)
            .collect(Collectors.toList());

        box.setItems(FXCollections.observableList(items));
    }

    private <T extends InventoryItem> void initializeInventory(ComboBox<T> box, Class<T> target,
                                                               Team team) {
        List<T> items = team.getInventory().stream()
            .filter(target::isInstance)
            .map(target::cast)
            .collect(Collectors.toList());

        box.setItems(FXCollections.observableList(items));
    }
}

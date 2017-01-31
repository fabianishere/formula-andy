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
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ToggleGroup;
import nl.tudelft.fa.client.race.CarConfiguration;
import nl.tudelft.fa.client.race.CarParameters;
import nl.tudelft.fa.client.team.*;
import nl.tudelft.fa.client.team.inventory.Car;
import nl.tudelft.fa.client.team.inventory.Engine;
import nl.tudelft.fa.client.team.inventory.InventoryItem;
import nl.tudelft.fa.client.team.inventory.Tire;
import nl.tudelft.fa.frontend.javafx.controller.AbstractController;
import nl.tudelft.fa.frontend.javafx.service.TeamService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.inject.Inject;

/**
 * The controller for the setup of a car.
 *
 * @author Fabian Mastenbroek
 * @author Christian Slothouber
 * @author Laetitia Molkenboer
 */
public class CarConfigurationController extends AbstractController {
    /**
     * The injected team service.
     */
    @Inject
    private TeamService teamService;

    /**
     * The mechanic risk of the configuration.
     */
    @FXML
    private ToggleGroup mechanicRisk;

    /**
     * The mechanic of the configuration.
     */
    @FXML
    private ComboBox<Mechanic> mechanic;

    /**
     * The aerodynamic risk of the configuration.
     */
    @FXML
    private ToggleGroup aeroRisk;

    /**
     * The aerodynamicist of the configuration.
     */
    @FXML
    private ComboBox<Aerodynamicist> aerodynamicist;

    /**
     * The strategic risk of the configuration.
     */
    @FXML
    private ToggleGroup strategicRisk;

    /**
     * The strategist of the configuration.
     */
    @FXML
    private ComboBox<Strategist> strategist;

    /**
     * The driver of the configuration.
     */
    @FXML
    private ComboBox<Driver> driver;

    /**
     * The engine of the configuration.
     */
    @FXML
    private ComboBox<Engine> engine;

    /**
     * The tire of the configuration.
     */
    @FXML
    private ComboBox<Tire> tire;

    /**
     * The car of this configuration.
     */
    protected Car car;

    /**
     * Return the {@link CarConfiguration} the user has created.
     *
     * @return The {@link CarConfiguration} the user has created.
     */
    public CarConfiguration getConfiguration() {
        return new CarConfiguration(car, engine.getValue(), driver.getValue(),
            mechanic.getValue(), aerodynamicist.getValue(), strategist.getValue());
    }

    /**
     * Return the {@link CarParameters} the user has created.
     *
     * @return The {@link CarParameters} the user has created.
     */
    public CarParameters getParameters() {
        return new CarParameters(getRiskFor(mechanicRisk), getRiskFor(aeroRisk),
            getRiskFor(strategicRisk), tire.getValue());
    }

    /**
     * This method return the selected risk of a particular specialist.
     * If none is selected it returns a medium risk.
     * 1 = low risk;
     * 2 = med risk;
     * 3 = high risk;
     *
     * @param group The ToggleGroup you want to get the risk of.
     * @return An integer which represents a particular risk.
     */
    private int getRiskFor(ToggleGroup group) {
        if (group.getSelectedToggle() == null) {
            return 2;
        }

        switch (group.getSelectedToggle().toString()) {
            case "L":
                return 1;
            case "M":
                return 2;
            case "H":
                return 3;
            default:
                return 2;
        }
    }

    /**
     * This method is called when the screen is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        Team team = teamService.teamProperty().get();

        initializeStaff(mechanic, Mechanic.class, team);
        initializeStaff(aerodynamicist, Aerodynamicist.class, team);
        initializeStaff(strategist, Strategist.class, team);
        initializeStaff(driver, Driver.class, team);

        initializeInventory(engine, Engine.class, team);
        initializeInventory(tire, Tire.class, team);
    }

    private <T extends Member> void initializeStaff(ComboBox<T> box, Class<T> target, Team team) {
        List<T> items = team.getStaff().stream()
            .filter(target::isInstance)
            .map(target::cast)
            .collect(Collectors.toList());

        box.setItems(FXCollections.observableList(items));
        box.setCellFactory(view -> new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    private <T extends InventoryItem> void initializeInventory(ComboBox<T> box, Class<T> target,
                                                               Team team) {
        List<T> items = team.getInventory().stream()
            .filter(target::isInstance)
            .map(target::cast)
            .collect(Collectors.toList());

        box.setItems(FXCollections.observableList(items));
        box.setCellFactory(view -> new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle(null);
                } else if (item instanceof Tire) {
                    Tire tire = (Tire) item;
                    setText(String.format("%s %s", tire.getBrand(), tire.getType()));
                } else if (item instanceof Engine) {
                    Engine engine = (Engine) item;
                    setText(String.format("%s %s", engine.getBrand(), engine.getName()));
                }
            }
        });
    }
}

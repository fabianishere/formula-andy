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

package nl.tudelft.fa.frontend.javafx.scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import nl.tudelft.fa.frontend.javafx.controller.TeamSettings;
import javafx.stage.Stage;

public class SetupScreenScene extends Scene {

    private Button startRace;
    private ToggleButton mechanical11;
    private ToggleButton mechanical12;
    private ToggleButton mechanical13;
    private ToggleButton mechanical21;
    private ToggleButton mechanical22;
    private ToggleButton mechanical23;
    private ToggleButton aerodynamic11;
    private ToggleButton aerodynamic12;
    private ToggleButton aerodynamic13;
    private ToggleButton aerodynamic21;
    private ToggleButton aerodynamic22;
    private ToggleButton aerodynamic23;
    private ToggleButton strategy11;
    private ToggleButton strategy12;
    private ToggleButton strategy13;
    private ToggleButton strategy21;
    private ToggleButton strategy22;
    private ToggleButton strategy23;
    private ComboBox tireCar1;
    private ComboBox tireCar2;
    private ComboBox engineCar1;
    private ComboBox engineCar2;
    private ComboBox driverCar1;
    private ComboBox driverCar2;
    private ComboBox mechanicCar1;
    private ComboBox mechanicCar2;
    private ComboBox aerodynamicistCar1;
    private ComboBox aerodynamicistCar2;
    private ComboBox strategistCar1;
    private ComboBox strategistCar2;

    /**
     * This file is to make the setup-screen buttons work. It contains:
     * Setup for all toggle buttons to act like radio buttons:
     * using the toggle void
     * Setup for all the dropdown buttons to display an arraylist to choose from:
     * using the dropdown void
     *
     * @param root the parent root
     */
    public SetupScreenScene(Parent root) {
        super(root);
        getInputObjects();
        setToggleButtons();
        setDropBoxes();
        setStartRaceButton();

    }

    private void setStartRaceButton() {
        startRace.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TeamSettings settings = new TeamSettings();
                try {
                    settings.setAero1(getSelectedValueComboBox(aerodynamicistCar1, false));
                    settings.setAero2(getSelectedValueComboBox(aerodynamicistCar2, false));
                    settings.setMechanic1(getSelectedValueComboBox(mechanicCar1, false));
                    settings.setMechanic2(getSelectedValueComboBox(mechanicCar2, false));
                    settings.setStrategist1(getSelectedValueComboBox(strategistCar1, false));
                    settings.setStrategist2(getSelectedValueComboBox(strategistCar2, false));

                    settings.setAeroRisk1(getAeroRiskCar1());
                    settings.setAeroRisk2(getAeroRiskCar2());
                    settings.setMechanicRisk1(getMechanicalRiskCar1());
                    settings.setMechanicRisk2(getMechanicalRiskCar2());
                    settings.setStrategistRisk1(getStratRiskCar1());
                    settings.setStrategistRisk2(getStratRiskCar2());

                    settings.setDriver1(getSelectedValueComboBox(driverCar1, false));
                    settings.setDriver2(getSelectedValueComboBox(driverCar2, false));

                    settings.setEngine1(getSelectedValueComboBox(engineCar1, true));
                    settings.setEngine2(getSelectedValueComboBox(engineCar2, true));

                    settings.setTire1(getSelectedValueComboBox(tireCar1, false));
                    settings.setTire2(getSelectedValueComboBox(tireCar2, false));

                    settings.setRaceable(true);
                }
                catch (Exception e) {

                }
                // client.send(settings);
            }
        });
    }

    private void setDropBoxes() {
        ObservableList<String> tire1 = FXCollections.observableArrayList(
                "Wet",
                "Intermediate",
                "Ultra Soft",
                "Super soft",
                "Soft",
                "Medium",
                "Hard");
        dropdown(tire1, "#tire1");

        ObservableList<String> tire2 = FXCollections.observableArrayList(
                "Wet",
                "Intermediate",
                "Ultra Soft",
                "Super soft",
                "Soft",
                "Medium",
                "Hard");
        dropdown(tire2, "#tire2");

        ObservableList<String> engine1 = FXCollections.observableArrayList(
                "Mercedes ($35M)",
                "Ferrari ($32M)",
                "Renault ($27M)",
                "Honda ($25M)");
        dropdown(engine1, "#engine1");

        ObservableList<String> engine2 = FXCollections.observableArrayList(
                "Mercedes ($35M)",
                "Ferrari ($32M)",
                "Renault ($27M)",
                "Honda ($25M)");
        dropdown(engine2, "#engine2");

        ObservableList<String> driver1 = FXCollections.observableArrayList(
                "Max Verstappen",
                "Pel de Pinda");
        dropdown(driver1, "#driver1");

        ObservableList<String> driver2 = FXCollections.observableArrayList(
                "Max Verstappen",
                "Pel de Pinda");
        dropdown(driver2, "#driver2");

        ObservableList<String> mechanic1 = FXCollections.observableArrayList(
                "Max Verstappen",
                "Pel de Pinda");
        dropdown(mechanic1, "#mechanic1");

        ObservableList<String> mechanic2 = FXCollections.observableArrayList(
                "Max Verstappen",
                "Pel de Pinda");
        dropdown(mechanic2, "#mechanic2");

        ObservableList<String> aerodynamicist1 = FXCollections.observableArrayList(
                "Max Verstappen",
                "Pel de Pinda");
        dropdown(aerodynamicist1, "#aerodynamicist1");

        ObservableList<String> aerodynamicist2 = FXCollections.observableArrayList(
                "Max Verstappen",
                "Pel de Pinda");
        dropdown(aerodynamicist2, "#aerodynamicist2");

        ObservableList<String> strategist1 = FXCollections.observableArrayList(
                "Max Verstappen",
                "Pel de Pinda");
        dropdown(strategist1, "#strategist1");

        ObservableList<String> strategist2 = FXCollections.observableArrayList(
                "Max Verstappen",
                "Pel de Pinda");
        dropdown(strategist2, "#strategist2");
    }

    private void setToggleButtons() {
        mechanical11.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(mechanical11, mechanical12, mechanical13, event);
            }
        });

        mechanical12.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(mechanical12, mechanical13, mechanical11, event);
            }
        });

        mechanical13.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(mechanical13, mechanical11, mechanical12, event);
            }
        });

        mechanical21.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(mechanical21, mechanical22, mechanical23, event);
            }
        });

        mechanical22.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(mechanical22, mechanical23, mechanical21, event);
            }
        });

        mechanical23.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(mechanical23, mechanical21, mechanical22, event);
            }
        });

        aerodynamic11.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(aerodynamic11, aerodynamic12, aerodynamic13, event);
            }
        });

        aerodynamic12.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(aerodynamic12, aerodynamic13, aerodynamic11, event);
            }
        });

        aerodynamic13.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(aerodynamic13, aerodynamic11, aerodynamic12, event);
            }
        });

        aerodynamic21.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(aerodynamic21, aerodynamic22, aerodynamic23, event);
            }
        });

        aerodynamic22.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(aerodynamic22, aerodynamic23, aerodynamic21, event);
            }
        });

        aerodynamic23.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(aerodynamic23, aerodynamic21, aerodynamic22, event);
            }
        });

        strategy11.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(strategy11, strategy12, strategy13, event);
            }
        });

        strategy12.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(strategy12, strategy13, strategy11, event);
            }
        });

        strategy13.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(strategy13, strategy11, strategy12, event);
            }
        });

        strategy21.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(strategy21, strategy22, strategy23, event);
            }
        });

        strategy22.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(strategy22, strategy23, strategy21, event);
            }
        });

        strategy23.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(strategy23, strategy21, strategy22, event);
            }
        });
    }

    private void getInputObjects() {
        startRace = (Button) this.lookup("#startRace");
        mechanical11 = (ToggleButton) this.lookup("#mechanical11");
        mechanical12 = (ToggleButton) this.lookup("#mechanical12");
        mechanical13 = (ToggleButton) this.lookup("#mechanical13");
        mechanical21 = (ToggleButton) this.lookup("#mechanical21");
        mechanical22 = (ToggleButton) this.lookup("#mechanical22");
        mechanical23 = (ToggleButton) this.lookup("#mechanical23");
        aerodynamic11 = (ToggleButton) this.lookup("#aerodynamic11");
        aerodynamic12 = (ToggleButton) this.lookup("#aerodynamic12");
        aerodynamic13 = (ToggleButton) this.lookup("#aerodynamic13");
        aerodynamic21 = (ToggleButton) this.lookup("#aerodynamic21");
        aerodynamic22 = (ToggleButton) this.lookup("#aerodynamic22");
        aerodynamic23 = (ToggleButton) this.lookup("#aerodynamic23");
        strategy11 = (ToggleButton) this.lookup("#strategy11");
        strategy12 = (ToggleButton) this.lookup("#strategy12");
        strategy13 = (ToggleButton) this.lookup("#strategy13");
        strategy21 = (ToggleButton) this.lookup("#strategy21");
        strategy22 = (ToggleButton) this.lookup("#strategy22");
        strategy23 = (ToggleButton) this.lookup("#strategy23");
        tireCar1 = (ComboBox) this.lookup("#tire1");
        tireCar2 = (ComboBox) this.lookup("#tire2");
        engineCar1 = (ComboBox) this.lookup("#engine1");
        engineCar2 = (ComboBox) this.lookup("#engine2");
        driverCar1 = (ComboBox) this.lookup("#driver1");
        driverCar2 = (ComboBox) this.lookup("#driver2");
        mechanicCar1 = (ComboBox) this.lookup("#mechanic1");
        mechanicCar2 = (ComboBox) this.lookup("#mechanic2");
        aerodynamicistCar1 = (ComboBox) this.lookup("#aerodynamicist1");
        aerodynamicistCar2 = (ComboBox) this.lookup("#aerodynamicist2");
        strategistCar1 = (ComboBox) this.lookup("#strategist1");
        strategistCar2 = (ComboBox) this.lookup("#strategist2");
    }

    /**
     * The actual toggle void: toggles the color between the buttons. Selected is red
     *
     * @param t1    the toggle id of the first button
     * @param t2    the toggle id of the second button
     * @param t3    the toggle id of the second button
     * @param event the action event for the toggling
     */
    private void toggle(ToggleButton t1, ToggleButton t2, ToggleButton t3, Event event) {
        t1.setSelected(true);
        t2.setSelected(false);
        t3.setSelected(false);
        t1.setStyle("-fx-background-color: red;");
        t2.setStyle("-fx-background-color: grey;");
        t3.setStyle("-fx-background-color: grey;");
        event.consume();
    }

    /**
     * Sets the items for the dropdown button.
     *
     * @param options the arraylist containing the items you can choose
     * @param id      the id of the combobox button you want to assign the arraylist to
     */
    private void dropdown(ObservableList<String> options, String id) {
        ComboBox<String> comboBox = (ComboBox<String>) this.lookup(id);
        comboBox.setItems(options);
    }

    private void printSettings() {
        System.out.println("Car 1:     Car 2:");
        System.out.println("mechRisk: " + getMechanicalRiskCar1() + " " + getMechanicalRiskCar2());
        System.out.println("aeroRisk: " + getAeroRiskCar1() + " " + getAeroRiskCar2());
        System.out.println("stratRisk: " + getStratRiskCar1() + " " + getStratRiskCar2());
        System.out.println("tire: " + getSelectedValueComboBox(tireCar1, false) + " " + getSelectedValueComboBox(tireCar2, false));
        System.out.println("engine: " + getSelectedValueComboBox(engineCar1, true) + " " + getSelectedValueComboBox(engineCar2, true));
        System.out.println("driver: " + getSelectedValueComboBox(driverCar1, false) + " " + getSelectedValueComboBox(driverCar2, false));
        System.out.println("mechanic: " + getSelectedValueComboBox(mechanicCar1, false) + " " + getSelectedValueComboBox(mechanicCar2, false));
        System.out.println("aerodynamicist: " + getSelectedValueComboBox(aerodynamicistCar1, false) + " " + getSelectedValueComboBox(aerodynamicistCar2, false));
        System.out.println("strategist: " + getSelectedValueComboBox(strategistCar1, false) + " " + getSelectedValueComboBox(strategistCar2, false));
    }

    private int getMechanicalRiskCar1() {
        if (mechanical11.isSelected()) {
            return 1;
        }
        if (mechanical12.isSelected()) {
            return 2;
        }
        return 3;
    }

    private int getMechanicalRiskCar2() {
        if (mechanical21.isSelected()) {
            return 1;
        }
        if (mechanical22.isSelected()) {
            return 2;
        }
        return 3;
    }

    private int getAeroRiskCar1() {
        if (aerodynamic11.isSelected()) {
            return 1;
        }
        if (aerodynamic12.isSelected()) {
            return 2;
        }
        return 3;
    }

    private int getAeroRiskCar2() {
        if (aerodynamic21.isSelected()) {
            return 1;
        }
        if (aerodynamic22.isSelected()) {
            return 2;
        }
        return 3;
    }

    private int getStratRiskCar1() {
        if (strategy11.isSelected()) {
            return 1;
        }
        if (strategy12.isSelected()) {
            return 2;
        }
        return 3;
    }

    private int getStratRiskCar2() {
        if (strategy21.isSelected()) {
            return 1;
        }
        if (strategy22.isSelected()) {
            return 2;
        }
        return 3;
    }

    private String getSelectedValueComboBox(ComboBox comboBox, boolean splitting) {
        if (splitting) {
            return comboBox.getValue().toString().split(" ")[0];
        }
        return comboBox.getValue().toString();

    @FXML
    protected void back(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../teamname-screen.fxml"));

        Scene scene = new Scene(root);
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();

        stage.setTitle("Formula Andy!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();

    }

}


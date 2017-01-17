package nl.tudelft.fa.frontend.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import nl.tudelft.fa.client.lobby.message.TeamConfigurationSubmission;
import nl.tudelft.fa.client.race.CarConfiguration;
import nl.tudelft.fa.client.team.Team;
import nl.tudelft.fa.frontend.javafx.Main;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by laeti on 9-1-2017.
 */
public class SetupScreenController {
    public ToggleGroup toggleGroupMechanic1;
    public ToggleButton mechanic1L;
    public ToggleButton mechanic1M;
    public ToggleButton mechanic1H;

    public ComboBox mechanic1;

    public ToggleGroup toggleGroupAero1;
    public ToggleButton aero1L;
    public ToggleButton aero1M;
    public ToggleButton aero1H;

    public ComboBox aero1;

    public ToggleGroup toggleGroupStrategist1;
    public ToggleButton strategist1L;
    public ToggleButton strategist1M;
    public ToggleButton strategist1H;

    public ComboBox strategist1;

    public ComboBox driver1;
    public ComboBox engine1;
    public ComboBox tire1;

    public ToggleGroup toggleGroupMechanic2;
    public ToggleButton mechanic2L;
    public ToggleButton mechanic2M;
    public ToggleButton mechanic2H;

    public ComboBox mechanic2;

    public ToggleGroup toggleGroupAero2;
    public ToggleButton aero2L;
    public ToggleButton aero2M;
    public ToggleButton aero2H;

    public ComboBox aero2;

    public ToggleGroup toggleGroupStrategist2;
    public ToggleButton strategist2L;
    public ToggleButton strategist2M;
    public ToggleButton strategist2H;

    public ComboBox strategist2;

    public ComboBox driver2;
    public ComboBox engine2;
    public ComboBox tire2;

    private int getSelectedRisk(ToggleGroup group) {
        try {
            if (group.getSelectedToggle().toString().contains("L")) {
                return 1;
            }
            if (group.getSelectedToggle().toString().contains("H")) {
                return 3;
            }
        } catch (Exception e) {
        }
        return 2;
    }

    @FXML
    protected void store(ActionEvent event) throws Exception {
        Main.launchScreen(event, "store-screen.fxml");
    }

    @FXML
    protected void next(ActionEvent event) throws Exception {
        final Set<CarConfiguration> cars = new HashSet<>();
        final TeamConfigurationSubmission submission = new TeamConfigurationSubmission(null, cars);
        cars.add(new CarConfiguration(
                null, null, null, null, null, null
        ));

        // session.tell(submission, ActorRef.noSender());
        // Main.launchScreen(event, "game-screen.fxml");
    }
}

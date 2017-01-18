package nl.tudelft.fa.frontend.javafx.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import nl.tudelft.fa.client.auth.Credentials;
import nl.tudelft.fa.client.lobby.message.TeamConfigurationSubmission;
import nl.tudelft.fa.client.race.CarConfiguration;
import nl.tudelft.fa.client.team.*;
import nl.tudelft.fa.client.team.inventory.Engine;
import nl.tudelft.fa.client.team.inventory.Tire;
import nl.tudelft.fa.frontend.javafx.Main;
import nl.tudelft.fa.frontend.javafx.service.ClientService;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Laetititia Molkenboer & Christian Slothouber
 */
public class SetupScreenController {
    @Inject
    private Team currentTeam;

    @Inject
    private ClientService service;

    public ToggleGroup toggleGroupMechanic1;
    public ToggleButton mechanic1L;
    public ToggleButton mechanic1M;
    public ToggleButton mechanic1H;

    @FXML
    public ComboBox<Mechanic> mechanic1 = new ComboBox<Mechanic>(FXCollections.observableList(getMechanics()));

    public ToggleGroup toggleGroupAero1;
    public ToggleButton aero1L;
    public ToggleButton aero1M;
    public ToggleButton aero1H;

    public ComboBox<Aerodynamicist> aero1;

    public ToggleGroup toggleGroupStrategist1;
    public ToggleButton strategist1L;
    public ToggleButton strategist1M;
    public ToggleButton strategist1H;

    public ComboBox<Strategist> strategist1;

    public ComboBox<Driver> driver1;
    public ComboBox<Engine> engine1;
    public ComboBox<Tire> tire1;

    public ToggleGroup toggleGroupMechanic2;
    public ToggleButton mechanic2L;
    public ToggleButton mechanic2M;
    public ToggleButton mechanic2H;

    public ComboBox<Mechanic> mechanic2;

    public ToggleGroup toggleGroupAero2;
    public ToggleButton aero2L;
    public ToggleButton aero2M;
    public ToggleButton aero2H;

    public ComboBox<Aerodynamicist> aero2;

    public ToggleGroup toggleGroupStrategist2;
    public ToggleButton strategist2L;
    public ToggleButton strategist2M;
    public ToggleButton strategist2H;

    public ComboBox<Strategist> strategist2;
    
    public ComboBox<Driver> driver2;
    public ComboBox<Engine> engine2;
    public ComboBox<Tire> tire2;

    private List<Mechanic> getMechanics() {
        return currentTeam.getStaff()
                .stream()
                .filter(member -> member instanceof Mechanic)
                .map(member -> (Mechanic) member)
                .collect(Collectors.toList());
    }

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
        final List<Team> teams = service.getClient().authorize(new Credentials("", ""))
                .teams()
                .list()
                .toCompletableFuture()
                .get();

        final List<Driver> drivers = teams.get(0).getStaff()
                .stream()
                .filter(member -> member instanceof Driver)
                .map(member -> (Driver) member)
                .collect(Collectors.toList());

        ComboBox<Driver> combo = new ComboBox<>(FXCollections.observableList(drivers));
        // session.tell(submission, ActorRef.noSender());
        // Main.launchScreen(event, "game-screen.fxml");
    }
}

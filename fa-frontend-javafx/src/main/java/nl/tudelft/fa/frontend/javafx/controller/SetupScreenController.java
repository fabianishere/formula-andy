package nl.tudelft.fa.frontend.javafx.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import nl.tudelft.fa.client.auth.Credentials;
import nl.tudelft.fa.client.lobby.message.CarParametersSubmission;
import nl.tudelft.fa.client.lobby.message.TeamConfigurationSubmission;
import nl.tudelft.fa.client.race.CarConfiguration;
import nl.tudelft.fa.client.race.CarParameters;
import nl.tudelft.fa.client.team.*;
import nl.tudelft.fa.client.team.inventory.Car;
import nl.tudelft.fa.client.team.inventory.Engine;
import nl.tudelft.fa.client.team.inventory.InventoryItem;
import nl.tudelft.fa.client.team.inventory.Tire;
import nl.tudelft.fa.frontend.javafx.Main;
import nl.tudelft.fa.frontend.javafx.service.ClientService;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Laetititia Molkenboer & Christian Slothouber
 */
public class SetupScreenController {

    public ToggleGroup toggleGroupMechanic1;
    public ToggleButton mechanic1L;
    public ToggleButton mechanic1M;
    public ToggleButton mechanic1H;

    public ComboBox<Mechanic> mechanic1;

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

    @FXML
    private void initialize() {
        setUpComboBoxes();
    }

    private void setUpComboBoxes() {
        mechanic1.setItems(FXCollections.observableArrayList(getMechanics()));
        mechanic2.setItems(FXCollections.observableArrayList(getMechanics()));
        strategist1.setItems(FXCollections.observableArrayList(getStrategist()));
        strategist2.setItems(FXCollections.observableArrayList(getStrategist()));
        aero1.setItems(FXCollections.observableArrayList(getAero()));
        aero2.setItems(FXCollections.observableArrayList(getAero()));
        driver1.setItems(FXCollections.observableArrayList(getDrivers()));
        driver2.setItems(FXCollections.observableArrayList(getDrivers()));
        engine1.setItems(FXCollections.observableArrayList(getEngines()));
        engine2.setItems(FXCollections.observableArrayList(getEngines()));
        tire1.setItems(FXCollections.observableArrayList(getTireTypes()));
        tire2.setItems(FXCollections.observableArrayList(getTireTypes()));
    }

    private List<Tire> getTireTypes() {
        return Main.getCurrentTeam().getInventory()
                .stream()
                .filter(product -> product instanceof Tire)
                .map(product -> (Tire) product)
                .collect(Collectors.toList());
    }

    private List<Engine> getEngines() {
        return Main.getCurrentTeam().getInventory()
                .stream()
                .filter(product -> product instanceof Engine)
                .map(product -> (Engine) product)
                .collect(Collectors.toList());
    }

    private List<Driver> getDrivers() {
        return Main.getCurrentTeam().getStaff()
                .stream()
                .filter(member -> member instanceof Driver)
                .map(member -> (Driver) member)
                .collect(Collectors.toList());
    }

    private List<Mechanic> getMechanics() {
        return Main.getCurrentTeam().getStaff()
                .stream()
                .filter(member -> member instanceof Mechanic)
                .map(member -> (Mechanic) member)
                .collect(Collectors.toList());
    }

    private List<Strategist> getStrategist() {
        return Main.getCurrentTeam().getStaff()
                .stream()
                .filter(member -> member instanceof Strategist)
                .map(member -> (Strategist) member)
                .collect(Collectors.toList());
    }

    private List<Aerodynamicist> getAero() {
        return Main.getCurrentTeam().getStaff()
                .stream()
                .filter(member -> member instanceof Aerodynamicist)
                .map(member -> (Aerodynamicist) member)
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
        Set<CarConfiguration> cars = new HashSet<>();
        TeamConfigurationSubmission teamConfigurationSubmission =
                new TeamConfigurationSubmission(Main.getCurrentTeam().getOwner(), cars);
        cars.add(setUpCarConfiguration1());
        //cars.add(setUpCarConfiguration2());

        CarParametersSubmission carParametersSubmission1 = new CarParametersSubmission(
                Main.getCurrentTeam().getOwner(),
                getCar(1),
                getCarParameters1());

        //CarParametersSubmission carParametersSubmission2 = new CarParametersSubmission(
//                Main.getCurrentTeam().getOwner(),
//                getCar(2),
//                getCarParameters2());

        System.out.println(getCarParameters1());
        //System.out.println(getCarParameters2());

        //Main.launchScreen(event, "game-screen.fxml");
    }

    private CarParameters getCarParameters1() {
        return new CarParameters(getSelectedRisk(toggleGroupMechanic1),
                getSelectedRisk(toggleGroupAero1),
                getSelectedRisk(toggleGroupStrategist1),
                tire1.getValue());
    }

    private CarParameters getCarParameters2() {
        return new CarParameters(getSelectedRisk(toggleGroupMechanic2),
                getSelectedRisk(toggleGroupAero2),
                getSelectedRisk(toggleGroupStrategist2),
                tire2.getValue());
    }

    private Car getCar(int carnumber) {
        return Main.getCurrentTeam()
                .getInventory()
                .stream()
                .filter(part -> part instanceof Car)
                .map(part -> (Car) part)
                .collect(Collectors.toList())
                .get(carnumber - 1);
    }

    private CarConfiguration setUpCarConfiguration1() {
        return new CarConfiguration(getCar(1),
                engine1.getValue(),
                driver1.getValue(),
                mechanic1.getValue(),
                aero1.getValue(),
                strategist1.getValue());
    }

    private CarConfiguration setUpCarConfiguration2() {
        return new CarConfiguration(getCar(2),
                engine2.getValue(),
                driver2.getValue(),
                mechanic2.getValue(),
                aero2.getValue(),
                strategist2.getValue());
    }
}

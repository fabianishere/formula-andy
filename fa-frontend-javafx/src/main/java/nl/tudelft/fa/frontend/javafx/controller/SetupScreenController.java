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
 *
 * @author Laetititia Molkenboer & Christian Slothouber
 */
public class SetupScreenController {

    /**
     *
     * Togglegroup of low, med and high risk mechanic 1.
     */
    @FXML
    public ToggleGroup toggleGroupMechanic1;

    /**
     *
     * ToggleButton low risk mechanic 1.
     */
    @FXML
    public ToggleButton mechanic1L;

    /**
     *
     * ToggleButton med risk mechanic 1.
     */
    @FXML
    public ToggleButton mechanic1M;

    /**
     *
     * ToggleButton high risk mechanic 1.
     */
    @FXML
    public ToggleButton mechanic1H;

    /**
     *
     * ComboBox to choose mechanic 1.
     */
    @FXML
    public ComboBox<Mechanic> mechanic1;

    /**
     *
     * ToggleGroup of low, med and high risk aero 1.
     */
    @FXML
    public ToggleGroup toggleGroupAero1;

    /**
     *
     * low risk ToggleButton aero 1.
     */
    @FXML
    public ToggleButton aero1L;

    /**
     *
     * med risk ToggleButton aero 1.
     */
    @FXML
    public ToggleButton aero1M;

    /**
     *
     * high risk ToggleButton aero 1.
     */
    @FXML
    public ToggleButton aero1H;

    /**
     *
     * ComboBox to choose aero 1.
     */
    @FXML
    public ComboBox<Aerodynamicist> aero1;

    /**
     *
     * ToggleGroup for grouping low, med and high risk buttons
     * strategist 1.
     */
    @FXML
    public ToggleGroup toggleGroupStrategist1;

    /**
     *
     * low risk strategist 1.
     */
    @FXML
    public ToggleButton strategist1L;

    /**
     *
     * med risk strategist 2.
     */
    @FXML
    public ToggleButton strategist1M;

    /**
     *
     * high risk strategist 2.
     */
    @FXML
    public ToggleButton strategist1H;

    /**
     *
     * ComboBox for choosing strategist 1.
     */
    @FXML
    public ComboBox<Strategist> strategist1;

    /**
     *
     * ComboBox for choosing driver 1.
     */
    @FXML
    public ComboBox<Driver> driver1;

    /**
     *
     * ComboBox for choosing engine 1.
     */
    @FXML
    public ComboBox<Engine> engine1;

    /**
     *
     * ComboBox for choosing tires of car 1.
     */
    @FXML
    public ComboBox<Tire> tire1;

    /**
     *
     * ToggleGroup to group the low, med and high risk buutons
     * of mechanic 2.
     */
    @FXML
    public ToggleGroup toggleGroupMechanic2;

    /**
     *
     * low risk button mechanic 2.
     */
    @FXML
    public ToggleButton mechanic2L;

    /**
     *
     * med risk button mechanic 2.
     */
    @FXML
    public ToggleButton mechanic2M;

    /**
     *
     * high risk button mechanic 2.
     */
    @FXML
    public ToggleButton mechanic2H;

    /**
     *
     * ComboBox for choosing mechanic 2.
     */
    @FXML
    public ComboBox<Mechanic> mechanic2;

    /**
     *
     * ToggleGroup for grouping low, med and high risk
     * buttons of aerodynamicist 2.
     */
    @FXML
    public ToggleGroup toggleGroupAero2;

    /**
     *
     * low risk button aero 2.
     */
    @FXML
    public ToggleButton aero2L;

    /**
     *
     * med risk button aero 2.
     */
    @FXML
    public ToggleButton aero2M;

    /**
     *
     * higgh risk button aero 2.
     */
    @FXML
    public ToggleButton aero2H;

    /**
     *
     * ComboBox for choosing aero 2.
     */
    @FXML
    public ComboBox<Aerodynamicist> aero2;

    /**
     *
     * ToggleGroup for grouping low, med and high
     * risk buttons strategist 2.
     */
    @FXML
    public ToggleGroup toggleGroupStrategist2;

    /**
     *
     * low risk button strategist 2.
     */
    @FXML
    public ToggleButton strategist2L;

    /**
     *
     * med risk button strategist 2.
     */
    @FXML
    public ToggleButton strategist2M;

    /**
     *
     * high risk button strategist 2.
     */
    @FXML
    public ToggleButton strategist2H;

    /**
     *
     * ComboBox for choosing strategist 2.
     */
    @FXML
    public ComboBox<Strategist> strategist2;

    /**
     *
     * ComboBox for choosing driver 2.
     */
    @FXML
    public ComboBox<Driver> driver2;

    /**
     *
     * ComboBox for choosing engine 2.
     */
    @FXML
    public ComboBox<Engine> engine2;

    /**
     *
     * ComboBox for choosing tires for car 2.
     */
    @FXML
    public ComboBox<Tire> tire2;

    /**
     *
     * This method is called when the page is loaded.
     */
    @FXML
    private void initialize() {
        setUpComboBoxes();
    }

    /**
     *
     * This method fills the ComboBoxes with data to choose from.
     */
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

    /**
     *
     * This method returns a list of all available tire types.
     *
     * @return List<Tire> with all tire types.
     */
    private List<Tire> getTireTypes() {
        return Main.getCurrentTeam().getInventory()
                .stream()
                .filter(product -> product instanceof Tire)
                .map(product -> (Tire) product)
                .collect(Collectors.toList());
    }

    /**
     *
     * This method returns a list of all available engines.
     *
     * @return List<Engine> with all engines.
     */
    private List<Engine> getEngines() {
        return Main.getCurrentTeam().getInventory()
                .stream()
                .filter(product -> product instanceof Engine)
                .map(product -> (Engine) product)
                .collect(Collectors.toList());
    }

    /**
     *
     * This method returns a list of all drivers the team has.
     *
     * @return List<Driver> with all drivers.
     */
    private List<Driver> getDrivers() {
        return Main.getCurrentTeam().getStaff()
                .stream()
                .filter(member -> member instanceof Driver)
                .map(member -> (Driver) member)
                .collect(Collectors.toList());
    }

    /**
     *
     * This method returns a list of all mechanics that the team has.
     *
     * @return List<Mechanic> with all mechanics.
     */
    private List<Mechanic> getMechanics() {
        return Main.getCurrentTeam().getStaff()
                .stream()
                .filter(member -> member instanceof Mechanic)
                .map(member -> (Mechanic) member)
                .collect(Collectors.toList());
    }

    /**
     *
     * This method returns all strategist that the team has.
     *
     * @return List<Strategist> with all strategists.
     */
    private List<Strategist> getStrategist() {
        return Main.getCurrentTeam().getStaff()
                .stream()
                .filter(member -> member instanceof Strategist)
                .map(member -> (Strategist) member)
                .collect(Collectors.toList());
    }

    /**
     *
     * This method returns all aerodynamicists of the team.
     *
     * @return List<Aerodynamicist> with all aeros.
     */
    private List<Aerodynamicist> getAero() {
        return Main.getCurrentTeam().getStaff()
                .stream()
                .filter(member -> member instanceof Aerodynamicist)
                .map(member -> (Aerodynamicist) member)
                .collect(Collectors.toList());
    }

    /**
     *
     * This method return the selected risk of a particular specialist.
     * If none is selected it returns a medium risk.
     * 1 = low risk;
     * 2 = med risk;
     * 3 = high risk;
     *
     * @param group The ToggleGroup you want to get the risk of.
     * @return int wich represents a particular risk.
     */
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

    /**
     *
     * This method launches the store screen.
     *
     * @param event The occurring event.
     * @throws Exception The Exception thrown when failing.
     */
    @FXML
    protected void store(ActionEvent event) throws Exception {
        Main.launchScreen(event, "store-screen.fxml");
    }

    /**
     *
     * This method is called when the player wants to continue.
     * It reads out the selected setup and sends the configurations
     * to the server.
     *
     * @param event The occurring event.
     * @throws Exception The Exception that is thrown when failed.
     */
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

    /**
     *
     * This method returns the selected carParameters of car 1.
     *
     * @return CarParameters of car 1.
     */
    private CarParameters getCarParameters1() {
        return new CarParameters(getSelectedRisk(toggleGroupMechanic1),
                getSelectedRisk(toggleGroupAero1),
                getSelectedRisk(toggleGroupStrategist1),
                tire1.getValue());
    }

    /**
     *
     * This method returns the selected carParameters of car 2.
     *
     * @return CarParameters of car 2.
     */
    private CarParameters getCarParameters2() {
        return new CarParameters(getSelectedRisk(toggleGroupMechanic2),
                getSelectedRisk(toggleGroupAero2),
                getSelectedRisk(toggleGroupStrategist2),
                tire2.getValue());
    }

    /**
     *
     * This method returns a car from inventory.
     *
     * @param carnumber The particular car to be returned.
     * @return  a Car
     */
    private Car getCar(int carnumber) {
        return Main.getCurrentTeam()
                .getInventory()
                .stream()
                .filter(part -> part instanceof Car)
                .map(part -> (Car) part)
                .collect(Collectors.toList())
                .get(carnumber - 1);
    }

    /**
     *
     * his method returns the CarCofiguration of car 1.
     *
     * @return CarConfiguration of car 1.
     */
    private CarConfiguration setUpCarConfiguration1() {
        return new CarConfiguration(getCar(1),
                engine1.getValue(),
                driver1.getValue(),
                mechanic1.getValue(),
                aero1.getValue(),
                strategist1.getValue());
    }


    /**
     *
     * This method returns the CarConfiguration of car 2.
     *
     * @return CarConfiguration of car 2.
     */
    private CarConfiguration setUpCarConfiguration2() {
        return new CarConfiguration(getCar(2),
                engine2.getValue(),
                driver2.getValue(),
                mechanic2.getValue(),
                aero2.getValue(),
                strategist2.getValue());
    }
}

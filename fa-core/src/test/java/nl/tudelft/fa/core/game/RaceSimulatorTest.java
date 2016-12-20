package nl.tudelft.fa.core.game;

import nl.tudelft.fa.core.race.CarConfiguration;
import nl.tudelft.fa.core.race.CarParameters;
import nl.tudelft.fa.core.race.Circuit;
import nl.tudelft.fa.core.race.GrandPrix;
import nl.tudelft.fa.core.team.*;
import nl.tudelft.fa.core.team.inventory.Car;
import nl.tudelft.fa.core.team.inventory.Engine;
import nl.tudelft.fa.core.team.inventory.Tire;
import nl.tudelft.fa.core.team.inventory.TireType;
import org.junit.*;
import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RaceSimulatorTest {

    int testAmount;
    double delta;

    Circuit circuit;
    GrandPrix gp;

    RaceSimulator rs;

    int mechanicalRisk;
    int aerodynamicRisk;
    int strategicRisk;
    Tire tire;

    CarParameters parameters;
    CarParameters parameters2;

    Car car;

    Driver driver;
    Driver driver2;

    Engine engine;
    Mechanic mechanic;
    Aerodynamicist aerodynamicist;
    Strategist strategist;

    CarConfiguration configuration;
    CarConfiguration configuration2;

    CarSimulator cs;
    CarSimulator cs2;

    List<CarSimulator> carSimulatorList;

    public void setUpCarSimulators() {
        mechanicalRisk = 2;
        aerodynamicRisk = 3;
        strategicRisk = 3;
        tire = new Tire(UUID.randomUUID(), "Pirelli", TireType.SUPER_SOFT, 7, 1);

        parameters = new CarParameters(mechanicalRisk, aerodynamicRisk, strategicRisk, tire);
        parameters2 = new CarParameters(mechanicalRisk, aerodynamicRisk, strategicRisk, tire);

        car = new Car(UUID.randomUUID());

        driver = new Driver(UUID.randomUUID(), "Henry", 3, 80, 90, 70);
        driver2 = new Driver(UUID.randomUUID(), "Max", 3, 70, 80, 30);

        engine = new Engine(UUID.randomUUID(), "Mercedes", "F1 W05 Hybrid", 100, 80, 85);
        mechanic = new Mechanic(UUID.randomUUID(), "Harry", 35, 80);
        aerodynamicist = new Aerodynamicist(UUID.randomUUID(), "Fred", 100, 80);
        strategist = new Strategist(UUID.randomUUID(), "Louis", 100, 80);

        configuration = new CarConfiguration(car, engine, driver, mechanic, aerodynamicist, strategist);
        configuration2 = new CarConfiguration(car, engine, driver2, mechanic, aerodynamicist, strategist);

        cs = new CarSimulator(configuration, parameters);
        cs2 = new CarSimulator(configuration2, parameters2);

        carSimulatorList = new ArrayList<CarSimulator>();

        carSimulatorList.add(cs);
        carSimulatorList.add(cs2);
    }

    public void setUpGrandPrix(int rainchance, int laps) {
        circuit = new Circuit(UUID.randomUUID(), "circuitName", "county", 5000);
        gp = new GrandPrix(UUID.randomUUID(), circuit, Instant.now(), laps, rainchance);
    }

    @Test
    public void setUp() {
        setUpCarSimulators();
        setUpGrandPrix(100, 100);

        rs = new RaceSimulator(carSimulatorList, gp);

    }

    @Test
    public void getGrandPrix() {
        setUp();
        assertEquals(gp, rs.getGrandPrix());
    }

    @Test
    public void getCarSimulators() {
        setUp();
        assertEquals(carSimulatorList, rs.getCarSimulators());
    }
}

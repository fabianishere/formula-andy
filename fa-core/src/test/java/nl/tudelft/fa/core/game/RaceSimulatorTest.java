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

    double delta;
    Circuit circuit;
    GrandPrix gp;
    RaceSimulator rs;
    int mechanicalRisk;
    int aerodynamicRisk;
    int strategicRisk;
    Tire tire;
    CarConfiguration configuration;
    CarParameters parameters;
    Car car;
    Driver driver;
    Engine engine;
    Mechanic mechanic;
    Aerodynamicist aerodynamicist;
    Strategist strategist;
    CarSimulator cs;
    List<CarSimulator> carSimulatorList;

    public void setUp() {
        delta = 0.00001;
        mechanicalRisk = 2;
        aerodynamicRisk = 3;
        strategicRisk = 3;
        tire = new Tire(UUID.randomUUID(), "Pirelli", TireType.SUPER_SOFT, 7, 1);
        parameters = new CarParameters(mechanicalRisk, aerodynamicRisk, strategicRisk, tire);
        car = new Car(UUID.randomUUID());
        driver = new Driver(UUID.randomUUID(), "Henry", 3, 80, 90, 70);
        engine = new Engine(UUID.randomUUID(), "Mercedes", "F1 W05 Hybrid", 100, 80, 85);
        mechanic = new Mechanic(UUID.randomUUID(), "Harry", 35, 80);
        aerodynamicist = new Aerodynamicist(UUID.randomUUID(), "Fred", 100, 80);
        strategist = new Strategist(UUID.randomUUID(), "Louis", 100, 80);
        configuration = new CarConfiguration(car, engine, driver, mechanic, aerodynamicist, strategist);
        cs = new CarSimulator(configuration, parameters);
        carSimulatorList = new ArrayList<CarSimulator>();
        carSimulatorList.add(cs);
        circuit = new Circuit(UUID.randomUUID(), "circuitName", "county", 5000);
        gp = new GrandPrix(UUID.randomUUID(), circuit, Instant.now(), 10000, 10000);
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

    @Test
    public void getNextCycle() {
        setUp();
        cs.getCarParameters().setCrashed(true);
        rs.getNextRaceCycle(carSimulatorList, rs.getRaining());
        assertTrue(cs.getCarParameters().getTraveledDistance() == 0);
    }

    @Test
    public void getNextCycle2() {
        setUp();
        rs.getNextRaceCycle(carSimulatorList, rs.getRaining());
        assertFalse(cs.getCarParameters().getTraveledDistance() == 0);
    }

    @Test
    public void getNextCycle3() {
        setUp();
        rs.getNextRaceCycle(carSimulatorList, rs.getRaining());
        if (cs.getCarParameters().getCrashed()) {
            assertTrue(cs.getCarParameters().getTraveledDistance() == 0.0);
        }
        assertTrue(cs.getCarParameters().getTraveledDistance() != 0.0);
    }

    @Test
    public void getNextCycle4() {
        setUp();
        double distance = 0.0;
        while(!cs.getCarParameters().getCrashed()) {
            rs.getNextRaceCycle(carSimulatorList, rs.getRaining());
            distance = cs.getCarParameters().getTraveledDistance();
        }
        rs.getNextRaceCycle(carSimulatorList, rs.getRaining());
        assertEquals(distance, cs.getCarParameters().getTraveledDistance(), delta);
    }
}

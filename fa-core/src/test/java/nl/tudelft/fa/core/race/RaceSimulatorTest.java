package nl.tudelft.fa.core.race;

import nl.tudelft.fa.core.team.Aerodynamicist;
import nl.tudelft.fa.core.team.Driver;
import nl.tudelft.fa.core.team.Mechanic;
import nl.tudelft.fa.core.team.Strategist;
import nl.tudelft.fa.core.team.inventory.Car;
import nl.tudelft.fa.core.team.inventory.Engine;
import nl.tudelft.fa.core.team.inventory.Tire;
import nl.tudelft.fa.core.team.inventory.TireType;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
    Car car2;
    Driver driver;
    Engine engine;
    Mechanic mechanic;
    Aerodynamicist aerodynamicist;
    Strategist strategist;
    CarSimulator cs;
    List<CarSimulator> carSimulatorList;

    @Before
    public void setUp() {
        delta = 0.00001;
        mechanicalRisk = 2;
        aerodynamicRisk = 3;
        strategicRisk = 3;
        tire = new Tire(UUID.randomUUID(), "Pirelli", TireType.SUPER_SOFT, 7, 1);
        parameters = new CarParameters(mechanicalRisk, aerodynamicRisk, strategicRisk, tire);
        car = new Car(UUID.randomUUID());
        car2 = new Car(UUID.randomUUID());
        driver = new Driver(UUID.randomUUID(), "Henry", 3, 80, 90, 70);
        engine = new Engine(UUID.randomUUID(), "Mercedes", "F1 W05 Hybrid", 100, 80, 85);
        mechanic = new Mechanic(UUID.randomUUID(), "Harry", 35, 80);
        aerodynamicist = new Aerodynamicist(UUID.randomUUID(), "Fred", 100, 80);
        strategist = new Strategist(UUID.randomUUID(), "Louis", 100, 80);
        configuration = new CarConfiguration(car, engine, driver, mechanic, aerodynamicist, strategist);
        cs = new CarSimulator(configuration, parameters);
        carSimulatorList = new ArrayList<CarSimulator>();
        carSimulatorList.add(cs);
        carSimulatorList.add(new CarSimulator(new CarConfiguration(car2, engine, driver, mechanic, aerodynamicist, strategist), parameters));
        circuit = new Circuit(UUID.randomUUID(), "circuitName", "county", 5000);
        gp = new GrandPrix(UUID.randomUUID(), circuit, Instant.now(), 10000, 10000);
        rs = new RaceSimulator(carSimulatorList, gp);
    }

    @Test
    public void getGrandPrix() {
        assertEquals(gp, rs.getGrandPrix());
    }

    @Test
    public void getNextCycle() {
        List<CarSimulationResult> results = new ArrayList<>();
        results.add(new CarSimulationResult(car,0, true, false));
        results = rs.next(new RaceSimulationResult(results, false)).getResults();
        assertEquals(0, results.get(0).getDistanceTraveled(), 0.01);
    }

    @Test
    public void getNextCycle2() {
        List<CarSimulationResult> results = new ArrayList<>();
        results.add(new CarSimulationResult(car, 0, false, false));
        results = rs.next(new RaceSimulationResult(results, false)).getResults();
        assertNotEquals(0, results.get(0).getDistanceTraveled(), 0.01);
    }

    @Test
    public void getNextCycle3() {
        List<CarSimulationResult> results = new ArrayList<>();
        results.add(new CarSimulationResult(car, 0, false, false));
        results = rs.next(new RaceSimulationResult(results, false)).getResults();

        if (results.get(0).hasCrashed()) {
            assertEquals(0, results.get(0).getDistanceTraveled(), 0.01);
            return;
        }
        assertNotEquals(0, results.get(0).getDistanceTraveled(), 0.01);
    }

    @Test
    public void getNextCycle4() {
        double distance = 0.0;
        List<CarSimulationResult> initial = new ArrayList<>();
        initial.add(new CarSimulationResult(car, 0, false, false));
        RaceSimulationResult result = new RaceSimulationResult(initial, false);

        while(!result.isFinished()) {
            distance = result.getResults().get(0).getDistanceTraveled();
            result = rs.next(result);
        }
        assertEquals(distance, result.getResults().get(0).getDistanceTraveled(), 0.01);
    }

    @Test
    public void finishDoNotChangePlacing() {
        List<CarSimulationResult> results = new ArrayList<>();
        results.add(new CarSimulationResult(car, 5, false, true));
        results.add(new CarSimulationResult(car2, 6, false, false));

        RaceSimulationResult result = new RaceSimulationResult(results, false);

        while(!result.isFinished()) {
            result = rs.next(new RaceSimulationResult(results, false));
        }

        assertEquals(result.getResults().get(0).getCar(), results.get(0).getCar());
    }

    @Test
    public void finishDoNotMove() {
        List<CarSimulationResult> results = new ArrayList<>();
        results.add(new CarSimulationResult(car, 5, false, true));
        results.add(new CarSimulationResult(car2, 6, false, false));

        RaceSimulationResult result = new RaceSimulationResult(results, false);

        while(!result.isFinished()) {
            result = rs.next(new RaceSimulationResult(results, false));
        }

        assertEquals(result.getResults().get(0).getDistanceTraveled(), results.get(0).getDistanceTraveled(), 0.01);
    }
}

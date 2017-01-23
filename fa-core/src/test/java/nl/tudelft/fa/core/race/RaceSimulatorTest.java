package nl.tudelft.fa.core.race;

import nl.tudelft.fa.core.team.*;
import nl.tudelft.fa.core.team.inventory.Car;
import nl.tudelft.fa.core.team.inventory.Engine;
import nl.tudelft.fa.core.team.inventory.Tire;
import nl.tudelft.fa.core.team.inventory.TireType;
import org.junit.*;
import static org.junit.Assert.*;

import java.time.Instant;
import java.util.*;

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

    @Before
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
        assertEquals(gp, rs.getGrandPrix());
    }

    @Test
    public void getNextCycle() {
        Map<Car, CarSimulationResult> results = new HashMap<>();
        results.put(car, new CarSimulationResult(0, true));
        results = rs.next(new RaceSimulationResult(results, false)).getCars();
        assertEquals(0, results.get(car).getDistanceTraveled(), 0.01);
    }

    @Test
    public void getNextCycle2() {
        Map<Car, CarSimulationResult> results = new HashMap<>();
        results.put(car, new CarSimulationResult(0, false));
        results = rs.next(new RaceSimulationResult(results, false)).getCars();
        assertNotEquals(0, results.get(car).getDistanceTraveled(), 0.01);
    }

    @Test
    public void getNextCycle3() {
        Map<Car, CarSimulationResult> results = new HashMap<>();
        results.put(car, new CarSimulationResult(0, false));
        results = rs.next(new RaceSimulationResult(results, false)).getCars();

        if (results.get(car).hasCrashed()) {
            assertEquals(0, results.get(car).getDistanceTraveled(), 0.01);
            return;
        }
        assertNotEquals(0, results.get(car).getDistanceTraveled(), 0.01);
    }

    @Test
    public void getNextCycle4() {
        double distance = 0.0;
        Map<Car, CarSimulationResult> results = new HashMap<>();
        results.put(car, new CarSimulationResult(0, false));

        while(!results.get(car).hasCrashed()) {
            distance = results.get(car).getDistanceTraveled();
            results = rs.next(new RaceSimulationResult(results, false)).getCars();
        }
        assertEquals(distance, results.get(car).getDistanceTraveled(), 0.01);
    }
}

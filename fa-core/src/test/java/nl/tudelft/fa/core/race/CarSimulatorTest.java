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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.*;

public class CarSimulatorTest {

    int testAmount;
    double delta;

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

    Map<Car, CarSimulationResult> results;

    @Before
    public void setUp() {
        delta = 0.00001;
        testAmount = 1000000;

        mechanicalRisk = 2;
        aerodynamicRisk = 3;
        strategicRisk = 3;
        tire = new Tire(UUID.randomUUID(), "Pirelli", TireType.SUPER_SOFT, 7, 1);

        parameters = new CarParameters(mechanicalRisk, aerodynamicRisk, strategicRisk, tire);
        parameters2 = new CarParameters(mechanicalRisk, aerodynamicRisk, strategicRisk, tire);

        car = new Car(UUID.randomUUID());

        driver = new Driver(UUID.randomUUID(), "Henry",  3, 80, 90, 70);
        driver2 = new Driver(UUID.randomUUID(), "Max", 3, 70, 80, 30);

        engine = new Engine(UUID.randomUUID(), "Mercedes", "F1 W05 Hybrid", 100, 80, 85);
        mechanic = new Mechanic(UUID.randomUUID(), "Harry", 35, 80);
        aerodynamicist = new Aerodynamicist(UUID.randomUUID(), "Fred", 100, 80);
        strategist = new Strategist(UUID.randomUUID(), "Louis", 100, 80);

        configuration = new CarConfiguration(car, engine, driver, mechanic, aerodynamicist, strategist);
        configuration2 = new CarConfiguration(car, engine, driver2, mechanic, aerodynamicist, strategist);

        cs = new CarSimulator(configuration, parameters);
        cs2 = new CarSimulator(configuration2, parameters2);

        results = new HashMap<>();
        results.put(car, new CarSimulationResult(car, 0, false));
        Car car2 = new Car(UUID.randomUUID());
        results.put(car2, new CarSimulationResult(car2, 0, false));
    }

    @Test
    public void getCarConfiguration() {
        assertEquals(configuration, cs.getConfiguration());
    }

    @Test
    public void getCarParameters() {
        assertEquals(parameters, cs.getParameters());
    }

    @Test
    public void TestCloseDriver() {
        assertTrue(cs.isNearby(results.get(car), results, 50));
    }

    @Test
    public void TestCloseDriver2() {
        results = new HashMap<>();
        results.put(car, new CarSimulationResult(car, 0, false));
        Car car2 = new Car(UUID.randomUUID());
        results.put(car2, new CarSimulationResult(car2, 200, false));

        assertFalse(cs.isNearby(results.get(car), results, 50));
    }

    @Test
    public void GetMaxDistance() {
        setUp();
        double temp = 0;
        Random random = new Random(1);
        for (int i = 0; i < 100000; i++) {
            temp += cs.calculateDelta(random);
        }
        assertEquals(718.6285499929213, temp/100000, delta);
    }

    @Test
    public void getCrashedThisCycle() {
        int temp = 0;
        Random random = new Random(1);
        for (int i = 0; i < testAmount; i++) {
            if (cs.hasCrashed(true, cs.isNearby(results.get(car), results, 50), random)) {
                temp++;
            }
        }
        System.out.println("rain and close:" + temp + " / " + testAmount);
        assertTrue(temp > 0 && temp < testAmount);
    }

    @Test
    public void getCrashedThisCycle2() {
        int temp = 0;
        Random random = new Random(1);
        for (int i = 0; i < testAmount; i++) {
            if (cs.hasCrashed(false, cs.isNearby(results.get(car), results, 50), random)) {
                temp++;
            }
        }
        System.out.println("no rain and close:" + temp + " / " + testAmount);
        assertTrue(temp > 0 && temp < testAmount);
    }

    @Test
    public void getCrashedThisCycle3() {
        int temp = 0;
        Random random = new Random(1);
        setUp();
        for (int i = 0; i < testAmount; i++) {
            if (cs.hasCrashed(true, cs.isNearby(results.get(car), results, 50), random)) {
                temp++;
            }
        }
        System.out.println("rain and not close:" + temp + " / " + testAmount);
        assertTrue(temp > 0 && temp < testAmount);
    }

    @Test
    public void getCrashedThisCycle4() {
        int temp = 0;
        Random random = new Random(2);
        setUp();
        for (int i = 0; i < testAmount; i++) {
            if (cs.hasCrashed(false, cs.isNearby(results.get(car), results, 50), random)) {
                temp++;
            }
        }
        System.out.println("no rain and no close:" + temp + " / " + testAmount);
        assertTrue(temp > 0 && temp < testAmount);
    }
}

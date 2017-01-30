package nl.tudelft.fa.core.race;

import nl.tudelft.fa.core.team.Aerodynamicist;
import nl.tudelft.fa.core.team.Driver;
import nl.tudelft.fa.core.team.Mechanic;
import nl.tudelft.fa.core.team.Strategist;
import nl.tudelft.fa.core.team.inventory.Car;
import nl.tudelft.fa.core.team.inventory.Engine;
import nl.tudelft.fa.core.team.inventory.Tire;
import nl.tudelft.fa.core.team.inventory.TireType;

import java.time.Instant;
import java.util.*;

public class RaceDemo {

    static Circuit circuit;
    static GrandPrix gp;

    static RaceSimulator rs;

    static int mechanicalRisk;
    static int aerodynamicRisk;
    static int strategicRisk;
    static Tire tire;

    static CarParameters parameters1;
    static CarParameters parameters2;
    static CarParameters parameters3;
    static CarParameters parameters4;
    static CarParameters parameters5;
    static CarParameters parameters6;
    static CarParameters parameters7;
    static CarParameters parameters8;
    static CarParameters parameters9;

    static Car car;

    static Driver driver1;
    static Driver driver2;
    static Driver driver3;
    static Driver driver4;
    static Driver driver5;
    static Driver driver6;
    static Driver driver7;
    static Driver driver8;
    static Driver driver9;

    static Engine engine;
    static Mechanic mechanic;
    static Aerodynamicist aerodynamicist;
    static Strategist strategist;

    static CarConfiguration configuration1;
    static CarConfiguration configuration2;
    static CarConfiguration configuration3;
    static CarConfiguration configuration4;
    static CarConfiguration configuration5;
    static CarConfiguration configuration6;
    static CarConfiguration configuration7;
    static CarConfiguration configuration8;
    static CarConfiguration configuration9;

    static CarSimulator cs1;
    static CarSimulator cs2;
    static CarSimulator cs3;
    static CarSimulator cs4;
    static CarSimulator cs5;
    static CarSimulator cs6;
    static CarSimulator cs7;
    static CarSimulator cs8;
    static CarSimulator cs9;

    static Map<Car, CarSimulator> simulators;
    static List<CarSimulationResult> results;

    public static void main(String[] args) {
        setUpGrandPrix();
        setUpCarSimulators();

        rs = new RaceSimulator(simulators.values(), gp);
        final Iterator<RaceSimulationResult> iterator = rs.iterator();

        boolean racing = true;
        int cycles = 0;
        while(racing) {
            results = iterator.next().getResults();
            racing = false;
            for (CarSimulationResult result : results) {
                if (!result.hasCrashed()) {
                    racing = true;
                }
                if (result.getDistanceTraveled() > gp.getLaps() * circuit.getLength()) {
                    racing = false;
                }

            }
            cycles++;

        }
        System.out.println("Cycle: " + cycles + "\n");
        System.out.println("Raining: " + rs.isRaining());
        System.out.println("Laps: " + gp.getLaps() + " Length lap: " + gp.getCircuit().getLength() + "\n");

        results.forEach(result -> {
            System.out.println(simulators.get(result.getConfiguration().getCar()).getConfiguration().getDriver().getName() + ": " + result.getDistanceTraveled() + "  " + result.hasCrashed());
        });
    }

    public static void setUpGrandPrix() {
        circuit = new Circuit(UUID.randomUUID(), "name", "country", 7000); /// delete country
        gp = new GrandPrix(UUID.randomUUID(), circuit, Instant.now(), 65, 50); //
    }

    public static void setUpCarSimulators() {
        mechanicalRisk = 2;
        aerodynamicRisk = 3;
        strategicRisk = 1;
        tire = new Tire(UUID.randomUUID(), "Pirelli", TireType.SUPER_SOFT, 7, 1);

        parameters1 = new CarParameters(mechanicalRisk, aerodynamicRisk, strategicRisk, tire);
        parameters2 = new CarParameters(mechanicalRisk, aerodynamicRisk, strategicRisk, tire);
        parameters3 = new CarParameters(mechanicalRisk, aerodynamicRisk, strategicRisk, tire);
        parameters4 = new CarParameters(mechanicalRisk, aerodynamicRisk, strategicRisk, tire);
        parameters5 = new CarParameters(mechanicalRisk, aerodynamicRisk, strategicRisk, tire);
        parameters6 = new CarParameters(mechanicalRisk, aerodynamicRisk, strategicRisk, tire);
        parameters7 = new CarParameters(mechanicalRisk, aerodynamicRisk, strategicRisk, tire);
        parameters8 = new CarParameters(mechanicalRisk, aerodynamicRisk, strategicRisk, tire);
        parameters9 = new CarParameters(mechanicalRisk, aerodynamicRisk, strategicRisk, tire);

        car = new Car(UUID.randomUUID());

        driver1 = new Driver(UUID.randomUUID(), "Racer 1", 3, 80, 90, 70);
        driver2 = new Driver(UUID.randomUUID(), "Racer 2", 3, 80, 90, 70);
        driver3 = new Driver(UUID.randomUUID(), "Racer 3", 3, 80, 90, 70);
        driver4 = new Driver(UUID.randomUUID(), "Racer 4", 3, 80, 90, 70);
        driver5 = new Driver(UUID.randomUUID(), "Racer 5", 3, 80, 90, 70);
        driver6 = new Driver(UUID.randomUUID(), "Racer 6", 3, 80, 90, 70);
        driver7 = new Driver(UUID.randomUUID(), "Racer 7", 3, 80, 90, 70);
        driver8 = new Driver(UUID.randomUUID(), "Sonic", 3, 100, 100, 100);
        driver9 = new Driver(UUID.randomUUID(), "Max", 3, 90, 90, 90);

        engine = new Engine(UUID.randomUUID(), "Mercedes", "F1 W05 Hybrid", 100, 80, 85);
        mechanic = new Mechanic(UUID.randomUUID(), "Harry", 35, 80);
        aerodynamicist = new Aerodynamicist(UUID.randomUUID(), "Fred", 100, 80);
        strategist = new Strategist(UUID.randomUUID(), "Louis", 100, 80);

        configuration1 = new CarConfiguration(new Car(UUID.randomUUID()), engine, driver1, mechanic, aerodynamicist, strategist);
        configuration2 = new CarConfiguration(new Car(UUID.randomUUID()), engine, driver2, mechanic, aerodynamicist, strategist);
        configuration3 = new CarConfiguration(new Car(UUID.randomUUID()), engine, driver3, mechanic, aerodynamicist, strategist);
        configuration4 = new CarConfiguration(new Car(UUID.randomUUID()), engine, driver4, mechanic, aerodynamicist, strategist);
        configuration5 = new CarConfiguration(new Car(UUID.randomUUID()), engine, driver5, mechanic, aerodynamicist, strategist);
        configuration6 = new CarConfiguration(new Car(UUID.randomUUID()), engine, driver6, mechanic, aerodynamicist, strategist);
        configuration7 = new CarConfiguration(new Car(UUID.randomUUID()), engine, driver7, mechanic, aerodynamicist, strategist);
        configuration8 = new CarConfiguration(new Car(UUID.randomUUID()), engine, driver8, mechanic, aerodynamicist, strategist);
        configuration9 = new CarConfiguration(car, engine, driver9, mechanic, aerodynamicist, strategist);

        cs1 = new CarSimulator(configuration1, parameters1);
        cs2 = new CarSimulator(configuration2, parameters2);
        cs3 = new CarSimulator(configuration3, parameters3);
        cs4 = new CarSimulator(configuration4, parameters4);
        cs5 = new CarSimulator(configuration5, parameters5);
        cs6 = new CarSimulator(configuration6, parameters6);
        cs7 = new CarSimulator(configuration7, parameters7);
        cs8 = new CarSimulator(configuration8, parameters8);
        cs9 = new CarSimulator(configuration9, parameters9);

        simulators = new HashMap<>();

        simulators.put(cs1.getConfiguration().getCar(), cs1);
        simulators.put(cs2.getConfiguration().getCar(), cs2);
        simulators.put(cs3.getConfiguration().getCar(), cs3);
        simulators.put(cs4.getConfiguration().getCar(), cs4);
        simulators.put(cs5.getConfiguration().getCar(), cs5);
        simulators.put(cs6.getConfiguration().getCar(), cs6);
        simulators.put(cs7.getConfiguration().getCar(), cs7);
        simulators.put(cs8.getConfiguration().getCar(), cs8);
        simulators.put(cs9.getConfiguration().getCar(), cs9);

    }
}

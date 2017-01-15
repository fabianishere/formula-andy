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

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    static List<CarSimulator> carSimulatorList;

    public static void main(String[] args) {
        setUpGrandPrix();
        setUpCarSimulators();

        rs = new RaceSimulator(carSimulatorList, gp);

        boolean racing = true;
        int cycles = 0;
        while(racing) {
            carSimulatorList = RaceSimulator.getNextRaceCycle(carSimulatorList, rs.getRaining());
            racing = false;
            for (CarSimulator cs : carSimulatorList) {
                if (!cs.getCarParameters().getCrashed()) {
                    racing = true;
                }
                if (cs.getCarParameters().getTraveledDistance() > gp.getLaps() * circuit.getLength()) {
                    racing = false;
                }

            }
            cycles++;

        }
        System.out.println("Cycle: " + cycles + "\n");
        System.out.println("Raining: " + rs.getRaining());
        System.out.println("Laps: " + gp.getLaps() + " Length lap: " + gp.getCircuit().getLength() + "\n");

        for (CarSimulator cs : carSimulatorList) {
            System.out.println(cs.getCarConfiguration().getDriver().getName() + ": " + cs.getCarParameters().getTraveledDistance() + "  " + cs.getCarParameters().getCrashed());
        }
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

        configuration1 = new CarConfiguration(car, engine, driver1, mechanic, aerodynamicist, strategist);
        configuration2 = new CarConfiguration(car, engine, driver2, mechanic, aerodynamicist, strategist);
        configuration3 = new CarConfiguration(car, engine, driver3, mechanic, aerodynamicist, strategist);
        configuration4 = new CarConfiguration(car, engine, driver4, mechanic, aerodynamicist, strategist);
        configuration5 = new CarConfiguration(car, engine, driver5, mechanic, aerodynamicist, strategist);
        configuration6 = new CarConfiguration(car, engine, driver6, mechanic, aerodynamicist, strategist);
        configuration7 = new CarConfiguration(car, engine, driver7, mechanic, aerodynamicist, strategist);
        configuration8 = new CarConfiguration(car, engine, driver8, mechanic, aerodynamicist, strategist);
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

        carSimulatorList = new ArrayList<CarSimulator>();

        carSimulatorList.add(cs1);
        carSimulatorList.add(cs2);
        carSimulatorList.add(cs3);
        carSimulatorList.add(cs4);
        carSimulatorList.add(cs5);
        carSimulatorList.add(cs6);
        carSimulatorList.add(cs7);
        carSimulatorList.add(cs8);
        carSimulatorList.add(cs9);

    }
}

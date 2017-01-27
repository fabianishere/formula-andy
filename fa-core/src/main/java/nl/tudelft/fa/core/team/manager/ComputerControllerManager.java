package nl.tudelft.fa.core.team.manager;

import nl.tudelft.fa.core.race.CarConfiguration;
import nl.tudelft.fa.core.race.CarParameters;
import nl.tudelft.fa.core.team.Aerodynamicist;
import nl.tudelft.fa.core.team.Driver;
import nl.tudelft.fa.core.team.Mechanic;
import nl.tudelft.fa.core.team.Strategist;
import nl.tudelft.fa.core.team.inventory.Car;
import nl.tudelft.fa.core.team.inventory.Engine;
import nl.tudelft.fa.core.team.inventory.Tire;
import nl.tudelft.fa.core.team.inventory.TireType;

import java.util.Random;
import java.util.UUID;

/**
 * This class is for the AI of the computer.
 * Now it will choose a random car with its engine and risk.
 *
 * @author Nikki Bouman
 * @author Fabian Mastenbroek
 */
public class ComputerControllerManager extends Manager {
    /**
     * The {@link Random} instance to use.
     */
    private Random random;

    /**
     * Construct a {@link ComputerControllerManager} instance.
     *
     * @param random The {@link Random} instance to use.
     */
    public ComputerControllerManager(Random random) {
        this.random = random;
    }

    /**
     * Construct a {@link ComputerControllerManager} instance.
     */
    public ComputerControllerManager() {
        this(new Random());
    }

    /**
     * Computer chooses a random tire, depending on if it's raining or not.
     *
     * @param raining A flag to indicate whether it is raining.
     * @return The tire that has been chosen.
     */
    private Tire chooseTire(boolean raining) {
        int type = raining ? random.nextInt(1) + 1 : random.nextInt(4) + 3;

        switch (type) {
            case 1:
                return new Tire(UUID.randomUUID(), "Pirelli", TireType.WET, 1, 7);
            case 2:
                return new Tire(UUID.randomUUID(), "Pirelli", TireType.INTERMEDIATE, 2, 6);
            case 3:
                return new Tire(UUID.randomUUID(), "Pirelli", TireType.ULTRA_SOFT, 3, 5);
            case 4:
                return new Tire(UUID.randomUUID(), "Pirelli", TireType.SUPER_SOFT, 4, 4);
            case 5:
                return new Tire(UUID.randomUUID(), "Pirelli", TireType.SOFT, 5, 3);
            case 6:
                return new Tire(UUID.randomUUID(), "Pirelli", TireType.MEDIUM, 6, 2);
            case 7:
                return new Tire(UUID.randomUUID(), "Pirelli", TireType.HARD, 7, 1);
            default:
                return new Tire(UUID.randomUUID(), "Pirelli", TireType.INTERMEDIATE, 2, 6);
        }
    }

    /**
     * Computer chooses a random engine to work with.
     *
     * @return The engine that has been chosen.
     */
    private Engine chooseEngine() {
        int type = random.nextInt(3) + 1;
        switch (type) {
            case 1:
                return new Engine(UUID.randomUUID(), "Mercedes", "", 100, 100, 80);
            case 2:
                return new Engine(UUID.randomUUID(), "Ferrari", "", 95, 95, 70);
            case 3:
                return new Engine(UUID.randomUUID(), "Renault", "", 90, 90, 60);
            case 4:
                return new Engine(UUID.randomUUID(), "Honda", "", 85, 85, 50);
            default:
                return new Engine(UUID.randomUUID(), "Renault", "", 90, 90, 60);
        }
    }


    /**
     * Method to choose random risks and create a new CarParameters with the risks and the chosen
     * tire in it.
     *
     * @param raining A flag to indicate whether it is raining.
     * @return The {@link CarParameters} to use.
     */
    public CarParameters createParameters(boolean raining) {
        int mechanic = random.nextInt(2) + 1;
        int aerodynamic = random.nextInt(2) + 1;
        int strategic = random.nextInt(2) + 1;
        return new CarParameters(mechanic, aerodynamic, strategic, chooseTire(raining));
    }

    /**
     * Creates the configuration for the car.
     *
     * @return returns the created configuration.
     */
    public CarConfiguration createConfiguration() {
        Random random = new Random();
        return new CarConfiguration(new Car(UUID.randomUUID()),
            chooseEngine(),
            new Driver(UUID.randomUUID(),
                    "AI Driver",
                    0,
                    random.nextInt(20) + 70,
                    random.nextInt(20) + 70,
                    random.nextInt(20) + 70),
            new Mechanic(UUID.randomUUID(),
                    "AI Mechanic",
                    0,
                    random.nextInt(20) + 70),
            new Aerodynamicist(UUID.randomUUID(),
                    "AI Aerodynamicist",
                    0,
                    random.nextInt(20) + 70),
            new Strategist(UUID.randomUUID(),
                    "AI Strategist",
                    0,
                    random.nextInt(20) + 70)
        );
    }

}

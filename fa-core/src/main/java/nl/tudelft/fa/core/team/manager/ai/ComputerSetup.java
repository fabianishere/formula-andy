package nl.tudelft.fa.core.team.manager.ai;

import nl.tudelft.fa.core.game.RaceSimulator;
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
 */
public class ComputerSetup {

    private Random rand = new Random();
    private Tire computerTire = new Tire(UUID.randomUUID(), "", TireType.INTERMEDIATE, 1, 1);
    private Engine computerEngine = new Engine(UUID.randomUUID(), "Renault", "", 1, 1, 1);
    private Driver computerDriver = new Driver(UUID.randomUUID(), "", 100, 100, 100, 100);
    private Aerodynamicist computerAerodynamicist = new Aerodynamicist(UUID.randomUUID(), "", 100, 1);
    private Strategist computerStrategist = new Strategist(UUID.randomUUID(), "", 100, 1);
    private Mechanic computerMechanic = new Mechanic(UUID.randomUUID(), "", 100, 1);
    private Car newcar = new Car(UUID.randomUUID());

    /**
     * Computer chooses a random tire, depending on if it's raining or not.
     * @param rc is the RaceSimulator, used to receive the boolean raining (getRaining()).
     */
    public void chooseTire(RaceSimulator rc) {
        boolean raining = rc.getRaining();
        int t = 0;
        if (!raining){
            t = rand.nextInt(4) + 3;
        }
        else {
            t = rand.nextInt(1) + 1;
        }
        switch(t) {
            case 1: computerTire = new Tire(UUID.randomUUID(), "", TireType.WET, 1, 1);
                break;
            case 2: computerTire = new Tire(UUID.randomUUID(), "", TireType.INTERMEDIATE, 1, 1);
                break;
            case 3: computerTire = new Tire(UUID.randomUUID(), "", TireType.ULTRA_SOFT, 1, 1);
                break;
            case 4: computerTire = new Tire(UUID.randomUUID(), "", TireType.SUPER_SOFT, 1, 1);
                break;
            case 5: computerTire = new Tire(UUID.randomUUID(), "", TireType.SOFT, 1, 1);
                break;
            case 6: computerTire = new Tire(UUID.randomUUID(), "", TireType.MEDIUM, 1, 1);
                break;
            case 7: computerTire = new Tire(UUID.randomUUID(), "", TireType.HARD, 1, 1);
                break;
            default: computerTire = new Tire(UUID.randomUUID(), "", TireType.INTERMEDIATE, 1, 1);
                break;
        }
    }

    /**
     * Computer chooses a random engine to work with.
     */
    public void chooseEngine() {
        int e = rand.nextInt(3) +1;
        switch(e) {
            case 1: computerEngine = new Engine(UUID.randomUUID(), "Mercedes", "", 1, 1, 1);
                break;
            case 2: computerEngine = new Engine(UUID.randomUUID(), "Ferrari", "", 1, 1, 1);
                break;
            case 3: computerEngine = new Engine(UUID.randomUUID(), "Renault", "", 1, 1, 1);
                break;
            case 4: computerEngine = new Engine(UUID.randomUUID(), "Honda", "", 1, 1, 1);
                break;
            default: computerEngine = new Engine(UUID.randomUUID(), "Renault", "", 1, 1, 1);
                break;
        }
    }


    /**
     * Method to choose random risks and create a new CarParameters with the risks and the chosen tire (from chooseTire) in it.
     */
    public void chooseRisk() {
        int m = rand.nextInt(2) + 1;
        int a = rand.nextInt(2) + 1;
        int s = rand.nextInt(2) + 1;
        new CarParameters(m, a, s, computerTire);
    }

    /**
     * Creates the actual car for the computer
     * @param newcar is the variable for chosen Car
     * @param computerEngine is the variable for the chosen Engine
     * @param computerDriver is the variable for the chose Driver
     * @param computerMechanic is the variable for the chosen Mechanic
     * @param computerAerodynamicist is the variable for the chosen Aerodynamicist
     * @param computerStrategist is the variable for the chosen Strategist
     * @return returns the created Car (CarConfiguration)
     */
    public CarConfiguration createCar(Car newcar, Engine computerEngine, Driver computerDriver, Mechanic computerMechanic, Aerodynamicist computerAerodynamicist, Strategist computerStrategist) {
        return new CarConfiguration(newcar, computerEngine, computerDriver, computerMechanic, computerAerodynamicist, computerStrategist);
    }

}

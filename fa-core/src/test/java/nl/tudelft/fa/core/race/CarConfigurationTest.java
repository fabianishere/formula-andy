package nl.tudelft.fa.core.race;

import nl.tudelft.fa.core.team.*;
import nl.tudelft.fa.core.team.inventory.Car;
import nl.tudelft.fa.core.team.inventory.Engine;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

/**
 * @author Fabian Mastenbroek <mail.fabianm@gmail.com>
 */
public class CarConfigurationTest {
    Car car;
    Driver driver;
    Engine engine;
    Mechanic mechanic;
    Aerodynamicist aerodynamicist;
    Strategist strategist;
    CarConfiguration configuration;

    @Before
    public void setUp() throws Exception {
        car = new Car(UUID.randomUUID(), null);
        driver = new Driver(UUID.randomUUID(), null, "Henry",  3, 4, 5, 6);
        engine = new Engine(UUID.randomUUID(), null, "Mercedes", "F1 W05 Hybrid", 1, 2, 3);
        mechanic = new Mechanic(UUID.randomUUID(), null, "Harry", 1, 2);
        aerodynamicist = new Aerodynamicist(UUID.randomUUID(), null, "Fred", 1, 2);
        strategist = new Strategist(UUID.randomUUID(), null, "Louis", 1, 2);
        configuration = new CarConfiguration(car, engine, driver, mechanic, aerodynamicist, strategist);
    }

    @Test
    public void getCar() throws Exception {
        assertEquals(car, configuration.getCar());
    }

    @Test
    public void getDriver() throws Exception {
        assertEquals(driver, configuration.getDriver());
    }

    @Test
    public void getEngine() throws Exception {
        assertEquals(engine, configuration.getEngine());
    }

    @Test
    public void getMechanic() throws Exception {
        assertEquals(mechanic, configuration.getMechanic());
    }

    @Test
    public void getAerodynamicist() throws Exception {
        assertEquals(aerodynamicist, configuration.getAerodynamicist());
    }

    @Test
    public void getStrategist() throws Exception {
        assertEquals(strategist, configuration.getStrategist());
    }

    @Test
    public void equalsNull() {
        assertThat(configuration, not(equalTo(null)));
    }

    @Test
    public void equalsDifferentType() {
        assertThat(configuration, not(equalTo("")));
    }

    @Test
    public void equalsReference() {
        assertEquals(configuration, configuration);
    }

    @Test
    public void equalsData() {
        assertEquals(new CarConfiguration(car, engine, driver, mechanic, aerodynamicist, strategist), configuration);
    }

    @Test
    public void equalsDifferentCar() {
        assertNotEquals(new CarConfiguration(new Car(UUID.randomUUID(), null), engine, driver, mechanic, aerodynamicist, strategist), configuration);
    }

    @Test
    public void equalsDifferentEngine() {
        Engine engine = new Engine(UUID.randomUUID(), null, "Merqcedes", "F1 W05 Hybrid", 1, 2, 3);;
        assertNotEquals(new CarConfiguration(car, engine, driver, mechanic, aerodynamicist, strategist), configuration);
    }

    @Test
    public void equalsDifferentDriver() {
        Driver driver = new Driver(UUID.randomUUID(), null, "Harm",  3, 4, 5, 6);
        assertNotEquals(new CarConfiguration(car, engine, driver, mechanic, aerodynamicist, strategist), configuration);
    }

    @Test
    public void equalsDifferentMechanic() {
        Mechanic mechanic = new Mechanic(UUID.randomUUID(), null, "Joop", 1, 2);
        assertNotEquals(new CarConfiguration(car, engine, driver, mechanic, aerodynamicist, strategist), configuration);
    }

    @Test
    public void equalsDifferentAerodynamicist() {
        Aerodynamicist aerodynamicist = new Aerodynamicist(UUID.randomUUID(), null, "Frenk", 1, 2);
        assertNotEquals(new CarConfiguration(car, engine, driver, mechanic, aerodynamicist, strategist), configuration);
    }

    @Test
    public void equalsDifferentStrategist() {
        Strategist  strategist = new Strategist(UUID.randomUUID(), null, "Calvin", 1, 2);
        assertNotEquals(new CarConfiguration(car, engine, driver, mechanic, aerodynamicist, strategist), configuration);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(car, engine, driver, mechanic, aerodynamicist, strategist), configuration.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("CarConfiguration(car=%s, engine=%s, driver=%s, mechanic=%s, "
                + "aerodynamicist=%s, strategist=%s)", car, engine, driver, mechanic,
            aerodynamicist, strategist), configuration.toString());
    }

}

package nl.tudelft.fa.core.team.inventory;

import nl.tudelft.fa.core.team.inventory.Engine;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class EngineTest {
    UUID id;
    String brand;
    String name;
    double power;
    double driveability;
    double fuelConsumption;
    Engine engine;

    @Before
    public void setUp() {
        id = UUID.randomUUID();
        brand = "Mercedes";
        name = "F1 W05 Hybrid";
        power = 5.0;
        driveability = 4.0;
        fuelConsumption = 1.0;
        engine = new Engine(id, brand, name, power, driveability, fuelConsumption);
    }

    @Test
    public void getId() throws Exception {
        assertEquals(id, engine.getId());
    }

    @Test
    public void getBrand() throws Exception {
        assertEquals(brand, engine.getBrand());
    }

    @Test
    public void getName() throws Exception {
        assertEquals(name, engine.getName());
    }

    @Test
    public void getPower() throws Exception {
        assertEquals(power, engine.getPower(), 0.01);
    }

    @Test
    public void getDriveability() throws Exception {
        assertEquals(driveability, engine.getDriveability(), 0.01);
    }

    @Test
    public void getFuelConsumption() throws Exception {
        assertEquals(fuelConsumption, engine.getFuelConsumption(), 0.01);
    }

    @Test
    public void equalsNull() {
        assertThat(engine, not(equalTo(null)));
    }

    @Test
    public void equalsReference() {
        assertEquals(engine, engine);
    }

    @Test
    public void equalsData() {
        assertEquals(new Engine(id, brand, name, power, driveability, fuelConsumption), engine);
    }

    @Test
    public void equalsDifferentId() {
        assertThat(engine, not(equalTo(new Engine(UUID.randomUUID(), brand, name, power, driveability, fuelConsumption))));
    }

    @Test
    public void equalsOtherPropertiesHaveNoEffect() {
        assertEquals(new Engine(id, brand + "m", name + "m", power + 1, driveability + 1, fuelConsumption + 1), engine);
    }

    @Test
    public void testHashCode() {
        assertEquals(id.hashCode(), engine.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals(String.format("Engine(id=%s, brand=%s, name=%s)", id, brand, name), engine.toString());
    }
}

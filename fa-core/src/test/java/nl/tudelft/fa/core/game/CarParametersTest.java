package nl.tudelft.fa.core.game;

import nl.tudelft.fa.core.race.CarParameters;
import nl.tudelft.fa.core.team.inventory.Tire;
import nl.tudelft.fa.core.team.inventory.TireType;
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
public class CarParametersTest {
    int mechanicalRisk;
    int aerodynamicRisk;
    int strategicRisk;
    Tire tire;
    CarParameters parameters;

    @Before
    public void setUp() throws Exception {
        mechanicalRisk = 2;
        aerodynamicRisk = 3;
        strategicRisk = 4;
        tire = new Tire(UUID.randomUUID(), "Pirelli", TireType.SOFT, 1, 2);
        parameters = new CarParameters(mechanicalRisk, aerodynamicRisk, strategicRisk, tire);
    }

    @Test
    public void getMechanicalRisk() throws Exception {
        assertEquals(mechanicalRisk, parameters.getMechanicalRisk());
    }

    @Test
    public void getAerodynamicRisk() throws Exception {
        assertEquals(aerodynamicRisk, parameters.getAerodynamicRisk());
    }

    @Test
    public void getStrategistRisk() throws Exception {
        assertEquals(strategicRisk, parameters.getStrategistRisk());
    }

    @Test
    public void getTire() throws Exception {
        assertEquals(tire, parameters.getTire());
    }

    @Test
    public void equalsNull() {
        assertThat(parameters, not(equalTo(null)));
    }

    @Test
    public void equalsReference() {
        assertEquals(parameters, parameters);
    }

    @Test
    public void equalsData() {
        assertEquals( new CarParameters(mechanicalRisk, aerodynamicRisk, strategicRisk, tire), parameters);
    }

    @Test
    public void equalsDifferentMechanicalRisk() {
        assertNotEquals( new CarParameters(mechanicalRisk + 1, aerodynamicRisk, strategicRisk, tire), parameters);
    }

    @Test
    public void equalsDifferentAerodynamicRisk() {
        assertNotEquals(new CarParameters(mechanicalRisk, aerodynamicRisk + 1, strategicRisk, tire), parameters);
    }

    @Test
    public void equalsDifferentDriver() {
        assertNotEquals(new CarParameters(mechanicalRisk, aerodynamicRisk, strategicRisk + 1, tire), parameters);
    }

    @Test
    public void equalsDifferentMechanic() {
        Tire tire = new Tire(UUID.randomUUID(), "Pirelli", TireType.SOFT, 1, 2);
        assertNotEquals(new CarParameters(mechanicalRisk, aerodynamicRisk, strategicRisk, tire), parameters);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(mechanicalRisk, aerodynamicRisk, strategicRisk, tire), parameters.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("CarParameters(mechanical=%d, aerodynamic=%d, strategic=%d, tire=%s)",
            mechanicalRisk, aerodynamicRisk, strategicRisk, tire), parameters.toString());
    }

}

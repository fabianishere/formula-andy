package nl.tudelft.fa.core.race;

import nl.tudelft.fa.core.team.inventory.Car;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class CarSimulationResultTest {
    private Car car;
    private double distance;
    private boolean crashed;
    private CarSimulationResult result;

    @Before
    public void setUp() {
        car = new Car(UUID.randomUUID());
        distance = 1.0;
        crashed = false;
        result = new CarSimulationResult(car, distance, crashed);
    }

    @Test
    public void testDistance() {
        assertEquals(distance, result.getDistanceTraveled(), 0.001);
    }

    @Test
    public void testCrashed() {
        assertEquals(crashed, result.hasCrashed());
    }

    @Test
    public void testCar() {
        assertEquals(car, result.getCar());
    }

    @Test
    public void testCrash() {
        result = result.crash();
        assertTrue(result.hasCrashed());
    }

    @Test
    public void testIncreaseDistanceNotCrashed() {
        result = result.increaseDistance(3.0);
        assertEquals(distance + 3.0, result.getDistanceTraveled(), 0.01);
    }

    @Test
    public void testIncreaseDistanceCrashed() {
        result = result.crash().increaseDistance(3.0);
        assertEquals(distance, result.getDistanceTraveled(), 0.01);
    }

    @Test
    public void equalsNull() {
        assertThat(result, not(equalTo(null)));
    }

    @Test
    public void equalsDifferentType() {
        assertThat(result, not(equalTo("")));
    }

    @Test
    public void equalsReference() {
        assertEquals(result, result);
    }

    @Test
    public void equalsData() {
        assertEquals(new CarSimulationResult(car, distance, crashed), result);
    }

    @Test
    public void equalsDifferentCar() {
        assertNotEquals(new CarSimulationResult(new Car(UUID.randomUUID()), distance, crashed), result);
    }

    @Test
    public void equalsDifferentDistance() {
        assertNotEquals(new CarSimulationResult(car, distance + 4.0, crashed), result);
    }

    @Test
    public void equalsDifferentCrashed() {
        assertNotEquals(new CarSimulationResult(car, distance, !crashed), result);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(distance, crashed, car), result.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("CarSimulationResult(car=%s, distanceTraveled=%f, crashed=%s)", car, distance, crashed), result.toString());
    }
}

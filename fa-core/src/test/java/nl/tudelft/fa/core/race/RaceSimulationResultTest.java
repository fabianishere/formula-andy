package nl.tudelft.fa.core.race;

import nl.tudelft.fa.core.team.inventory.Car;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class RaceSimulationResultTest {

    private Map<Car, CarSimulationResult> cars;
    private boolean finished;
    private RaceSimulationResult result;

    @Before
    public void setUp() {
        cars = new HashMap<Car, CarSimulationResult>() {{
            Car car = new Car(UUID.randomUUID());
            put(car, new CarSimulationResult(car, 100, true));
        }};
        finished = false;
        result = new RaceSimulationResult(cars, finished);
    }

    @Test
    public void testCars() {
        assertEquals(cars, result.getCars());
    }

    @Test
    public void testFinished() {
        assertEquals(finished, result.isFinished());
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
        assertEquals(new RaceSimulationResult(cars, finished), result);
    }

    @Test
    public void equalsDifferentCars() {
        assertNotEquals(new RaceSimulationResult(Collections.emptyMap(), finished), result);
    }

    @Test
    public void equalsDifferentFinish() {
        assertNotEquals(new RaceSimulationResult(cars, !finished), result);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(cars, finished), result.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("RaceSimulationResult(cars=%s, finished=%s)", cars, finished), result.toString());
    }
}

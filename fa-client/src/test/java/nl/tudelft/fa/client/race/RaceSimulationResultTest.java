package nl.tudelft.fa.client.race;

import nl.tudelft.fa.client.team.inventory.Car;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class RaceSimulationResultTest {

    private Set<CarSimulationResult> cars;
    private boolean finished;
    private RaceSimulationResult result;

    @Before
    public void setUp() {
        cars = Collections.singleton(new CarSimulationResult(new Car(UUID.randomUUID()), 100, true));
        finished = false;
        result = new RaceSimulationResult(cars, finished);
    }

    @Test
    public void testCars() {
        assertEquals(cars, result.getResults());
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
        assertNotEquals(new RaceSimulationResult(Collections.emptySet(), finished), result);
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
        assertEquals(String.format("RaceSimulationResult(results=%s, finished=%s)", cars, finished), result.toString());
    }
}

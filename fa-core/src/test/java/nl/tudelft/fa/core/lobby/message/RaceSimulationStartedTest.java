package nl.tudelft.fa.core.lobby.message;

import nl.tudelft.fa.core.race.CarConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class RaceSimulationStartedTest {
    private Set<CarConfiguration> cars;
    private RaceSimulationStarted event;

    @Before
    public void setUp() throws Exception {
        cars = Collections.singleton(new CarConfiguration(null, null, null, null, null, null));
        event = new RaceSimulationStarted(cars);
    }
    @Test
    public void getCars() {
        assertEquals(cars, event.getCars());
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(cars), event.hashCode());
    }

    @Test
    public void equalsNull() {
        assertThat(event, not(equalTo(null)));
    }

    @Test
    public void equalsDifferentType() {
        assertThat(event, not(equalTo("")));
    }

    @Test
    public void equalsReference() {
        assertEquals(event, event);
    }

    @Test
    public void equalsData() {
        assertEquals(new RaceSimulationStarted(cars), event);
    }

    @Test
    public void equalsDifferentCars() {
        assertNotEquals(new RaceSimulationStarted(Collections.emptySet()), event);
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("RaceSimulationStarted(cars=%s)", cars), event.toString());
    }

}

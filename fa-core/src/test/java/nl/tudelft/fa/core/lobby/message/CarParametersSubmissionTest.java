package nl.tudelft.fa.core.lobby.message;

import nl.tudelft.fa.core.race.CarParameters;
import nl.tudelft.fa.core.team.inventory.Car;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class CarParametersSubmissionTest {
    private Car car;
    private CarParameters parameters;
    private CarParametersSubmission msg;

    @Before
    public void setUp() throws Exception {
        car = new Car(UUID.randomUUID());
        parameters = new CarParameters(0, 0, 0, null);
        msg = new CarParametersSubmission(car, parameters);
    }

    @Test
    public void getCar() throws Exception {
        assertEquals(car, msg.getCar());
    }

    @Test
    public void getParameters() throws Exception {
        assertEquals(parameters, msg.getParameters());
    }

    @Test
    public void equalsDifferentType() {
        assertThat(msg, not(equalTo("")));
    }

    @Test
    public void equalsNull() {
        assertThat(msg, not(equalTo(null)));
    }

    @Test
    public void equalsReference() {
        assertEquals(msg, msg);
    }

    @Test
    public void equalsData() {
        assertEquals(new CarParametersSubmission(car, parameters), msg);
    }

    @Test
    public void equalsDifferentCar() {
        assertNotEquals(new CarParametersSubmission(new Car(UUID.randomUUID()), parameters),
            msg);
    }

    @Test
    public void equalsDifferentParameters() {
        assertNotEquals(new CarParametersSubmission(car, new CarParameters(1, 0, 0, null)), msg);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(car, parameters), msg.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("CarParametersSubmission(car=%s, parameters=%s)", car, parameters), msg.toString());
    }
}

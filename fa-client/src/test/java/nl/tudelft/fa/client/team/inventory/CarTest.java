package nl.tudelft.fa.client.team.inventory;

import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class CarTest {
    UUID id;
    Car car;

    @Before
    public void setUp() {
        id = UUID.randomUUID();
        car = new Car(id);
    }

    @Test
    public void getId() throws Exception {
        assertEquals(id, car.getId());
    }

    @Test
    public void equalsNull() {
        assertThat(car, not(equalTo(null)));
    }

    @Test
    public void equalsDifferentType() {
        assertThat(car, not(equalTo("")));
    }

    @Test
    public void equalsReference() {
        assertEquals(car, car);
    }

    @Test
    public void equalsData() {
        assertEquals(new Car(id), car);
    }

    @Test
    public void equalsDifferentId() {
        assertThat(car, not(equalTo(new Car(UUID.randomUUID()))));
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hash(id), car.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals(String.format("Car(id=%s)", id), car.toString());
    }
}

package nl.tudelft.fa.client.race;

import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class CircuitTest {
    UUID id;
    String name;
    String country;
    Circuit circuit;
    int length;

    @Before
    public void setUp() throws Exception {
        id = UUID.randomUUID();
        name = "Spa";
        country = "Belgium";
        length = 5000;
        circuit = new Circuit(id, name, country, length);
    }

    @Test
    public void getLength() {
        assertEquals(length, circuit.getLength());
    }
    @Test
    public void getId() throws Exception {
        assertEquals(id, circuit.getId());
    }

    @Test
    public void getName() throws Exception {
        assertEquals(name, circuit.getName());
    }

    @Test
    public void getCountry() throws Exception {
        assertEquals(country, circuit.getCountry());
    }

    @Test
    public void equalsNull() {
        assertThat(circuit, not(equalTo(null)));
    }

    @Test
    public void equalsDifferentType() {
        assertThat(circuit, not(equalTo("")));
    }

    @Test
    public void equalsReference() {
        assertEquals(circuit, circuit);
    }

    @Test
    public void equalsData() {
        assertEquals(circuit = new Circuit(id, name, country, length), circuit);
    }

    @Test
    public void equalsDifferentId() {
        assertThat(circuit, not(equalTo(new Circuit(UUID.randomUUID(), name, country, length))));
    }

    @Test
    public void equalsOtherPropertiesHaveNoEffect() {
        assertEquals(new Circuit(id, "Monza", "Italy", length), circuit);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(id), circuit.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("Circuit(id=%s, name=%s, country=%s, length=%d)", id, name, country, length), circuit.toString());
    }

}

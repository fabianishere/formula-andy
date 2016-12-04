package nl.tudelft.fa.core.model.game;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class CircuitTest {
    UUID id;
    String name;
    String country;
    Circuit circuit;

    @Before
    public void setUp() throws Exception {
        id = UUID.randomUUID();
        name = "Spa";
        country = "Belgium";
        circuit = new Circuit(id, name, country);
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
    public void equalsReference() {
        assertEquals(circuit, circuit);
    }

    @Test
    public void equalsData() {
        assertEquals(circuit = new Circuit(id, name, country), circuit);
    }

    @Test
    public void equalsDifferentId() {
        assertThat(circuit, not(equalTo(new Circuit(UUID.randomUUID(), name, country))));
    }

    @Test
    public void equalsOtherPropertiesHaveNoEffect() {
        assertEquals(new Circuit(id, "Monza", "Italy"), circuit);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(id.hashCode(), circuit.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("Circuit(id=%s, name=%s, country=%s)", id, name, country), circuit.toString());
    }

}

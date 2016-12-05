package nl.tudelft.fa.core.game;

import nl.tudelft.fa.core.race.Circuit;
import nl.tudelft.fa.core.race.GrandPrix;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

/**
 * @author Fabian Mastenbroek <mail.fabianm@gmail.com>
 */
public class GrandPrixTest {
    UUID id;
    Circuit circuit;
    Instant date;
    int laps;
    GrandPrix grandPrix;

    @Before
    public void setUp() throws Exception {
        id = UUID.randomUUID();
        circuit = new Circuit(UUID.randomUUID(), "Monza", "Italy");
        date = Instant.now();
        laps = 30;
        grandPrix = new GrandPrix(id, circuit, date, laps);
    }

    @Test
    public void getId() throws Exception {
        assertEquals(id, grandPrix.getId());
    }

    @Test
    public void getCircuit() throws Exception {
        assertEquals(circuit, grandPrix.getCircuit());
    }

    @Test
    public void getDate() throws Exception {
        assertEquals(date, grandPrix.getDate());
    }

    @Test
    public void getLaps() throws Exception {
        assertEquals(laps, grandPrix.getLaps());
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(id.hashCode(), grandPrix.hashCode());
    }

    @Test
    public void equalsNull() {
        assertThat(grandPrix, not(equalTo(null)));
    }

    @Test
    public void equalsReference() {
        assertEquals(grandPrix, grandPrix);
    }

    @Test
    public void equalsData() {
        assertEquals(new GrandPrix(id, circuit, date, laps), grandPrix);
    }

    @Test
    public void equalsDifferentId() {
        assertThat(grandPrix, not(equalTo(new GrandPrix(UUID.randomUUID(), circuit, date, laps))));
    }

    @Test
    public void equalsOtherPropertiesHaveNoEffect() {
        assertEquals(new GrandPrix(id, new Circuit(UUID.randomUUID(),"Spa", "Belgium"), Instant.now(),
            10), grandPrix);
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("GrandPrix(id=%s, circuit=%s, date=%s, laps=%d)", id, circuit,
            date, laps), grandPrix.toString());
    }

}

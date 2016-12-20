package nl.tudelft.fa.core.race;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Random;
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
    int rainChance;
    GrandPrix grandPrix;

    long seed;
    long seed2;

    @Before
    public void setUp() throws Exception {
        id = UUID.randomUUID();
        circuit = new Circuit(UUID.randomUUID(), "Monza", "Italy", 5000);
        date = Instant.now();
        laps = 30;
        rainChance = 80;
        grandPrix = new GrandPrix(id, circuit, date, laps, rainChance);

        seed = 1; // nextInt(101) = 97
        seed2 = 2; // nextInt(101) = 5
    }
    @Test
    public void getRainChance() {
        assertEquals(rainChance, grandPrix.getRainChance());
    }

    @Test
    public void isItRaining() {
        assertTrue(grandPrix.isItRaining(new Random(seed2)));
    }

    @Test
    public void isItRaining2() {
        assertFalse(grandPrix.isItRaining(new Random(seed)));
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
        assertEquals(new GrandPrix(id, circuit, date, laps, rainChance), grandPrix);
    }

    @Test
    public void equalsDifferentId() {
        assertThat(grandPrix, not(equalTo(new GrandPrix(UUID.randomUUID(), circuit, date, laps, rainChance))));
    }

    @Test
    public void equalsOtherPropertiesHaveNoEffect() {
        assertEquals(new GrandPrix(id, new Circuit(UUID.randomUUID(),"Spa", "Belgium", 5000), Instant.now(),
            10, rainChance), grandPrix);
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("GrandPrix(id=%s, circuit=%s, date=%s, laps=%d)", id, circuit,
            date, laps), grandPrix.toString());
    }

}

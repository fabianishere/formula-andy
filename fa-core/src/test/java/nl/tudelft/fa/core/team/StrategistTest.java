package nl.tudelft.fa.core.team;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class StrategistTest {
    @Test
    public void testToString() throws Exception {
        UUID id = UUID.randomUUID();
        String name = "Bjorn";
        int salary = 445;
        double level = 1;
        Strategist strategist = new Strategist(id, name, salary, level);

        assertEquals(String.format("Strategist(id=%s, name=%s, salary=%d, level=%f)",
            id, name, salary, level), strategist.toString());
    }
}

package nl.tudelft.fa.client.team;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class MechanicTest {
    @Test
    public void testToString() throws Exception {
        UUID id = UUID.randomUUID();
        String name = "Bjorn";
        int salary = 445;
        double level = 1;
        Mechanic mechanic = new Mechanic(id, name, salary, level);

        assertEquals(String.format("Mechanic(id=%s, name=%s, salary=%d, level=%f)",
            id, name, salary, level), mechanic.toString());
    }
}

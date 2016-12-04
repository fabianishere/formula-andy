package nl.tudelft.fa.core.model.team;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class AerodynamicistTest {
    @Test
    public void testToString() throws Exception {
        UUID id = UUID.randomUUID();
        String name = "Bjorn";
        int salary = 445;
        double level = 1;
        Aerodynamicist aerodynamicist = new Aerodynamicist(id, name, salary, level);

        assertEquals(String.format("Aerodynamicist(id=%s, name=%s, salary=%d, level=%f)",
            id, name, salary, level), aerodynamicist.toString());
    }
}

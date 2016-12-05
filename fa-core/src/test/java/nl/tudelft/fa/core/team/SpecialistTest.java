package nl.tudelft.fa.core.team;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class SpecialistTest {
    @Test
    public void getLevel() throws Exception {
        assertEquals(100, new Strategist(UUID.randomUUID(), "Hank", 123, 100).getLevel(), 0.01);
    }
}

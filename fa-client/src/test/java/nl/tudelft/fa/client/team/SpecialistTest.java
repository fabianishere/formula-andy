package nl.tudelft.fa.client.team;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class SpecialistTest {

    double delta;

    Mechanic mechanic;
    String name;
    int salary;
    int level;

    @Before
    public void setUp() {
        this.delta = 0.0000001;

        this.name = "Henk";
        this.salary = 261537256;
        this.level = 80;

        this.mechanic = new Mechanic(UUID.randomUUID(), name, salary, level);
    }

    @Test
    public void getLevel() throws Exception {
        assertEquals(level, mechanic.getLevel(), 0.01);
    }
}

package nl.tudelft.fa.core.team;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class SpecialistTest {

    double delta;

    Mechanic mechanic;
    String name;
    int salary;
    int level;

    int lowRisk;
    int mediumRisk;
    int highRisk;

    double lowRiskFactor;
    double mediumRiskFactor;
    double highRiskFactor;

    @Before
    public void setUp() {
        this.delta = 0.0000001;

        this.name = "Henk";
        this.salary = 261537256;
        this.level = 80;

        this.lowRisk = 1;
        this.mediumRisk = 2;
        this.highRisk = 3;

        this.lowRiskFactor = 0.7;
        this.mediumRiskFactor = 0.8;
        this.highRiskFactor = 1.0;

        this.mechanic = new Mechanic(UUID.randomUUID(), name, salary, level);
    }

    @Test
    public void getLevel() throws Exception {
        assertEquals(level, mechanic.getLevel(), 0.01);
    }

    @Test
    public void getSpecialistFactorHighRisk() throws Exception {
        assertEquals((double) level / 100 * highRiskFactor, mechanic.getSpecialistFactor(highRisk), delta);
    }

    @Test
    public void getSpecialistFactorMediumRisk() throws Exception {
        assertEquals((double) level / 100 * mediumRiskFactor, mechanic.getSpecialistFactor(mediumRisk), delta);
    }

    @Test
    public void getSpecialistFactorLowRisk() throws Exception {
        assertEquals((double) level / 100 * lowRiskFactor, mechanic.getSpecialistFactor(lowRisk), delta);
    }


}

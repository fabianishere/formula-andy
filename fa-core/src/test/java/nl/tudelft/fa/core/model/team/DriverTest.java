package nl.tudelft.fa.core.model.team;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class DriverTest {

    UUID id;
    String name;
    int salary;
    double speed;
    double racecraft;
    double strategy;
    Driver driver;

    @Before
    public void setUp() throws Exception {
        id = UUID.randomUUID();
        name = "Tom";
        salary = 10000;
        speed = 0.5;
        racecraft = 1.2;
        strategy = 56;
        driver = new Driver(id, name, salary, speed, racecraft, strategy);
    }

    @Test
    public void getSpeed() throws Exception {
        assertEquals(speed, driver.getSpeed(), 0.01);
    }

    @Test
    public void getSalary() throws Exception {
        assertEquals(salary, driver.getSalary(), 0.01);
    }

    @Test
    public void getStrategy() throws Exception {
        assertEquals(strategy, driver.getStrategy(), 0.01);
    }

    @Test
    public void testToString() {
        assertEquals(String.format("Driver(id=%s, name=%s, salary=%d, speed=%f, racecraft=%f,"
            + " strategy=%f)", id, name, salary, speed, racecraft, strategy), driver.toString());
    }
}

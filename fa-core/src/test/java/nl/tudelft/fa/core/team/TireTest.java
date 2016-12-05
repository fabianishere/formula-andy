package nl.tudelft.fa.core.team;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


/**
 * @author Fabian Mastenbroek <mail.fabianm@gmail.com>
 */
public class TireTest {

    UUID id;
    String brand;
    String name;
    double durability;
    double grip;
    Tire tireA;

    @Before
    public void setUp() {
        id = UUID.randomUUID();
        brand = "Pirelli";
        name = "Intermediate";
        durability = 1.0;
        grip = 2.0;
        tireA = new Tire(id, brand, name, durability, grip);
    }

    @Test
    public void getId() {
        assertEquals(id, tireA.getId());
    }

    @Test
    public void getBrand() {
        assertEquals(brand, tireA.getBrand());
    }

    @Test
    public  void getName() {
        assertEquals(name, tireA.getName());
    }

    @Test
    public void getDurability() {
        assertEquals(durability, tireA.getDurability(), 0.01);
    }

    @Test
    public void getGrip() {
        assertEquals(grip, tireA.getGrip(), 0.01);
    }

    @Test
    public void equalsNull() {
        assertThat(tireA, not(equalTo(null)));
    }

    @Test
    public void equalsReference() {
        assertEquals(tireA, tireA);
    }

    @Test
    public void equalsData() {
        assertEquals(new Tire(id, brand, name, durability, grip), tireA);
    }

    @Test
    public void equalsDifferentId() {
        assertThat(tireA, not(equalTo(new Tire(UUID.randomUUID(), brand, name, durability, grip))));
    }

    @Test
    public void testHashCode() {
        assertEquals(id.hashCode(), tireA.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals(String.format("Tire(id=%s, brand=%s, name=%s)", id, brand, name), tireA.toString());
    }
}

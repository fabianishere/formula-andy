package nl.tudelft.fa.core.model.team;

import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


/**
 * @author Fabian Mastenbroek <mail.fabianm@gmail.com>
 */
public class TireTest {

    String brand;
    String name;
    double durability;
    double grip;
    Tire tireA;

    @Before
    public void setUp() {
        brand = "Pirelli";
        name = "Intermediate";
        durability = 1.0;
        grip = 2.0;
        tireA = new Tire(brand, name, durability, grip);
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
        assertEquals(new Tire(brand, name, durability, grip), tireA);
    }

    @Test
    public void equalsDifferentBrand() {
        assertThat(tireA, not(equalTo(new Tire("Silverstone", name, durability, grip))));
    }

    @Test
    public void equalsDifferentType() {
        assertThat(tireA, not(equalTo(new Tire(brand, "Soft", durability, grip))));
    }

    @Test
    public void equalsOtherPropertiesHaveNoEffect() {
        assertEquals(new Tire(brand, name, durability, grip), tireA);
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hash(brand, name), tireA.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals(String.format("Tire(brand=%s, name=%s)", brand, name), tireA.toString());
    }
}

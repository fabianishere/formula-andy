package nl.tudelft.fa.core.team.inventory;

import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TireTest {

    double delta;
    UUID id;
    String brand;
    TireType type;
    double durability;
    double grip;
    Tire tireA;

    @Before
    public void setUp() {
        delta = 0.000001;
        id = UUID.randomUUID();
        brand = "Pirelli";
        type = TireType.INTERMEDIATE;
        durability = 1.0;
        grip = 7.0;
        tireA = new Tire(id, brand, type, durability, grip);
    }

    @Test
    public void getResistanceFactor() {
        assertEquals((double) 1 - tireA.getGrip() * tireA.getGrip() / 250, tireA.getResistanceFactor(), delta);
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
    public  void getType() {
        assertEquals(type, tireA.getType());
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
    public void equalsDifferentType() {
        assertThat(tireA, not(equalTo("")));
    }

    @Test
    public void equalsReference() {
        assertEquals(tireA, tireA);
    }

    @Test
    public void equalsData() {
        assertEquals(new Tire(id, brand, type, durability, grip), tireA);
    }

    @Test
    public void equalsDifferentId() {
        assertThat(tireA, not(equalTo(new Tire(UUID.randomUUID(), brand, type, durability, grip))));
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hash(id), tireA.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals(String.format("Tire(id=%s, brand=%s, type=%s)", id, brand, type), tireA.toString());
    }
}

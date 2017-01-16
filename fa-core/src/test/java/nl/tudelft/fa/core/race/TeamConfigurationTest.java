package nl.tudelft.fa.core.race;

import nl.tudelft.fa.core.team.inventory.Car;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class TeamConfigurationTest {
    Set<CarConfiguration> configurations;
    TeamConfiguration configuration;

    @Before
    public void setUp() throws Exception {
       configurations = new HashSet<>();
       configuration = new TeamConfiguration(configurations);
    }

    @Test
    public void getConfigurations() throws Exception {
        assertEquals(configurations, configuration.getConfigurations());
    }

    @Test
    public void equalsNull() {
        assertThat(configuration, not(equalTo(null)));
    }

    @Test
    public void equalsDifferentType() {
        assertThat(configuration, not(equalTo("")));
    }

    @Test
    public void equalsReference() {
        assertEquals(configuration, configuration);
    }

    @Test
    public void equalsData() {
        assertEquals(new TeamConfiguration(configurations), configuration);
    }

    @Test
    public void equalsDifferentConfigurations() {
        CarConfiguration config = new CarConfiguration(new Car(UUID.randomUUID(), null), null, null, null, null, null);
        Set<CarConfiguration> configurations = new HashSet<CarConfiguration>() {{
            add(config);
        }};
        assertNotEquals(new TeamConfiguration(configurations), configuration);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(configurations), configuration.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("TeamConfiguration(configurations=%s)", configurations),
            configuration.toString());
    }

}

package nl.tudelft.fa.client.lobby.message;

import nl.tudelft.fa.client.race.CarConfiguration;
import nl.tudelft.fa.client.team.inventory.Car;
import nl.tudelft.fa.client.user.User;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class TeamConfigurationSubmittedTest {
    private User user;
    private Set<CarConfiguration> configuration;
    private TeamConfigurationSubmitted event;

    @Before
    public void setUp() throws Exception {
        user = new User(UUID.randomUUID(), "a");
        configuration = Collections.emptySet();
        event = new TeamConfigurationSubmitted(user, configuration);
    }

    @Test
    public void getUser() throws Exception {
        assertEquals(user, event.getUser());
    }

    @Test
    public void getConfiguration() throws Exception {
        assertEquals(configuration, event.getCars());
    }

    @Test
    public void equalsDifferentType() {
        assertThat(event, not(equalTo("")));
    }

    @Test
    public void equalsNull() {
        assertThat(event, not(equalTo(null)));
    }

    @Test
    public void equalsReference() {
        assertEquals(event, event);
    }

    @Test
    public void equalsData() {
        assertEquals(new TeamConfigurationSubmitted(user, configuration), event);
    }

    @Test
    public void equalsDifferentUser() {
        assertNotEquals(new TeamConfigurationSubmitted(new User(UUID.randomUUID(), "b"), configuration),
            event);
    }

    @Test
    public void equalsDifferentConfiguration() {
        assertNotEquals(new TeamConfigurationSubmitted(user, new HashSet<CarConfiguration>() {{
            add(new CarConfiguration(new Car(UUID.randomUUID()), null, null, null, null, null));
        }}), event);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(user, configuration), event.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("TeamConfigurationSubmitted(user=%s, cars=%s)", user, configuration), event.toString());
    }
}

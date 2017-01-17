package nl.tudelft.fa.core.lobby.message;

import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.race.CarConfiguration;
import nl.tudelft.fa.core.team.inventory.Car;
import nl.tudelft.fa.core.user.User;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class TeamConfigurationSubmissionTest {
    private User user;
    private Set<CarConfiguration> configuration;
    private TeamConfigurationSubmission event;

    @Before
    public void setUp() throws Exception {
        user = new User(UUID.randomUUID(), new Credentials("a", "b"));
        configuration = Collections.emptySet();
        event = new TeamConfigurationSubmission(user, configuration);
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
        assertEquals(new TeamConfigurationSubmission(user, configuration), event);
    }

    @Test
    public void equalsDifferentUser() {
        assertNotEquals(new TeamConfigurationSubmission(new User(UUID.randomUUID(), new Credentials("b", "c")), configuration),
            event);
    }

    @Test
    public void equalsDifferentConfiguration() {
        assertNotEquals(new TeamConfigurationSubmission(user, new HashSet<CarConfiguration>() {{
            add(new CarConfiguration(new Car(UUID.randomUUID()), null, null, null, null, null));
            }}), event);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(user, configuration), event.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("TeamConfigurationSubmission(user=%s, cars=%s)", user, configuration), event.toString());
    }
}

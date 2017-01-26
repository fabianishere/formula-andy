package nl.tudelft.fa.client.lobby.message;

import nl.tudelft.fa.client.race.CarConfiguration;
import nl.tudelft.fa.client.team.Team;
import nl.tudelft.fa.client.team.inventory.Car;
import nl.tudelft.fa.client.user.User;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class TeamConfigurationSubmissionTest {
    private Team team;
    private Set<CarConfiguration> configuration;
    private TeamConfigurationSubmission event;

    @Before
    public void setUp() throws Exception {
        team = new Team(UUID.randomUUID(), "test", 100, new User(UUID.randomUUID(), null));
        configuration = Collections.emptySet();
        event = new TeamConfigurationSubmission(team, configuration);
    }

    @Test
    public void getUser() throws Exception {
        assertEquals(team, event.getTeam());
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
        assertEquals(new TeamConfigurationSubmission(team, configuration), event);
    }

    @Test
    public void equalsDifferentUser() {
        assertNotEquals(new TeamConfigurationSubmission(new Team(UUID.randomUUID(), null, 1, null), configuration),
            event);
    }

    @Test
    public void equalsDifferentConfiguration() {
        assertNotEquals(new TeamConfigurationSubmission(team, new HashSet<CarConfiguration>() {{
            add(new CarConfiguration(new Car(UUID.randomUUID()), null, null, null, null, null));
            }}), event);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(team, configuration), event.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("TeamConfigurationSubmission(team=%s, cars=%s)", team, configuration), event.toString());
    }
}

package nl.tudelft.fa.client.lobby;

import nl.tudelft.fa.client.race.GrandPrix;
import nl.tudelft.fa.client.team.Team;
import nl.tudelft.fa.client.user.User;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class LobbyTest {
    private String id;
    private LobbyStatus status;
    private LobbyConfiguration configuration;
    private Set<Team> teams;
    private List<GrandPrix> schedule;
    private Lobby information;

    @Before
    public void setUp() {
        id = UUID.randomUUID().toString();
        status = LobbyStatus.PREPARATION;
        configuration = new LobbyConfiguration(11, Duration.ofMinutes(5), Duration.ZERO);
        teams = new HashSet<>();
        teams.add(new Team(UUID.randomUUID(), "test", 100, new User(UUID.randomUUID(), null)));
        schedule = new ArrayList<>();
        schedule.add(new GrandPrix(UUID.randomUUID(), null, null, 0, 0));
        information = new Lobby(id, status, configuration, teams, schedule);
    }

    @Test
    public void testId() {
        assertEquals(id, information.getId());
    }

    @Test
    public void testStatus() {
        assertEquals(status, information.getStatus());
    }

    @Test
    public void testConfiguration() {
        assertEquals(configuration, information.getConfiguration());
    }

    @Test
    public void testUsers() {
        assertEquals(teams, information.getTeams());
    }

    @Test
    public void testSchedule() {
        assertEquals(schedule, information.getSchedule());
    }

    @Test
    public void equalsNull() {
        assertThat(information, not(equalTo(null)));
    }

    @Test
    public void equalsDifferentType() {
        assertThat(information, not(equalTo("")));
    }

    @Test
    public void equalsReference() {
        assertEquals(information, information);
    }

    @Test
    public void equalsData() {
        assertEquals(new Lobby(id, status, configuration, teams, schedule), information);
    }

    @Test
    public void equalsDifferentId() {
        assertNotEquals(new Lobby(UUID.randomUUID().toString(), status, configuration, teams, schedule), information);
    }


    @Test
    public void equalsDifferentStatus() {
        assertNotEquals(new Lobby(id, LobbyStatus.PROGRESSION, configuration, teams, schedule), information);
    }

    @Test
    public void equalsDifferentConfiguration() {
        assertNotEquals(new Lobby(id, status, new LobbyConfiguration(0, Duration.ZERO, Duration.ZERO), teams, schedule), information);
    }

    @Test
    public void equalsDifferentUsers() {
        assertNotEquals(new Lobby(id, status, configuration, Collections.emptySet(), schedule), information);
    }

    @Test
    public void equalsDifferentSchedule() {
        assertNotEquals(new Lobby(id, status, configuration, teams, Collections.emptyList()), information);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(id, status, configuration, teams, schedule), information.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("Lobby(id=%s, status=%s, configuration=%s, teams=%s, schedule=%s)",
            id, status, configuration, teams, schedule), information.toString());
    }
}

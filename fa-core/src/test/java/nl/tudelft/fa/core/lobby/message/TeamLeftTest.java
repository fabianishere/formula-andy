package nl.tudelft.fa.core.lobby.message;

import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.team.Team;
import nl.tudelft.fa.core.user.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class TeamLeftTest {
    private Team team;
    private TeamLeft event;

    @Before
    public void setUp() throws Exception {
        team = new Team(UUID.randomUUID(), "test", 100, new User(UUID.randomUUID(), null));
        event = new TeamLeft(team);
    }

    @Test
    public void getUser() throws Exception {
        assertEquals(team, event.getTeam());
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
        assertEquals(new TeamLeft(team), event);
    }

    @Test
    public void equalsDifferentUser() {
        assertNotEquals(new TeamLeft(new Team(UUID.randomUUID(), null, 1, null)),
            event);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(team), event.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("TeamLeft(team=%s)", team), event.toString());
    }
}

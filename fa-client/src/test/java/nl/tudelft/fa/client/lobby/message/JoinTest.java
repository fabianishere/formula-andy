package nl.tudelft.fa.client.lobby.message;

import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import nl.tudelft.fa.client.team.Team;
import nl.tudelft.fa.client.user.User;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class JoinTest {
    private Team team;
    private Join req;

    @Before
    public void setUp() throws Exception {
        team = new Team(UUID.randomUUID(), "test", 100, new User(UUID.randomUUID(), null));
        req = new Join(team);
    }

    @Test
    public void getUser() throws Exception {
        assertEquals(team, req.getTeam());
    }

    @Test
    public void equalsDifferentType() {
        assertThat(req, not(equalTo("")));
    }

    @Test
    public void equalsNull() {
        assertThat(req, not(equalTo(null)));
    }

    @Test
    public void equalsReference() {
        assertEquals(req, req);
    }

    @Test
    public void equalsData() {
        assertEquals(new Join(team), req);
    }

    @Test
    public void equalsDifferentUser() {
        assertNotEquals(new Join(new Team(UUID.randomUUID(), null, 1, null)),
            req);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(team), req.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("Join(team=%s)", team), req.toString());
    }
}

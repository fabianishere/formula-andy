package nl.tudelft.fa.core.lobby.message;

import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.team.Team;
import nl.tudelft.fa.core.user.User;
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
    private static ActorSystem system;
    private Team team;
    private Join req;
    private JavaTestKit probe;

    @BeforeClass
    public static void setUpClass() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void tearDownClass() {
        JavaTestKit.shutdownActorSystem(system);
    }

    @Before
    public void setUp() throws Exception {
        probe = new JavaTestKit(system);
        team = new Team(UUID.randomUUID(), "test", 100, new User(UUID.randomUUID(), null));
        req = new Join(team, probe.getRef());
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
        assertEquals(new Join(team, probe.getRef()), req);
    }

    @Test
    public void equalsDifferentUser() {
        assertNotEquals(new Join(new Team(UUID.randomUUID(), null, 1, null), probe.getRef()),
            req);
    }

    @Test
    public void equalsDifferentHandler() {
        assertNotEquals(new Join(team, new JavaTestKit(system).getRef()),
            req);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(team, probe.getRef()), req.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("Join(team=%s, handler=%s)", team, probe.getRef()), req.toString());
    }
}

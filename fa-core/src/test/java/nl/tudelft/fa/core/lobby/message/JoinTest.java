package nl.tudelft.fa.core.lobby.message;

import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import nl.tudelft.fa.core.auth.Credentials;
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
    private User user;
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
        user = new User(UUID.randomUUID(), new Credentials("a", "b"));
        req = new Join(user, probe.getRef());
    }

    @Test
    public void getUser() throws Exception {
        assertEquals(user, req.getUser());
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
        assertEquals(new Join(user, probe.getRef()), req);
    }

    @Test
    public void equalsDifferentUser() {
        assertNotEquals(new Join(new User(UUID.randomUUID(), new Credentials("b", "c")), probe.getRef()),
            req);
    }

    @Test
    public void equalsDifferentHandler() {
        assertNotEquals(new Join(user, new JavaTestKit(system).getRef()),
            req);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(user, probe.getRef()), req.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("Join(user=%s, handler=%s)", user, probe.getRef()), req.toString());
    }
}

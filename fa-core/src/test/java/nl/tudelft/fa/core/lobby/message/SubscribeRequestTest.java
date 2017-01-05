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

public class SubscribeRequestTest {
    private static ActorSystem system;
    private SubscribeRequest req;
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
        req = new SubscribeRequest(probe.getRef());
    }

    @Test
    public void getActor() throws Exception {
        assertEquals(probe.getRef(), req.getActor());
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
        assertEquals(new SubscribeRequest(probe.getRef()), req);
    }

    @Test
    public void equalsDifferentActor() {
        assertNotEquals(new SubscribeRequest(new JavaTestKit(system).getRef()),
            req);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(probe.getRef()), req.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("SubscribeRequest(actor=%s)", probe.getRef()), req.toString());
    }
}

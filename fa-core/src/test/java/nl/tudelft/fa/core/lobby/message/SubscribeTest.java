package nl.tudelft.fa.core.lobby.message;

import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class SubscribeTest {
    private static ActorSystem system;
    private Subscribe req;
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
        req = new Subscribe(probe.getRef());
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
        assertEquals(new Subscribe(probe.getRef()), req);
    }

    @Test
    public void equalsDifferentActor() {
        assertNotEquals(new Subscribe(new JavaTestKit(system).getRef()),
            req);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(probe.getRef()), req.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("Subscribe(actor=%s)", probe.getRef()), req.toString());
    }
}

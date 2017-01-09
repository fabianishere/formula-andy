package nl.tudelft.fa.core.lobby.message;


import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.user.User;
import org.junit.*;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class JoinSuccessTest {
    private static ActorSystem system;
    private JavaTestKit probe;
    private User user;
    private JoinSuccess msg;

    @BeforeClass
    public static void setUpClass() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void tearDownClass() {
        JavaTestKit.shutdownActorSystem(system);
    }

    @Before
    public void setUp() {
        probe = new JavaTestKit(system);
        user = new User(UUID.randomUUID(), new Credentials("fabianishere", "test"));
        msg = new JoinSuccess(user, probe.getRef());
    }

    @Test
    public void testUser() {
        assertEquals(user, msg.getUser());
    }

    @Test
    public void equalsNull() {
        assertThat(msg, not(equalTo(null)));
    }

    @Test
    public void equalsDifferentType() {
        assertThat(msg, not(equalTo("")));
    }

    @Test
    public void equalsReference() {
        assertEquals(msg, msg);
    }

    @Test
    public void equalsData() {
        assertEquals(new JoinSuccess(user, probe.getRef()), msg);
    }

    @Test
    public void equalsDifferentUser() {
        assertNotEquals(new JoinSuccess(new User(UUID.randomUUID(), new Credentials("test", "Test")), probe.getRef()), msg);
    }

    @Test
    public void equalsDifferentLobby() {
        assertNotEquals(new JoinSuccess(user, new JavaTestKit(system).getRef()), msg);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(user, probe.getRef()), msg.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("JoinSuccess(user=%s, lobby=%s)", user, probe.getRef()), msg.toString());
    }
}

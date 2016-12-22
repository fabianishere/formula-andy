package nl.tudelft.fa.core.lobby.message;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.lobby.LobbyConfiguration;
import nl.tudelft.fa.core.lobby.actor.Lobby;
import nl.tudelft.fa.core.lobby.message.LeaveSuccess;
import nl.tudelft.fa.core.user.User;
import org.junit.*;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class LeaveSuccessTest {

    private static ActorSystem system;
    private Props props;
    private User user;
    private ActorRef lobby;
    private LeaveSuccess msg;


    @BeforeClass
    public static void setUpClass() {
        system = ActorSystem.create();
    }

    @Before
    public void setUp() {
        user = new User(UUID.randomUUID(), new Credentials("fabianishere", "test"));
        props = Lobby.props(new LobbyConfiguration(1,  Duration.ZERO));
        lobby = system.actorOf(props, UUID.randomUUID().toString());
        msg = new LeaveSuccess(user, lobby);

    }

    @AfterClass
    public static void tearDownClass() {
        JavaTestKit.shutdownActorSystem(system);
    }

    @Test
    public void testUser() {
        assertEquals(user, msg.getUser());
    }

    @Test
    public void testLobby() {
        assertEquals(lobby, msg.getLobby());
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
        assertEquals(new LeaveSuccess(user, lobby), msg);
    }

    @Test
    public void equalsDifferentUser() {
        assertNotEquals(new LeaveSuccess(new User(UUID.randomUUID(), new Credentials("test", "Test")), lobby), msg);
    }

    @Test
    public void equalsDifferentLobby() {
        assertNotEquals(new LeaveSuccess(user, system.actorOf(props, UUID.randomUUID().toString())), msg);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(user, lobby), msg.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("LeaveSuccess(user=%s, lobby=%s)", user, lobby), msg.toString());
    }
}
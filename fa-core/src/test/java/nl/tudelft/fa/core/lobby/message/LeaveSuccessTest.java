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
    private User user;
    private LeaveSuccess msg;

    @Before
    public void setUp() {
        user = new User(UUID.randomUUID(), new Credentials("fabianishere", "test"));
        msg = new LeaveSuccess(user);

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
        assertEquals(new LeaveSuccess(user), msg);
    }

    @Test
    public void equalsDifferentUser() {
        assertNotEquals(new LeaveSuccess(new User(UUID.randomUUID(), new Credentials("test", "Test"))), msg);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(user), msg.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("LeaveSuccess(user=%s)", user), msg.toString());
    }
}

package nl.tudelft.fa.core.lobby.message;


import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.user.User;
import org.junit.*;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class JoinSuccessTest {
    private User user;
    private JoinSuccess msg;

    @Before
    public void setUp() {
        user = new User(UUID.randomUUID(), new Credentials("fabianishere", "test"));
        msg = new JoinSuccess(user);

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
        assertEquals(new JoinSuccess(user), msg);
    }

    @Test
    public void equalsDifferentUser() {
        assertNotEquals(new JoinSuccess(new User(UUID.randomUUID(), new Credentials("test", "Test"))), msg);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(user), msg.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("JoinSuccess(user=%s)", user), msg.toString());
    }
}

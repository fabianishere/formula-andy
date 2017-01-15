package nl.tudelft.fa.client.lobby.message;

import nl.tudelft.fa.client.user.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class UserJoinedTest {
    private User user;
    private UserJoined event;

    @Before
    public void setUp() throws Exception {
        user = new User(UUID.randomUUID(), "fabianishere");
        event = new UserJoined(user);
    }

    @Test
    public void getUser() throws Exception {
        assertEquals(user, event.getUser());
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
        assertEquals(new UserJoined(user), event);
    }

    @Test
    public void equalsDifferentUser() {
        assertNotEquals(new UserJoined(new User(UUID.randomUUID(), "fabianishere")),
            event);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(user), event.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("UserJoined(user=%s)", user), event.toString());
    }
}
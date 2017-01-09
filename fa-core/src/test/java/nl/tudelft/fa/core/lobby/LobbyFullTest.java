package nl.tudelft.fa.core.lobby;

import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class LobbyFullTest {
    private int users;
    private LobbyFull msg;

    @Before
    public void setUp() {
        users = 1;
        msg = new LobbyFull(users);
    }

    @Test
    public void testUsers() {
        assertEquals(users, msg.getUsers());
    }

    @Test
    public void equalsNull() {
        assertThat(msg, not(equalTo(null)));
    }

    @Test
    public void equalsReference() {
        assertEquals(msg, msg);
    }

    @Test
    public void equalsData() {
        assertEquals(new LobbyFull(users), msg);
    }

    @Test
    public void equalsDifferentUsers() {
        assertNotEquals(new LobbyFull(users + 1), msg);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(users), msg.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("The lobby you are trying to join is full", msg.toString());
    }
}

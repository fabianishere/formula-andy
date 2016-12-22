package nl.tudelft.fa.core.lobby.message;

import nl.tudelft.fa.core.auth.message.InvalidCredentialsError;
import nl.tudelft.fa.core.lobby.message.LobbyFullError;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class LobbyFullErrorTest {
    private int users;
    private LobbyFullError msg;

    @Before
    public void setUp() {
        users = 1;
        msg = new LobbyFullError(users);
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
        assertEquals(new LobbyFullError(1), msg);
    }
}

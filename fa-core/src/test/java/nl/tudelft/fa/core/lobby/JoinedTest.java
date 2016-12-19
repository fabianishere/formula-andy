package nl.tudelft.fa.core.lobby;

import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class JoinedTest {
    private LobbyInformation information;
    private Joined msg;

    @Before
    public void setUp() {
        information = new LobbyInformation(UUID.randomUUID(),
            LobbyStatus.PREPARATION, new LobbyConfiguration(1, Duration.ZERO), Collections.emptySet());
        msg = new Joined(information);
    }

    @Test
    public void testInformation() {
        assertEquals(information, msg.getInformation());
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
        assertEquals(new Joined(information), msg);
    }

    @Test
    public void equalsDifferentInformation() {
        assertNotEquals(new Joined(new LobbyInformation(UUID.randomUUID(),
            LobbyStatus.PREPARATION, new LobbyConfiguration(1, Duration.ZERO), Collections.emptySet())), msg);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(information.hashCode(), msg.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("Joined(information=%s)", information), msg.toString());
    }
}
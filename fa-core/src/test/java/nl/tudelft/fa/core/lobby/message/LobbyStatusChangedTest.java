package nl.tudelft.fa.core.lobby.message;

import nl.tudelft.fa.core.lobby.LobbyStatus;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class LobbyStatusChangedTest {
    LobbyStatus previous;
    LobbyStatus status;
    LobbyStatusChanged event;

    @Before
    public void setUp() throws Exception {
        previous = LobbyStatus.INTERMISSION;
        status = LobbyStatus.PREPARATION;
        event = new LobbyStatusChanged(previous, status);
    }

    @Test
    public void getPrevious() throws Exception {
        assertEquals(previous, event.getPrevious());
    }

    @Test
    public void getStatus() throws Exception {
        assertEquals(status, event.getStatus());
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
        assertEquals(new LobbyStatusChanged(previous, status), event);
    }

    @Test
    public void equalsDifferentPrevious() {
        assertNotEquals(new LobbyStatusChanged(LobbyStatus.PREPARATION, status), event);
    }

    @Test
    public void equalsDifferentStatus() {
        assertNotEquals(new LobbyStatusChanged(previous, LobbyStatus.PROGRESSION), event);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(previous, status), event.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("LobbyStatusChanged(previous=%s, status=%s)", previous, status), event.toString());
    }
}

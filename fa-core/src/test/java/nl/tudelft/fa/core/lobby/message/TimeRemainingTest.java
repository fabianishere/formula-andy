package nl.tudelft.fa.core.lobby.message;

import nl.tudelft.fa.core.lobby.LobbyStatus;
import nl.tudelft.fa.core.team.Team;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class TimeRemainingTest {
    private Duration duration;
    private TimeRemaining event;

    @Before
    public void setUp() {
        duration = Duration.ofMillis(2);
        event = new TimeRemaining(duration);
    }

    @Test
    public void getRemaining() throws Exception {
        assertEquals(duration, event.getRemaining());
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
        assertEquals(new TimeRemaining(duration), event);
    }

    @Test
    public void equalsDifferentDuration() {
        assertNotEquals(new TimeRemaining(Duration.ZERO),
            event);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(duration), event.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("TimeRemaining(remaining=%s)", duration), event.toString());
    }
}

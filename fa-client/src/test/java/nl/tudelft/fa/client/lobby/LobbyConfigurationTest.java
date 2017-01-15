package nl.tudelft.fa.client.lobby;

import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class LobbyConfigurationTest {
    private int maxPlayers;
    private Duration preparationTime;
    private LobbyConfiguration configuration;

    @Before
    public void setUp() {
        maxPlayers = 1;
        preparationTime = Duration.ofMinutes(2);
        configuration = new LobbyConfiguration(maxPlayers, preparationTime);
    }

    @Test
    public void testMaxPlayers() {
        assertEquals(maxPlayers, configuration.getPlayerMaximum());
    }

    @Test
    public void testPreparationTime() {
        assertEquals(preparationTime, configuration.getPreparationTime());
    }

    @Test
    public void equalsNull() {
        assertThat(configuration, not(equalTo(null)));
    }

    @Test
    public void equalsDifferentType() {
        assertThat(configuration, not(equalTo("")));
    }

    @Test
    public void equalsReference() {
        assertEquals(configuration, configuration);
    }

    @Test
    public void equalsData() {
        assertEquals(new LobbyConfiguration(maxPlayers, preparationTime), configuration);
    }

    @Test
    public void equalsDifferentMaxPlayers() {
        assertNotEquals(new LobbyConfiguration(maxPlayers + 1, preparationTime), configuration);
    }

    @Test
    public void equalsDifferentPreparationTime() {
        assertNotEquals(new LobbyConfiguration(maxPlayers, preparationTime.plusMinutes(5)), configuration);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(maxPlayers, preparationTime), configuration.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals( String.format("LobbyConfiguration(maxPlayers=%d, preparationTime=%s)",
            maxPlayers, preparationTime), configuration.toString());
    }
}

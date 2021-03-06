package nl.tudelft.fa.client.lobby;

import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;


public class LobbyConfigurationTest {
    private int maxUsers;
    private Duration intermission;
    private Duration preparation;
    private LobbyConfiguration configuration;

    @Before
    public void setUp() {
        maxUsers = 1;
        intermission = Duration.ofMinutes(2);
        preparation = Duration.ofMinutes(2);
        configuration = new LobbyConfiguration(maxUsers, intermission, preparation);
    }

    @Test
    public void testMaxTeams() {
        assertEquals(maxUsers, configuration.getTeamMaximum());
    }

    @Test
    public void testIntermissionTime() {
        assertEquals(intermission, configuration.getIntermission());
    }

    @Test
    public void testPreparationTime() {
        assertEquals(preparation, configuration.getPreparation());
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
        assertEquals(new LobbyConfiguration(maxUsers, intermission, preparation), configuration);
    }

    @Test
    public void equalsDifferentMaxUsers() {
        assertNotEquals(new LobbyConfiguration(maxUsers + 1, intermission, preparation), configuration);
    }

    @Test
    public void equalsDifferentIntermissionTime() {
        assertNotEquals(new LobbyConfiguration(maxUsers, intermission, preparation.plusMinutes(5)), configuration);
    }

    @Test
    public void equalsDifferentPreparationTime() {
        assertNotEquals(new LobbyConfiguration(maxUsers, intermission.plusMinutes(5), preparation), configuration);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(maxUsers, intermission, preparation), configuration.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("LobbyConfiguration(teamMaximum=%d, intermission=%s, preparation=%s)",
            maxUsers, intermission, preparation), configuration.toString());
    }
}

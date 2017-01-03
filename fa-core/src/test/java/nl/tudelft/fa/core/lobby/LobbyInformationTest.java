package nl.tudelft.fa.core.lobby;

import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.user.User;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class LobbyInformationTest {
    private LobbyStatus status;
    private LobbyConfiguration configuration;
    private Set<User> users;
    private LobbyInformation information;

    @Before
    public void setUp() {
        status = LobbyStatus.PREPARATION;
        configuration = new LobbyConfiguration(11, Duration.ofMinutes(5));
        users = new HashSet<>();
        users.add(new User(UUID.randomUUID(), new Credentials("fabianishere", "test")));
        information = new LobbyInformation(status, configuration, users);
    }

    @Test
    public void testStatus() {
        assertEquals(status, information.getStatus());
    }

    @Test
    public void testConfiguration() {
        assertEquals(configuration, information.getConfiguration());
    }

    @Test
    public void testUsers() {
        assertEquals(users, information.getUsers());
    }

    @Test
    public void equalsNull() {
        assertThat(information, not(equalTo(null)));
    }

    @Test
    public void equalsDifferentType() {
        assertThat(information, not(equalTo("")));
    }

    @Test
    public void equalsReference() {
        assertEquals(information, information);
    }

    @Test
    public void equalsData() {
        assertEquals(new LobbyInformation(status, configuration, users), information);
    }

    @Test
    public void equalsDifferentStatus() {
        assertNotEquals(new LobbyInformation(LobbyStatus.IN_PROGRESS, configuration, users), information);
    }

    @Test
    public void equalsDifferentConfiguration() {
        assertNotEquals(new LobbyInformation(status, new LobbyConfiguration(0, Duration.ZERO), users), information);
    }

    @Test
    public void equalsDifferentUsers() {
        assertNotEquals(new LobbyInformation(status, configuration, Collections.emptySet()), information);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(status, configuration, users), information.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("LobbyInformation(status=%s, configuration=%s, users=%d)",
            status, configuration, users.size()), information.toString());
    }
}

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
    private UUID id;
    private LobbyStatus status;
    private LobbyConfiguration configuration;
    private List<User> users;
    private LobbyInformation information;

    @Before
    public void setUp() {
        id = UUID.randomUUID();
        status = LobbyStatus.PREPARATION;
        configuration = new LobbyConfiguration(11, Duration.ofMinutes(5));
        users = new ArrayList<>();
        users.add(new User(UUID.randomUUID(), new Credentials("fabianishere", "test")));
        information = new LobbyInformation(id, status, configuration, users);
    }

    @Test
    public void testId() {
        assertEquals(id, information.getId());
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
    public void equalsReference() {
        assertEquals(information, information);
    }

    @Test
    public void equalsData() {
        assertEquals(new LobbyInformation(id, status, configuration, users), information);
    }

    @Test
    public void equalsDifferentId() {
        assertNotEquals(new LobbyInformation(UUID.randomUUID(), status, configuration, users), information);
    }

    @Test
    public void equalsDifferentStatus() {
        assertNotEquals(new LobbyInformation(id, LobbyStatus.IN_PROGRESS, configuration, users), information);
    }

    @Test
    public void equalsDifferentConfiguration() {
        assertNotEquals(new LobbyInformation(id, status, new LobbyConfiguration(0, Duration.ZERO), users), information);
    }

    @Test
    public void equalsDifferentUsers() {
        assertNotEquals(new LobbyInformation(id, status, configuration, Collections.emptyList()), information);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(id, status, configuration, users), information.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("LobbyInformation(id=%s, status=%s, configuration=%s, users=%d)",
            id, status, configuration, users.size()), information.toString());
    }
}

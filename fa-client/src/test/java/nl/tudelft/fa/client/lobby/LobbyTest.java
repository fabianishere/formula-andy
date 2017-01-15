package nl.tudelft.fa.client.lobby;

import nl.tudelft.fa.client.auth.Credentials;
import nl.tudelft.fa.client.user.User;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class LobbyTest {
    private String id;
    private LobbyStatus status;
    private LobbyConfiguration configuration;
    private Set<User> users;
    private Lobby information;

    @Before
    public void setUp() {
        id = UUID.randomUUID().toString();
        status = LobbyStatus.PREPARATION;
        configuration = new LobbyConfiguration(11, Duration.ofMinutes(5));
        users = new HashSet<>();
        users.add(new User(UUID.randomUUID(), "fabianishere"));
        information = new Lobby(id, status, configuration, users);
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
    public void equalsDifferentType() {
        assertThat(information, not(equalTo("")));
    }

    @Test
    public void equalsReference() {
        assertEquals(information, information);
    }

    @Test
    public void equalsData() {
        assertEquals(new Lobby(id, status, configuration, users), information);
    }

    @Test
    public void equalsDifferentId() {
        assertNotEquals(new Lobby(UUID.randomUUID().toString(), status, configuration, users), information);
    }


    @Test
    public void equalsDifferentStatus() {
        assertNotEquals(new Lobby(id, LobbyStatus.IN_PROGRESS, configuration, users), information);
    }

    @Test
    public void equalsDifferentConfiguration() {
        assertNotEquals(new Lobby(id, status, new LobbyConfiguration(0, Duration.ZERO), users), information);
    }

    @Test
    public void equalsDifferentUsers() {
        assertNotEquals(new Lobby(id, status, configuration, Collections.emptySet()), information);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(id, status, configuration, users), information.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("Lobby(id=%s, status=%s, configuration=%s, users=%d)",
            id, status, configuration, users.size()), information.toString());
    }
}

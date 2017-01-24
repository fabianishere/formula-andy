package nl.tudelft.fa.client.lobby;

import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class LobbyBalancerTest {
    private Set<Lobby> lobbies;
    private LobbyBalancer information;

    @Before
    public void setUp() {
        lobbies = new HashSet<>();
        information = new LobbyBalancer(lobbies);
    }

    @Test
    public void testLobbies() {
        assertEquals(lobbies, information.getLobbies());
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
        assertEquals(new LobbyBalancer(lobbies), information);
    }

    @Test
    public void equalsDifferentLobbies() {
        assertNotEquals(new LobbyBalancer(new HashSet<Lobby>() {{
            add(new Lobby(UUID.randomUUID().toString(), LobbyStatus.PREPARATION, new LobbyConfiguration(1, Duration.ZERO, Duration.ZERO), Collections.emptySet(), Collections.emptyList()));
        }}), information);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(lobbies), information.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("LobbyBalancer(lobbies=%s)", lobbies), information.toString());
    }
}

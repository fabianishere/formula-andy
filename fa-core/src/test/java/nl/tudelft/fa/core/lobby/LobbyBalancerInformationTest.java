package nl.tudelft.fa.core.lobby;

import akka.actor.ActorRef;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class LobbyBalancerInformationTest {
    private Map<ActorRef, LobbyInformation> lobbies;
    private LobbyBalancerInformation information;

    @Before
    public void setUp() {
        lobbies = new HashMap<>();
        information = new LobbyBalancerInformation(lobbies);
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
        assertEquals(new LobbyBalancerInformation(lobbies), information);
    }

    @Test
    public void equalsDifferentLobbies() {
        assertNotEquals(new LobbyBalancerInformation(new HashMap<ActorRef, LobbyInformation>() {{
            put(null, new LobbyInformation(LobbyStatus.PREPARATION, new LobbyConfiguration(1, Duration.ZERO), Collections.emptySet()));
        }}), information);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(lobbies), information.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("LobbyBalancerInformation(lobbies=%s)", lobbies), information.toString());
    }
}

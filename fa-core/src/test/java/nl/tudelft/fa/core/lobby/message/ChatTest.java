package nl.tudelft.fa.core.lobby.message;

import nl.tudelft.fa.core.team.Team;
import nl.tudelft.fa.core.user.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class ChatTest {
    private Team team;
    private String message;
    private Chat chat;

    @Before
    public void setUp() throws Exception {
        team = new Team(UUID.randomUUID(), "test", 100, new User(UUID.randomUUID(), null));
        message = "test";
        chat = new Chat(team, message);
    }

    @Test
    public void getTeam() throws Exception {
        assertEquals(team, chat.getTeam());
    }

    @Test
    public void getMessage() throws Exception {
        assertEquals(message, chat.getMessage());
    }

    @Test
    public void equalsDifferentType() {
        assertThat(chat, not(equalTo("")));
    }

    @Test
    public void equalsNull() {
        assertThat(chat, not(equalTo(null)));
    }

    @Test
    public void equalsReference() {
        assertEquals(chat, chat);
    }

    @Test
    public void equalsData() {
        assertEquals(new Chat(team, message), chat);
    }

    @Test
    public void equalsDifferentTeam() {
        assertNotEquals(new Chat(new Team(UUID.randomUUID(), null, 1, null), message),
            chat);
    }

    @Test
    public void equalsDifferentHandler() {
        assertNotEquals(new Chat(team, ""), chat);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(team, message), chat.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("Chat(team=%s, message=%s)", team, message), chat.toString());
    }
}

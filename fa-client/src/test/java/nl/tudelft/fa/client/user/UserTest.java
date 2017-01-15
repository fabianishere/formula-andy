package nl.tudelft.fa.client.user;

import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.user.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class UserTest {
    UUID id;
    String username;
    User user;

    @Before
    public void setUp() throws Exception {
        id = UUID.randomUUID();
        username = "fabianishere";
        user = new User(id, username);
    }

    @Test
    public void getId() throws Exception {
        assertEquals(id, user.getId());
    }

    @Test
    public void getUsername() throws Exception {
        assertEquals(username, user.getUsername());
    }

    @Test
    public void equalsNull() {
        assertThat(user, not(equalTo(null)));
    }

    @Test
    public void equalsDifferentType() {
        assertThat(user, not(equalTo("")));
    }

    @Test
    public void equalsReference() {
        assertEquals(user, user);
    }

    @Test
    public void equalsData() {
        assertEquals(new User(id, username), user);
    }

    @Test
    public void equalsDifferentId() {
        assertThat(user, not(equalTo(new User(UUID.randomUUID(), username))));
    }

    @Test
    public void equalsOtherPropertiesHaveNoEffect() {
        assertEquals(new User(id, "christovs"), user);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(id), user.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("User(id=%s, username=%s)", id, username), user.toString());
    }

}

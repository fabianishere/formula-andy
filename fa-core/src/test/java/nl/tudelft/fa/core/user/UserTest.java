package nl.tudelft.fa.core.user;

import nl.tudelft.fa.core.auth.Credentials;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class UserTest {
    UUID id;
    Credentials credentials;
    User user;

    @Before
    public void setUp() throws Exception {
        id = UUID.randomUUID();
        credentials = new Credentials("ChristovS", "lolol");
        user = new User(id, credentials);
    }

    @Test
    public void getId() throws Exception {
        assertEquals(id, user.getId());
    }

    @Test
    public void getCredentials() throws Exception {
        assertEquals(credentials, user.getCredentials());
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
        assertEquals(new User(id, credentials), user);
    }

    @Test
    public void equalsDifferentId() {
        assertThat(user, not(equalTo(new User(UUID.randomUUID(), credentials))));
    }

    @Test
    public void equalsOtherPropertiesHaveNoEffect() {
        assertEquals(new User(id, new Credentials("fabianishere", "test")), user);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(id), user.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("User(id=%s, username=%s)", id, credentials.getUsername()), user.toString());
    }

}

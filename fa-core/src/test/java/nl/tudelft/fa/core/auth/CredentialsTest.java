package nl.tudelft.fa.core.auth;

import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class CredentialsTest {
    String username;
    String password;
    Credentials credentials;

    @Before
    public void setUp() throws Exception {
        username = "ChristovS";
        password = "ILikePonies";
        credentials = new Credentials(username, password);
    }

    @Test
    public void getUsername() throws Exception {
        assertEquals(username, credentials.getUsername());
    }

    @Test
    public void getPassword() throws Exception {
        assertEquals(password, credentials.getPassword());
    }

    @Test
    public void equalsNull() {
        assertThat(credentials, not(equalTo(null)));
    }

    @Test
    public void equalsDifferentType() {
        assertThat(credentials, not(equalTo("")));
    }

    @Test
    public void equalsReference() {
        assertEquals(credentials, credentials);
    }

    @Test
    public void equalsData() {
        assertEquals(new Credentials(username, password), credentials);
    }

    @Test
    public void equalsDifferentUsername() {
        assertNotEquals(new Credentials(username + "s", password), credentials);
    }

    @Test
    public void equalsDifferentPassword() {
        assertNotEquals(new Credentials(username, password + "s"), credentials);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(username, password), credentials.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("Credentials(username=%s, password=%s)", username, password), credentials.toString());
    }

}

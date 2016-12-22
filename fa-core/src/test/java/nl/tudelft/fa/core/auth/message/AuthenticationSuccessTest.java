package nl.tudelft.fa.core.auth.message;

import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.user.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

/**
 * @author Fabian Mastenbroek <mail.fabianm@gmail.com>
 */
public class AuthenticationSuccessTest {
    User user;
    AuthenticationSuccess res;

    @Before
    public void setUp() throws Exception {
        user = new User(UUID.randomUUID(), new Credentials("a", "b"));
        res = new AuthenticationSuccess(user);
    }

    @Test
    public void getUser() throws Exception {
        assertEquals(user, res.getUser());
    }
    @Test
    public void equalsNull() {
        assertThat(res, not(equalTo(null)));
    }

    @Test
    public void equalsReference() {
        assertEquals(res, res);
    }

    @Test
    public void equalsData() {
        assertEquals(new AuthenticationSuccess(user), res);
    }

    @Test
    public void equalsDifferentCredentials() {
        assertNotEquals(new AuthenticationSuccess(new User(UUID.randomUUID(), new Credentials("b", "c"))),
            res);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(user), res.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("AuthenticationSuccess(user=%s)", user), user.toString());
    }
}

package nl.tudelft.fa.core.lobby.message;

import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.user.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class LeaveTest {
    User user;
    Leave req;

    @Before
    public void setUp() throws Exception {
        user = new User(UUID.randomUUID(), new Credentials("a", "b"));
        req = new Leave(user);
    }

    @Test
    public void getUser() throws Exception {
        assertEquals(user, req.getUser());
    }

    @Test
    public void equalsDifferentType() {
        assertThat(req, not(equalTo("")));
    }

    @Test
    public void equalsNull() {
        assertThat(req, not(equalTo(null)));
    }

    @Test
    public void equalsReference() {
        assertEquals(req, req);
    }

    @Test
    public void equalsData() {
        assertEquals(new Leave(user), req);
    }

    @Test
    public void equalsDifferentCredentials() {
        assertNotEquals(new Leave(new User(UUID.randomUUID(), new Credentials("b", "c"))),
            req);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(user), req.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("Leave(user=%s)", user), req.toString());
    }
}

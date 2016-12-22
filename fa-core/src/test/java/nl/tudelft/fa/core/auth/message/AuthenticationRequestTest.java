package nl.tudelft.fa.core.auth.message;

import nl.tudelft.fa.core.auth.Credentials;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class AuthenticationRequestTest {
    Credentials credentials;
    AuthenticationRequest request;

    @Before
    public void setUp() throws Exception {
        credentials = new Credentials("a", "b");
        request = new AuthenticationRequest(credentials);
    }

    @Test
    public void getCredentials() throws Exception {
        assertEquals(credentials, request.getCredentials());
    }

    @Test
    public void equalsNull() {
        assertThat(request, not(equalTo(null)));
    }

    @Test
    public void equalsReference() {
        assertEquals(request, request);
    }

    @Test
    public void equalsData() {
        assertEquals(new AuthenticationRequest(credentials), request);
    }

    @Test
    public void equalsDifferentCredentials() {
        assertNotEquals(new AuthenticationRequest(new Credentials("b", "c")),
            request);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(Objects.hash(credentials), request.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("AuthenticationRequest(credentials=%s)", credentials), request.toString());
    }
}

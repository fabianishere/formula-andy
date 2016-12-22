package nl.tudelft.fa.core.lobby.message;

import nl.tudelft.fa.core.auth.message.InvalidCredentialsError;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class NotInLobbyErrorTest {
    NotInLobbyError error;

    @Before
    public void setUp() {
        error = new NotInLobbyError();
    }

    @Test
    public void equalsNull() {
        assertThat(error, not(equalTo(null)));
    }

    @Test
    public void equalsReference() {
        assertEquals(error, error);
    }

    @Test
    public void equalsData() {
        assertEquals(new NotInLobbyError(""), error);
    }
}

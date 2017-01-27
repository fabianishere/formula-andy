package nl.tudelft.fa.core.auth.message;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Fabian Mastenbroek <mail.fabianm@gmail.com>
 */
public class InvalidCredentialsErrorTest {
    InvalidCredentialsError error;

    @Before
    public void setUp() {
        error = new InvalidCredentialsError();
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
        assertEquals(new InvalidCredentialsError(""), error);
    }
}

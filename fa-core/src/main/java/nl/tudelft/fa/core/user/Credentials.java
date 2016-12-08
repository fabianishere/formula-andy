package nl.tudelft.fa.core.user;

import java.util.Objects;

/**
 * This class represents the credentials of a user.
 *
 * @author Fabian Mastenbroek
 */
public class Credentials {
    /**
     * The username of the user.
     */
    private String username;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * Construct a {@link Credentials} instance.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     */
    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Test whether this {@link Credentials} is equal to the given object.
     *
     * @param other The object to be tested for equality
     * @return <code>true</code> if both objects are equal, <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Credentials) {
            Credentials that = (Credentials) other;
            return this.username.equals(that.username) && this.password.equals(that.password);
        }
        return false;
    }

    /**
     * Return the hash code of this object.
     *
     * @return The hash code of this object as integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    /**
     * Return a string representation of this team.
     *
     * @return A string representation of this team.
     */
    @Override
    public String toString() {
        return String.format("Credentials(username=%s, password=%s)", username, password);
    }
}

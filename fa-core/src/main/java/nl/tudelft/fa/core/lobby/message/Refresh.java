package nl.tudelft.fa.core.lobby.message;

import nl.tudelft.fa.core.lobby.actor.LobbyBalancerActor;

/**
 * This message is used internally used by the {@link LobbyBalancerActor} to refresh its caches.
 *
 * @author Fabian Mastenbroek
 */
public final class Refresh {
    /**
     * The static instance of this class.
     */
    public static final Refresh INSTANCE = new Refresh();

    /**
     * Return a string representation of this message.
     *
     * @return A string representation of this message.
     */
    @Override
    public String toString() {
        return "Refresh";
    }
}

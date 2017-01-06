package nl.tudelft.fa.core.lobby.message;

import nl.tudelft.fa.core.lobby.actor.LobbyBalancer;

/**
 * This message is used internally used by the {@link LobbyBalancer} to refresh its caches.
 *
 * @author Fabian Mastenbroek
 */
public final class Refresh {
    /**
     * The static instance of this class.
     */
    public static final Refresh INSTANCE = new Refresh();
}

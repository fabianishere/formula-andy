package nl.tudelft.fa.core.lobby.message;

import akka.actor.ActorRef;
import nl.tudelft.fa.core.lobby.actor.Lobby;

/**
 * This class represents a {@link JoinException} that occurred specifically in the {@link Lobby}.
 *
 * @author Fabian Mastenbroek
 */
public abstract class JoinException extends LobbyException {
    /**
     * Construct a {@link JoinException} instance.
     *
     * @param lobby The lobby where the error occurred.
     * @param message The message of the error.
     */
    public JoinException(ActorRef lobby, String message) {
        super(lobby, message);
    }

    /**
     * Construct a {@link JoinException} instance.
     *
     * @param lobby The lobby where the error occurred.
     */
    public JoinException(ActorRef lobby) {
        super(lobby);
    }
}

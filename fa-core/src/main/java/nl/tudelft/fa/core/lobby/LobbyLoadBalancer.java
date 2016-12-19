package nl.tudelft.fa.core.lobby;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import akka.pattern.Patterns;
import akka.pattern.PatternsCS;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * This actor acts as a load balancer between multiple {@link Lobby} actors and
 * spins up new lobbies on the fly if needed.
 *
 * @author Fabian Mastenbroek
 */
public class LobbyLoadBalancer extends AbstractActor {
    /**
     * The configuration of the lobbies to spin up.
     */
    private LobbyConfiguration configuration;

    /**
     * The {@link LoggingAdapter} of this class.
     */
    private final LoggingAdapter log = Logging.getLogger(context().system(), this);

    /**
     * The instances this load balancer has spun up.
     */
    private List<ActorRef> instances = new ArrayList<>();

    /**
     * Construct a {@link LobbyLoadBalancer} instance.
     *
     * @param configuration The configuration of the lobbies.
     */
    private LobbyLoadBalancer(LobbyConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * This method defines the initial actor behavior, it must return a partial function with the
     * actor logic.
     *
     * @return The initial actor behavior as a partial function.
     */
    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return ReceiveBuilder
            .match(Inform.class, this::inform)
            .match(Join.class, this::join)
            .build();
    }

    /**
     * Inform the sender about the {@link Lobby} actors provided by
     * this load balancer.
     *
     * @param req The inform request.
     */
    private void inform(Inform req) {

    }

    /**
     * Handle a {@link Join} request from a user.
     *
     * @param req The {@link Join} request to handle.
     */
    private void join(Join req) {
    }

    /**
     * Create {@link Props} instance for an actor of this type.
     *
     * @param configuration The configuration of the lobby.
     * @return A Props for creating this actor, which can then be further configured
     *         (e.g. calling `.withDispatcher()` on it)
     */
    public static Props props(LobbyConfiguration configuration) {
        return Props.create(LobbyLoadBalancer.class, () -> new LobbyLoadBalancer(configuration));
    }
}

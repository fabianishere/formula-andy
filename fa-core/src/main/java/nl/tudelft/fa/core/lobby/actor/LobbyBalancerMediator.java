package nl.tudelft.fa.core.lobby.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import nl.tudelft.fa.core.lobby.message.JoinError;
import nl.tudelft.fa.core.lobby.message.JoinRequest;
import nl.tudelft.fa.core.lobby.message.JoinSuccess;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * This actor handles the joining process between a balancer and a lobby.
 *
 * @author Fabian Mastenbroek
 */
public class LobbyBalancerMediator extends AbstractActor {
    /**
     * The {@link LobbyBalancer} that created this mediator.
     */
    private ActorRef balancer;

    /**
     * The {@link JoinRequest} this mediator handles.
     */
    private JoinRequest req;

    /**
     * Construct a {@link LobbyBalancerMediator} instance.
     *
     * @param balancer The {@link LobbyBalancer} that created this mediator.
     * @param req The join request this mediator handles.
     */
    private LobbyBalancerMediator(ActorRef balancer, JoinRequest req) {
        this.balancer = balancer;
        this.req = req;
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
            .match(JoinSuccess.class, this::success)
            .match(JoinError.class, this::failure)
            .build();
    }

    /**
     * This method is invoked whenever the joining process ended successfully.
     *
     * @param res The {@link JoinSuccess} response from the lobby.
     */
    private void success(JoinSuccess res) {
        req.getHandler().tell(res, sender());
        context().stop(self());
    }

    /**
     * This method is invoked whenever the joining process ended in a failure.
     *
     * @param error The error that occurred.
     */
    private void failure(JoinError error) {
        // Resend the request. TODO: ask the balancer to refresh via a RefreshRequest message
        balancer.tell(req, req.getHandler());
        context().stop(self());
    }

    /**
     * Create {@link Props} instance for an actor of this type.
     *
     * @param balancer The lobby balancer that created this mediator.
     * @param req The join request to handle.
     * @return A Props for creating this actor, which can then be further configured
     *         (e.g. calling `.withDispatcher()` on it)
     */
    public static Props props(ActorRef balancer, JoinRequest req) {
        return Props.create(LobbyBalancerMediator.class,
            () -> new LobbyBalancerMediator(balancer, req));
    }
}

package nl.tudelft.fa.core.lobby.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import nl.tudelft.fa.core.lobby.message.*;
import scala.PartialFunction;
import scala.concurrent.duration.FiniteDuration;
import scala.runtime.BoxedUnit;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * An actor that counts down a finite duration of time and informs the bus about the remaining
 * time at a given interval.
 *
 * @author Fabian Mastenbroek
 */
public class LobbyCountdownActor extends AbstractActor {
    /**
     * The bus to send the messages to.
     */
    private final ActorRef bus;

    /**
     * The total time before the countdown is complete.
     */
    private final Duration duration;

    /**
     * The interval at which the informational messages take place.
     */
    private final Duration interval;

    /**
     * The previous snapshot of the countdown.
     */
    private TimeRemaining snapshot;

    /**
     * The cancellable timer.
     */
    private Cancellable timer;

    /**
     * Construct a {@link LobbyCountdownActor}.
     *
     * @param bus The bus to send the messages to.
     * @param duration The total time before the countdown is complete.
     * @param interval The interval at which the informational messages take place.
     */
    public LobbyCountdownActor(ActorRef bus, Duration duration, Duration interval) {
        this.bus = bus;
        this.duration = duration;
        this.interval = interval;
        this.snapshot = new TimeRemaining(duration);
    }

    /**
     * This method is invoked before the start of this actor.
     */
    @Override
    public void preStart() {
        final FiniteDuration interval = FiniteDuration.create(this.interval.toNanos(),
            TimeUnit.NANOSECONDS);

        // Schedule the timer
        timer = context().system().scheduler().schedule(FiniteDuration.Zero(), interval, self(),
            "tick", context().dispatcher(), self());
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
            .matchEquals("tick", msg -> {
                snapshot = new TimeRemaining(snapshot.getRemaining().minus(interval));

                // if the duration is negative, the countdown is over
                if (snapshot.getRemaining().isNegative()) {
                    timer.cancel();
                    context().stop(self());
                    return;
                }

                bus.tell(snapshot, self());
            })
            .build();
    }

    /**
     * Create {@link Props} instance for an actor of this type.
     *
     * @param bus The bus to send the messages to.
     * @param duration The total time before the countdown is complete.
     * @param interval The interval at which the informational messages take place.
     * @return A Props for creating this actor, which can then be further configured
     *         (e.g. calling `.withDispatcher()` on it)
     */
    public static Props props(ActorRef bus, Duration duration, Duration interval) {
        return Props.create(LobbyCountdownActor.class,
            () -> new LobbyCountdownActor(bus, duration, interval));
    }
}

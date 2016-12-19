package nl.tudelft.fa.core.lobby;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.testkit.JavaTestKit;
import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.user.User;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.Await;

import java.time.Duration;
import java.util.UUID;

import static org.junit.Assert.*;

public class LobbyRouterTest {
    private static ActorSystem system;
    private LobbyConfiguration configuration;
    private User user;

    @BeforeClass
    public static void setUpClass() {
        system = ActorSystem.create();
    }

    @Before
    public void setUp() {
        configuration = new LobbyConfiguration(1, Duration.ofMinutes(2));
        user = new User(UUID.randomUUID(), new Credentials("fabianishere", "test"));
    }


    @AfterClass
    public static void tearDownClass() {
        JavaTestKit.shutdownActorSystem(system);
    }


    @Test
    public void testJoinNewSuccess() {
        new JavaTestKit(system) {
            {
                final Props props = LobbyRouter.props(configuration);
                final ActorRef subject = system.actorOf(props);
                final Join req = new Join(user);

                subject.tell(req, getRef());

                // await the correct response
                expectMsgClass(duration("1 second"), Joined.class);
            }
        };
    }

    @Test
    public void testJoinExisting() throws Exception {
        new JavaTestKit(system) {
            {
                final Props props = LobbyRouter.props(new LobbyConfiguration(2, Duration.ofMinutes(1)));
                final ActorRef subject = system.actorOf(props);
                final Join reqA = new Join(new User(UUID.randomUUID(), new Credentials("Test", "test")));
                final Join reqB = new Join(user);

                Joined resA = (Joined) Await.result(Patterns.ask(subject, reqA, 1000), duration("1 second"));
                Joined resB = (Joined) Await.result(Patterns.ask(subject, reqB, 1000), duration("1 second"));
                assertEquals(resA.getInformation().getId(), resB.getInformation().getId());
            }
        };
    }

    @Test
    public void testJoinDifferent() throws Exception {
        new JavaTestKit(system) {
            {
                final Props props = LobbyRouter.props(configuration);
                final ActorRef subject = system.actorOf(props);
                final Join req = new Join(user);

                Joined resA = (Joined) Await.result(Patterns.ask(subject, req, 1000), duration("1 second"));
                Joined resB = (Joined) Await.result(Patterns.ask(subject, req, 1000), duration("1 second"));

                // await the correct response
                assertNotEquals(resA.getInformation().getId(), resB.getInformation().getId());
            }
        };
    }
}

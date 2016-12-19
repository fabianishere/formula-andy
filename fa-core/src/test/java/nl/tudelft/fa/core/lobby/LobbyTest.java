package nl.tudelft.fa.core.lobby;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;

import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.user.User;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

public class LobbyTest {
    private static ActorSystem system;
    private UUID id;
    private LobbyConfiguration configuration;
    private User user;

    @BeforeClass
    public static void setUpClass() {
        system = ActorSystem.create();
    }

    @Before
    public void setUp() {
        id = UUID.randomUUID();
        configuration = new LobbyConfiguration(1, Duration.ofMinutes(2));
        user = new User(UUID.randomUUID(), new Credentials("fabianishere", "test"));
    }


    @AfterClass
    public static void tearDownClass() {
        JavaTestKit.shutdownActorSystem(system);
    }

    @Test
    public void testInform() {
        new JavaTestKit(system) {
            {
                final Props props = Lobby.props(configuration);
                final ActorRef subject = system.actorOf(props, id.toString());
                final Inform req = new Inform();

                subject.tell(req, getRef());

                // await the correct response
                expectMsgEquals(duration("1 second"), new LobbyInformation(id,
                    LobbyStatus.PREPARATION, configuration, Collections.emptySet()));
            }
        };
    }

    @Test
    public void testJoinSuccess() {
        new JavaTestKit(system) {
            {
                final Props props = Lobby.props(configuration);
                final ActorRef subject = system.actorOf(props, id.toString());
                final Join req = new Join(user);

                subject.tell(req, getRef());

                // await the correct response
                expectMsgEquals(duration("1 second"), new Joined(new LobbyInformation(id,
                    LobbyStatus.PREPARATION, configuration, Collections.singleton(user))));
            }
        };
    }

    @Test
    public void testJoinFull() {
        new JavaTestKit(system) {
            {
                final Props props = Lobby.props(new LobbyConfiguration(0, Duration.ZERO));
                final ActorRef subject = system.actorOf(props, id.toString());
                final Join req = new Join(user);

                subject.tell(req, getRef());

                // await the correct response
                expectMsgEquals(duration("1 second"), new LobbyFull(0));
            }
        };
    }

    @Test
    public void testLeaveSuccess() {
        new JavaTestKit(system) {
            {
                final Props props = Lobby.props(configuration);
                final ActorRef subject = system.actorOf(props, id.toString());
                final Leave req = new Leave();

                subject.tell(new Join(user), getRef());
                expectMsgClass(duration("1 second"), Joined.class);

                subject.tell(req, getRef());
                expectMsgEquals(duration("1 second"), new Left(user, subject));
            }
        };
    }

}

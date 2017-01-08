package nl.tudelft.fa.core.lobby.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;

import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.lobby.LobbyConfiguration;
import nl.tudelft.fa.core.lobby.Lobby;
import nl.tudelft.fa.core.lobby.LobbyStatus;
import nl.tudelft.fa.core.lobby.message.*;
import nl.tudelft.fa.core.user.User;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

public class LobbyActorTest {
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
        configuration = new LobbyConfiguration(2, Duration.ofMinutes(2));
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
                final Props props = LobbyActor.props(configuration);
                final ActorRef subject = system.actorOf(props, id.toString());
                final RequestInformation req = RequestInformation.INSTANCE;

                subject.tell(req, getRef());

                // await the correct response
                expectMsgEquals(duration("1 second"), new Lobby(
                    subject.path().name(), LobbyStatus.PREPARATION, configuration, Collections.emptySet()));
            }
        };
    }

    @Test
    public void testJoinSuccess() {
        new JavaTestKit(system) {
            {
                final Props props = LobbyActor.props(configuration);
                final ActorRef subject = system.actorOf(props, id.toString());
                final Join req = new Join(user, getRef());

                subject.tell(req, getRef());

                // await the correct response
                expectMsgClass(duration("1 second"), JoinSuccess.class);
            }
        };
    }

    @Test
    public void testJoinSuccessDifferentHandler() {
        new JavaTestKit(system) {
            {
                final Props props = LobbyActor.props(configuration);
                final ActorRef subject = system.actorOf(props, id.toString());
                final JavaTestKit probe = new JavaTestKit(system);
                final Join req = new Join(user, probe.getRef());

                subject.tell(req, getRef());
                probe.expectNoMsg();
                expectMsgClass(duration("1 second"), JoinSuccess.class);
            }
        };
    }

    @Test
    public void testJoinSuccessDifferentHandlerSubscribed() {
        new JavaTestKit(system) {
            {
                final Props props = LobbyActor.props(configuration);
                final ActorRef subject = system.actorOf(props, id.toString());
                final JavaTestKit probe = new JavaTestKit(system);
                final Join req = new Join(user, probe.getRef());

                subject.tell(new Subscribe(probe.getRef()), probe.getRef());
                subject.tell(req, getRef());
                probe.expectMsgEquals(duration("1 second"), new UserJoined(user));
                expectMsgClass(duration("1 second"), JoinSuccess.class);
            }
        };
    }

    @Test
    public void testJoinFull() {
        new JavaTestKit(system) {
            {
                final Props props = LobbyActor.props(new LobbyConfiguration(0, Duration.ZERO));
                final ActorRef subject = system.actorOf(props, id.toString());
                final Join req = new Join(user, getRef());

                subject.tell(req, getRef());

                // await the correct response
                expectMsgClass(duration("1 second"), LobbyFullException.class);
            }
        };
    }

    @Test
    public void testLeaveSuccess() {
        new JavaTestKit(system) {
            {
                final Props props = LobbyActor.props(configuration);
                final ActorRef subject = system.actorOf(props, id.toString());
                final Leave req = new Leave(user);

                final JavaTestKit probe = new JavaTestKit(system);

                subject.tell(new Join(new User(UUID.randomUUID(), new Credentials("test", "Test")), probe.getRef()), probe.getRef());

                subject.tell(new Join(user, getRef()), getRef());
                expectMsgClass(duration("1 second"), JoinSuccess.class);

                subject.tell(req, getRef());
                expectMsgEquals(duration("1 second"), new LeaveSuccess(user));
            }
        };
    }

    @Test
    public void testLeaveSuccessDifferentHandler() {
        new JavaTestKit(system) {
            {
                final Props props = LobbyActor.props(configuration);
                final ActorRef subject = system.actorOf(props, id.toString());
                final Leave req = new Leave(user);
                final JavaTestKit probe = new JavaTestKit(system);

                subject.tell(new Join(user, probe.getRef()), getRef());
                probe.expectNoMsg();
                expectMsgClass(duration("1 second"), JoinSuccess.class);

                subject.tell(req, getRef());
                expectMsgEquals(duration("1 second"), new LeaveSuccess(user));
                probe.expectNoMsg();
            }
        };
    }

    @Test
    public void testLeaveSuccessDifferentHandlerSubscribed() {
        new JavaTestKit(system) {
            {
                final Props props = LobbyActor.props(configuration);
                final ActorRef subject = system.actorOf(props, id.toString());
                final Leave req = new Leave(user);
                final JavaTestKit probe = new JavaTestKit(system);

                subject.tell(new Subscribe(probe.getRef()), probe.getRef());
                subject.tell(new Join(user, probe.getRef()), getRef());
                probe.expectMsgClass(duration("1 second"),  UserJoined.class);
                expectMsgClass(duration("1 second"), JoinSuccess.class);

                subject.tell(req, getRef());
                expectMsgEquals(duration("1 second"), new LeaveSuccess(user));
                probe.expectMsgEquals(duration("1 second"), new UserLeft(user));
            }
        };
    }

    @Test
    public void testLeaveNotInLobby() {
        new JavaTestKit(system) {
            {
                final Props props = LobbyActor.props(configuration);
                final ActorRef subject = system.actorOf(props, id.toString());
                final Leave req = new Leave(user);

                subject.tell(req, getRef());
                expectMsgClass(duration("1 second"), NotInLobbyException.class);
            }
        };
    }

}

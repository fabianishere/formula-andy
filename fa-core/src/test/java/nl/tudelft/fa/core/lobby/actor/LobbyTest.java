package nl.tudelft.fa.core.lobby.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;

import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.lobby.LobbyConfiguration;
import nl.tudelft.fa.core.lobby.LobbyInformation;
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
                final Props props = Lobby.props(configuration);
                final ActorRef subject = system.actorOf(props, id.toString());
                final InformationRequest req = InformationRequest.INSTANCE;

                subject.tell(req, getRef());

                // await the correct response
                expectMsgEquals(duration("1 second"), new LobbyInformation(
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
                final JoinRequest req = new JoinRequest(user, getRef());

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
                final Props props = Lobby.props(configuration);
                final ActorRef subject = system.actorOf(props, id.toString());
                final JavaTestKit probe = new JavaTestKit(system);
                final JoinRequest req = new JoinRequest(user, probe.getRef());

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
                final Props props = Lobby.props(configuration);
                final ActorRef subject = system.actorOf(props, id.toString());
                final JavaTestKit probe = new JavaTestKit(system);
                final JoinRequest req = new JoinRequest(user, probe.getRef());

                subject.tell(new SubscribeRequest(probe.getRef()), probe.getRef());
                subject.tell(req, getRef());
                probe.expectMsgClass(duration("1 second"), JoinSuccess.class);
                expectMsgClass(duration("1 second"), JoinSuccess.class);
            }
        };
    }

    @Test
    public void testJoinFull() {
        new JavaTestKit(system) {
            {
                final Props props = Lobby.props(new LobbyConfiguration(0, Duration.ZERO));
                final ActorRef subject = system.actorOf(props, id.toString());
                final JoinRequest req = new JoinRequest(user, getRef());

                subject.tell(req, getRef());

                // await the correct response
                expectMsgEquals(duration("1 second"), new LobbyFullError(0));
            }
        };
    }

    @Test
    public void testLeaveSuccess() {
        new JavaTestKit(system) {
            {
                final Props props = Lobby.props(configuration);
                final ActorRef subject = system.actorOf(props, id.toString());
                final LeaveRequest req = new LeaveRequest(user);

                final JavaTestKit probe = new JavaTestKit(system);

                subject.tell(new JoinRequest(new User(UUID.randomUUID(), new Credentials("test", "Test")), probe.getRef()), probe.getRef());

                subject.tell(new JoinRequest(user, getRef()), getRef());
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
                final Props props = Lobby.props(configuration);
                final ActorRef subject = system.actorOf(props, id.toString());
                final LeaveRequest req = new LeaveRequest(user);
                final JavaTestKit probe = new JavaTestKit(system);

                subject.tell(new JoinRequest(user, probe.getRef()), getRef());
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
                final Props props = Lobby.props(configuration);
                final ActorRef subject = system.actorOf(props, id.toString());
                final LeaveRequest req = new LeaveRequest(user);
                final JavaTestKit probe = new JavaTestKit(system);

                subject.tell(new SubscribeRequest(probe.getRef()), probe.getRef());
                subject.tell(new JoinRequest(user, probe.getRef()), getRef());
                probe.expectMsgClass(duration("1 second"), JoinSuccess.class);
                expectMsgClass(duration("1 second"), JoinSuccess.class);

                subject.tell(req, getRef());
                expectMsgEquals(duration("1 second"), new LeaveSuccess(user));
                probe.expectMsgEquals(duration("1 second"), new LeaveSuccess(user));
            }
        };
    }

    @Test
    public void testLeaveNotInLobby() {
        new JavaTestKit(system) {
            {
                final Props props = Lobby.props(configuration);
                final ActorRef subject = system.actorOf(props, id.toString());
                final LeaveRequest req = new LeaveRequest(user);

                subject.tell(req, getRef());
                expectMsgClass(duration("1 second"), NotInLobbyError.class);
            }
        };
    }

}

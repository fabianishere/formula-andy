package nl.tudelft.fa.core.lobby.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.lobby.LobbyBalancerInformation;
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
import java.util.HashMap;
import java.util.UUID;

public class LobbyBalancerTest {
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
    public void testInform() throws Exception {
        new JavaTestKit(system) {
            {
                final Props props = LobbyBalancer.props(configuration, 0, 5);
                final ActorRef subject = system.actorOf(props);
                final InformationRequest req = InformationRequest.INSTANCE;

                watch(subject);
                subject.tell(req, getRef());
                expectMsgEquals(duration("1 second"), new LobbyBalancerInformation(Collections.emptyMap()));
            }
        };
    }

    @Test
    public void testJoinNew() throws Exception {
        new JavaTestKit(system) {
            {
                final Props props = LobbyBalancer.props(configuration, 0, 5);
                final ActorRef subject = system.actorOf(props);
                final JoinRequest req = new JoinRequest(user);

                watch(subject);
                subject.tell(req, getRef());
                expectMsgClass(duration("1 second"), JoinSuccess.class);
            }
        };
    }

    @Test
    public void testJoinRunning() throws Exception {
        new JavaTestKit(system) {
            {
                final Props props = LobbyBalancer.props(configuration);
                final ActorRef subject = system.actorOf(props);
                final JoinRequest req = new JoinRequest(user);

                watch(subject);
                subject.tell(req, getRef());
                expectMsgClass(duration("1 second"), JoinSuccess.class);
            }
        };
    }

    @Test
    public void testJoinDifferent() throws Exception {
        new JavaTestKit(system) {
            {
                final Props props = LobbyBalancer.props(configuration, 0, 5);
                final ActorRef subject = system.actorOf(props);
                final JoinRequest req = new JoinRequest(user);
                final JavaTestKit probe = new JavaTestKit(system);

                subject.tell(new JoinRequest(new User(UUID.randomUUID(), new Credentials("test", "test"))), probe.getRef());
                probe.expectMsgClass(duration("1 second"),JoinSuccess.class);
                subject.tell(req, getRef());
                probe.expectNoMsg();
                expectMsgClass(duration("1 second"), JoinSuccess.class);
            }
        };
    }

    @Test
    public void testJoinSame() throws Exception {
        new JavaTestKit(system) {
            {
                final Props props = LobbyBalancer.props(new LobbyConfiguration(2, Duration.ZERO), 0, 1);
                final ActorRef subject = system.actorOf(props);
                final JoinRequest req = new JoinRequest(user);
                final JavaTestKit probe = new JavaTestKit(system);
                final User snd = new User(UUID.randomUUID(), new Credentials("test", "test"));

                subject.tell(new JoinRequest(snd), probe.getRef());
                probe.expectMsgEquals(duration("1 second"), new JoinSuccess(snd));
                subject.tell(req, getRef());
                probe.expectMsgEquals(duration("1 second"), new JoinSuccess(user));
                expectMsgEquals(duration("1 second"), new JoinSuccess(user));
            }
        };
    }

    @Test
    public void testJoinExhausted() throws Exception {
        new JavaTestKit(system) {
            {
                final Props props = LobbyBalancer.props(configuration, 0, 0);
                final ActorRef subject = system.actorOf(props);
                final JoinRequest req = new JoinRequest(user);

                subject.tell(req, getRef());
                expectMsgClass(duration("1 second"), LobbyBalancerExhaustedError.class);
            }
        };
    }

    @Test
    public void testLeaveKill() throws Exception {
        new JavaTestKit(system) {
            {
                final Props props = LobbyBalancer.props(configuration, 0, 2);
                final ActorRef subject = system.actorOf(props);
                final JoinRequest req = new JoinRequest(user);
                final JavaTestKit probe = new JavaTestKit(system);
                final User snd = new User(UUID.randomUUID(), new Credentials("test", "test"));
                subject.tell(new JoinRequest(snd), probe.getRef());
                probe.expectMsgClass(duration("1 second"),JoinSuccess.class);
                subject.tell(req, getRef());
                expectMsgClass(duration("1 second"), JoinSuccess.class);
                reply(new LeaveRequest(user));
                expectMsgClass(duration("1 second"), LeaveSuccess.class);

                subject.tell(InformationRequest.INSTANCE, getRef());
                expectMsgEquals(duration("1 second"), new LobbyBalancerInformation(new HashMap<ActorRef, LobbyInformation>() {{
                    put(probe.getLastSender(), new LobbyInformation(LobbyStatus.PREPARATION, configuration, Collections.singleton(snd)));
                }}));
            }
        };
    }

    @Test
    public void testLeaveNoKill() throws Exception {
        new JavaTestKit(system) {
            {
                final Props props = LobbyBalancer.props(configuration, 1, 2);
                final ActorRef subject = system.actorOf(props);
                final JoinRequest req = new JoinRequest(user);

                subject.tell(req, getRef());
                expectMsgClass(duration("1 second"), JoinSuccess.class);
                reply(new LeaveRequest(user));
                expectMsgClass(duration("1 second"), LeaveSuccess.class);

                subject.tell(InformationRequest.INSTANCE, getRef());
                expectMsgEquals(duration("1 second"), new LobbyBalancerInformation(new HashMap<ActorRef, LobbyInformation>() {{
                    put(getLastSender(), new LobbyInformation(LobbyStatus.PREPARATION, configuration, Collections.emptySet()));
                }}));
            }
        };
    }
}

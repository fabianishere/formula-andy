package nl.tudelft.fa.core.lobby.actor;

import static org.junit.Assert.*;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;
import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.lobby.LobbyBalancer;
import nl.tudelft.fa.core.lobby.LobbyConfiguration;
import nl.tudelft.fa.core.lobby.message.*;

import nl.tudelft.fa.core.lobby.schedule.LobbyScheduleFactory;
import nl.tudelft.fa.core.lobby.schedule.StaticLobbyScheduleFactory;
import nl.tudelft.fa.core.user.User;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.Await;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

public class LobbyBalancerActorTest {
    private static ActorSystem system;
    private LobbyConfiguration configuration;
    private User user;
    private LobbyScheduleFactory factory;

    @BeforeClass
    public static void setUpClass() {
        system = ActorSystem.create();
    }

    @Before
    public void setUp() {
        factory = new StaticLobbyScheduleFactory(Collections.emptyList());
        configuration = new LobbyConfiguration(1, Duration.ofMinutes(2), Duration.ZERO, factory);
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
                final Props props = LobbyBalancerActor.props(configuration, 0, 5);
                final ActorRef subject = system.actorOf(props);
                final RequestInformation req = RequestInformation.INSTANCE;

                watch(subject);
                subject.tell(req, getRef());
                expectMsgEquals(duration("1 second"), new LobbyBalancer(Collections.emptyMap()));
            }
        };
    }

    @Test
    public void testRefresh() throws Exception {
        new JavaTestKit(system) {
            {
                final Props props = LobbyBalancerActor.props(configuration, 0, 5);
                final ActorRef subject = system.actorOf(props);
                final RequestInformation req = RequestInformation.INSTANCE;

                watch(subject);
                subject.tell(Refresh.INSTANCE, getRef());
                expectNoMsg(duration("1 second"));
            }
        };
    }

    @Test
    public void testJoinNew() throws Exception {
        new JavaTestKit(system) {
            {
                final Props props = LobbyBalancerActor.props(configuration, 0, 5);
                final ActorRef subject = system.actorOf(props);
                final Join req = new Join(user, getRef());

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
                final Props props = LobbyBalancerActor.props(configuration);
                final ActorRef subject = system.actorOf(props);
                final Join req = new Join(user, getRef());

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
                final Props props = LobbyBalancerActor.props(configuration, 0, 5);
                final ActorRef subject = system.actorOf(props);
                final Join req = new Join(user, getRef());
                final JavaTestKit probe = new JavaTestKit(system);

                subject.tell(new Join(new User(UUID.randomUUID(), new Credentials("test", "test")), probe.getRef()),
                    probe.getRef());
                probe.expectMsgClass(duration("1 second"), JoinSuccess.class);
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
                final Props props = LobbyBalancerActor.props(new LobbyConfiguration(2, Duration.ofMinutes(5), Duration.ofMinutes(5), factory), 0, 1);
                final ActorRef subject = system.actorOf(props);
                final Join req = new Join(user, getRef());
                final JavaTestKit probe = new JavaTestKit(system);
                final User snd = new User(UUID.randomUUID(), new Credentials("test", "test"));

                subject.tell(new Join(snd, probe.getRef()), probe.getRef());
                probe.expectMsgClass(duration("1 second"), JoinSuccess.class);
                probe.reply(new Subscribe(probe.getRef()));
                subject.tell(req, getRef());
                probe.expectMsgClass(duration("1 second"), UserJoined.class);
                expectMsgClass(duration("1 second"), JoinSuccess.class);
            }
        };
    }

    @Test
    public void testJoinExhausted() throws Exception {
        new JavaTestKit(system) {
            {
                final Props props = LobbyBalancerActor.props(configuration, 0, 0);
                final ActorRef subject = system.actorOf(props);
                final Join req = new Join(user, getRef());

                subject.tell(req, getRef());
                expectMsgClass(duration("1 second"), LobbyBalancerExhaustedException.class);
            }
        };
    }

    @Test
    public void testLeaveKill() throws Exception {
        new JavaTestKit(system) {
            {
                final Props props = LobbyBalancerActor.props(configuration, 0, 2);
                final TestActorRef<LobbyBalancerActor> subject = TestActorRef.create(system, props, "testB");
                final Join req = new Join(user, getRef());
                final JavaTestKit probe = new JavaTestKit(system);
                final User snd = new User(UUID.randomUUID(), new Credentials("test", "test"));

                subject.tell(new Join(user, probe.getRef()), probe.getRef());
                probe.expectMsgClass(duration("1 second"), JoinSuccess.class);
                probe.reply(new Leave(user));
                probe.expectMsgClass(duration("1 second"), LeaveSuccess.class);
                probe.expectNoMsg(duration("1 second"));
                LobbyBalancer info = (LobbyBalancer) Await.result(Patterns.ask(subject, RequestInformation.INSTANCE, 1000), duration("1 second"));
                assertEquals(0, info.getLobbies().size());
            }
        };
    }

    @Test
    public void testLeaveNoKill() throws Exception {
        new JavaTestKit(system) {
            {
                final Props props = LobbyBalancerActor.props(configuration, 1, 2);
                final ActorRef subject = system.actorOf(props);
                final Join req = new Join(user, getRef());

                subject.tell(req, getRef());
                expectMsgClass(duration("1 second"), JoinSuccess.class);
                reply(new Leave(user));
                expectMsgClass(duration("1 second"), LeaveSuccess.class);
                expectNoMsg(duration("1 second"));
                LobbyBalancer info = (LobbyBalancer) Await.result(Patterns.ask(subject, RequestInformation.INSTANCE, 1000), duration("1 second"));
                assertEquals(1, info.getLobbies().size());
            }
        };
    }
}

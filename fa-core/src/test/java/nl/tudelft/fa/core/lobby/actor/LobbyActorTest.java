package nl.tudelft.fa.core.lobby.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.testkit.JavaTestKit;

import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.lobby.LobbyConfiguration;
import nl.tudelft.fa.core.lobby.Lobby;
import nl.tudelft.fa.core.lobby.LobbyStatus;
import nl.tudelft.fa.core.lobby.message.*;
import nl.tudelft.fa.core.lobby.schedule.LobbyScheduleFactory;
import nl.tudelft.fa.core.lobby.schedule.StaticLobbyScheduleFactory;
import nl.tudelft.fa.core.race.CarConfiguration;
import nl.tudelft.fa.core.race.TeamConfiguration;
import nl.tudelft.fa.core.team.inventory.Car;
import nl.tudelft.fa.core.user.User;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.duration.FiniteDuration;

import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class LobbyActorTest {
    private static ActorSystem system;
    private UUID id;
    private LobbyScheduleFactory factory;
    private LobbyConfiguration configuration;
    private User user;

    @BeforeClass
    public static void setUpClass() {
        system = ActorSystem.create();
    }

    @Before
    public void setUp() {
        id = UUID.randomUUID();
        factory = new StaticLobbyScheduleFactory(Collections.emptyList());
        configuration = new LobbyConfiguration(2, Duration.ofMinutes(10), Duration.ofMinutes(5), factory);
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
                    subject.path().name(), LobbyStatus.INTERMISSION, configuration, Collections.emptySet(), Collections.emptyList()));
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
                final Props props = LobbyActor.props(new LobbyConfiguration(0, Duration.ofMinutes(5), Duration.ofMinutes(5), factory));
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
                expectMsgClass(duration("1 second"), LeaveSuccess.class);
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
                expectMsgClass(duration("1 second"), LeaveSuccess.class);
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
                expectMsgClass(duration("1 second"), LeaveSuccess.class);
                probe.expectMsgEquals(duration("1 second"), new UserLeft(user));
            }
        };
    }

    @Test
    public void testLeaveSuccessDifferentHandlerUnsubscribed() {
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

                subject.tell(new Unsubscribe(probe.getRef()), probe.getRef());
                subject.tell(req, getRef());
                expectMsgClass(duration("1 second"), LeaveSuccess.class);
                probe.expectNoMsg();
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

    @Test
    public void testTerminatedLeave() {
        new JavaTestKit(system) {
            {
                final Props props = LobbyActor.props(configuration);
                final ActorRef subject = system.actorOf(props, id.toString());
                final Leave req = new Leave(user);
                final JavaTestKit probe = new JavaTestKit(system);

                subject.tell(new Subscribe(getRef()), getRef());
                subject.tell(new Join(user, probe.getRef()), probe.getRef());
                expectMsgClass(duration("1 second"),  UserJoined.class);
                probe.expectMsgClass(duration("1 second"), JoinSuccess.class);

                system.stop(probe.getRef());
                expectMsgClass(duration("1 second"), UserLeft.class);
            }
        };
    }

    @Test
    public void testSubmitConfiguration() {
        new JavaTestKit(system) {
            {
                final Props props = LobbyActor.props(configuration);
                final ActorRef subject = system.actorOf(props, id.toString());
                final Leave req = new Leave(user);
                final JavaTestKit probe = new JavaTestKit(system);
                final User user = new User(UUID.randomUUID(), new Credentials("fabianishere", "test"));
                final TeamConfiguration configuration = new TeamConfiguration(new HashSet<CarConfiguration>() {{
                    add(new CarConfiguration(new Car(UUID.randomUUID()), null, null, null, null, null));
                }});
                final TeamConfigurationSubmission msg = new TeamConfigurationSubmission(user, configuration);

                subject.tell(new Subscribe(probe.getRef()), probe.getRef());
                subject.tell(new Join(user, getRef()), getRef());
                expectMsgClass(duration("1 second"),  JoinSuccess.class);
                probe.expectMsgClass(duration("1 second"), UserJoined.class);

                subject.tell(msg, getRef());
                probe.expectMsgClass(duration("1 second"), TeamConfigurationSubmission.class);
            }
        };
    }

    @Test
    public void testTransitToPreparationEnough() throws Exception {
        new JavaTestKit(system) {
            {
                final Props props = LobbyActor.props(new LobbyConfiguration(2, Duration.ofMillis(500), Duration.ofMinutes(5), factory));
                final ActorRef subject = system.actorOf(props, id.toString());
                final Subscribe req = new Subscribe(getRef());

                subject.tell(new Join(new User(UUID.randomUUID(), null), getRef()), getRef());
                expectMsgClass(duration("1 second"), JoinSuccess.class);
                expectNoMsg(duration("1 second"));
                Lobby info = (Lobby) Await.result(Patterns.ask(subject, RequestInformation.INSTANCE, 1000), FiniteDuration.create(1000, TimeUnit.MILLISECONDS));
                assertEquals(LobbyStatus.PREPARATION, info.getStatus());
            }
        };
    }

    @Test
    public void testTransitToPreparationNotEnough() throws Exception {
        new JavaTestKit(system) {
            {
                final Props props = LobbyActor.props(new LobbyConfiguration(2, Duration.ZERO, Duration.ofMinutes(5), factory));
                final ActorRef subject = system.actorOf(props, id.toString());
                final Subscribe req = new Subscribe(getRef());

                subject.tell(req, getRef());
                Lobby info = (Lobby) Await.result(Patterns.ask(subject, RequestInformation.INSTANCE, 1000), FiniteDuration.create(1000, TimeUnit.MILLISECONDS));
                assertEquals(LobbyStatus.INTERMISSION, info.getStatus());
            }
        };
    }
}

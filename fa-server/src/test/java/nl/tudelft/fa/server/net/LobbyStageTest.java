package nl.tudelft.fa.server.net;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.BidiFlow;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Source;
import akka.stream.testkit.javadsl.TestSink;
import akka.testkit.JavaTestKit;
import nl.tudelft.fa.core.lobby.Lobby;
import nl.tudelft.fa.core.lobby.LobbyConfiguration;
import nl.tudelft.fa.core.lobby.actor.LobbyActor;
import nl.tudelft.fa.core.lobby.message.*;
import nl.tudelft.fa.core.lobby.schedule.StaticLobbyScheduleFactory;
import nl.tudelft.fa.core.user.User;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author Fabian Mastenbroek <mail.fabianm@gmail.com>
 */
public class LobbyStageTest {
    private static ActorSystem system;
    private static ActorMaterializer mat;
    private LobbyConfiguration configuration;
    private ActorRef lobby;
    private User user;
    private LobbyStage stage;

    @BeforeClass
    public static void setUpClass() {
        system = ActorSystem.create();
        mat = ActorMaterializer.create(system);
    }

    @AfterClass
    public static void tearDownClass() {
        JavaTestKit.shutdownActorSystem(system);
    }

    @Before
    public void setUp() {
        configuration = new LobbyConfiguration(11, Duration.ofMinutes(3), Duration.ZERO, new StaticLobbyScheduleFactory(Collections.emptyList()));
        lobby = system.actorOf(LobbyActor.props(configuration));
        user = new User(UUID.randomUUID(), null);
        stage = new LobbyStage(lobby);
    }

    @Test
    public void acceptMessage() {
        final Source<LobbyInboundMessage, NotUsed> source =
            Source.maybe().mergeMat(Source.single(new Join(user, null)), Keep.right());
        final Flow<LobbyInboundMessage, LobbyOutboundMessage, NotUsed> flow = Flow.fromGraph(stage);

        LobbyOutboundMessage msg = source
            .via(flow)
            .runWith(TestSink.probe(system), mat)
            .requestNext();
        assertTrue(msg instanceof JoinSuccess);
    }
}

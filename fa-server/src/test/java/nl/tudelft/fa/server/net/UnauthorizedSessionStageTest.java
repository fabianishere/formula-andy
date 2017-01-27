package nl.tudelft.fa.server.net;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.BidiFlow;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Source;
import akka.stream.testkit.javadsl.TestSink;
import akka.testkit.JavaTestKit;
import nl.tudelft.fa.core.lobby.message.*;
import nl.tudelft.fa.server.net.message.NotAuthorizedException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class UnauthorizedSessionStageTest {
    private static ActorSystem system;
    private static ActorMaterializer mat;
    private UnauthorizedSessionStage stage;

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
        stage = new UnauthorizedSessionStage();
    }

    @Test
    public void rejectMessage() {
        final Source<LobbyInboundMessage, NotUsed> source = Source.single(new Join(null, null));
        final Flow<LobbyInboundMessage, LobbyOutboundMessage, NotUsed> join = Flow.fromFunction(message -> new TeamJoined(null));
        final Flow<LobbyInboundMessage, LobbyOutboundMessage, NotUsed> flow =
            BidiFlow.fromGraph(stage).join(join);

        LobbyOutboundMessage msg = source
            .via(flow)
            .runWith(TestSink.probe(system), mat)
            .requestNext();
        assertTrue(msg instanceof NotAuthorizedException);
    }

    @Test
    public void rejectMultiple() {
        final Source<LobbyInboundMessage, NotUsed> source = Source.from(Arrays.asList(new Join(null, null), new Join(null, null)));
        final Flow<LobbyInboundMessage, LobbyOutboundMessage, NotUsed> join = Flow.fromFunction(message -> new TeamJoined(null));
        final Flow<LobbyInboundMessage, LobbyOutboundMessage, NotUsed> flow =
            BidiFlow.fromGraph(stage).join(join);

        LobbyOutboundMessage msg = source
            .via(flow)
            .runWith(TestSink.probe(system), mat)
            .request(1)
            .requestNext();
        assertTrue(msg instanceof NotAuthorizedException);
    }

    @Test
    public void acceptMessage() {
        final Source<LobbyInboundMessage, NotUsed> source = Source.single(RequestInformation.INSTANCE);
        final Flow<LobbyInboundMessage, LobbyOutboundMessage, NotUsed> join = Flow.fromFunction(message -> new TeamJoined(null));
        final Flow<LobbyInboundMessage, LobbyOutboundMessage, NotUsed> flow =
            BidiFlow.fromGraph(stage).join(join);

        LobbyOutboundMessage msg = source
            .via(flow)
            .runWith(TestSink.probe(system), mat)
            .requestNext();
        assertTrue(msg instanceof TeamJoined);
    }
}

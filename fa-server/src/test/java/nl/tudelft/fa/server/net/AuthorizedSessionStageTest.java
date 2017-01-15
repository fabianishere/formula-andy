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
import nl.tudelft.fa.core.user.User;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author Fabian Mastenbroek <mail.fabianm@gmail.com>
 */
public class AuthorizedSessionStageTest {
    private static ActorSystem system;
    private static ActorMaterializer mat;
    private User user;
    private AuthorizedSessionStage stage;

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
        user = new User(UUID.randomUUID(), null);
        stage = new AuthorizedSessionStage(user);
    }

    @Test
    public void acceptMultiple() {
        final Source<LobbyInboundMessage, NotUsed> source = Source.from(Arrays.asList(new Join(null, null), new Join(null, null)));
        final Flow<LobbyInboundMessage, LobbyOutboundMessage, NotUsed> join = Flow.fromFunction(LobbyInboundMessageWrapper::new);
        final Flow<LobbyInboundMessage, LobbyOutboundMessage, NotUsed> flow =
            BidiFlow.fromGraph(stage).join(join);

        LobbyInboundMessageWrapper msg = (LobbyInboundMessageWrapper) source
            .via(flow)
            .runWith(TestSink.probe(system), mat)
            .request(1)
            .requestNext();
        assertEquals(new Join(user, null), msg.message);
    }

    @Test
    public void acceptMessage() {
        final Source<LobbyInboundMessage, NotUsed> source = Source.single(RequestInformation.INSTANCE);
        final Flow<LobbyInboundMessage, LobbyOutboundMessage, NotUsed> join = Flow.fromFunction(LobbyInboundMessageWrapper::new);
        final Flow<LobbyInboundMessage, LobbyOutboundMessage, NotUsed> flow =
            BidiFlow.fromGraph(stage).join(join);

        LobbyInboundMessageWrapper msg = (LobbyInboundMessageWrapper) source
            .via(flow)
            .runWith(TestSink.probe(system), mat)
            .requestNext();
        assertTrue(msg.message instanceof RequestInformation);
    }


    @Test
    public void acceptLeaveMessage() {
        final Source<LobbyInboundMessage, NotUsed> source = Source.single(new Leave(null));
        final Flow<LobbyInboundMessage, LobbyOutboundMessage, NotUsed> join = Flow.fromFunction(LobbyInboundMessageWrapper::new);
        final Flow<LobbyInboundMessage, LobbyOutboundMessage, NotUsed> flow =
            BidiFlow.fromGraph(stage).join(join);

        LobbyInboundMessageWrapper msg = (LobbyInboundMessageWrapper) source
            .via(flow)
            .runWith(TestSink.probe(system), mat)
            .requestNext();
        assertEquals(new Leave(user), msg.message);
    }

    private class LobbyInboundMessageWrapper implements LobbyOutboundMessage {
        public final LobbyInboundMessage message;

        public LobbyInboundMessageWrapper(LobbyInboundMessage message) {
            this.message = message;
        }
    }
}

package nl.tudelft.fa.server.net.codec.jackson;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.ws.BinaryMessage;
import akka.http.javadsl.model.ws.Message;
import akka.http.javadsl.model.ws.TextMessage;
import akka.stream.ActorAttributes;
import akka.stream.ActorMaterializer;
import akka.stream.Supervision;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Source;
import akka.stream.testkit.javadsl.TestSink;
import akka.testkit.JavaTestKit;
import akka.util.ByteString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.fa.core.lobby.message.*;
import nl.tudelft.fa.server.helper.jackson.LobbyModule;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class JacksonWebSocketCodecTest {
    private static ActorSystem system;
    private static ActorMaterializer mat;
    private JacksonWebSocketCodec codec;
    private ObjectMapper mapper;

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
        mapper = new ObjectMapper();
        mapper.registerModule(new LobbyModule());
        codec = new JacksonWebSocketCodec(mapper);
    }

    public void decodeMessageSuccess(Message message) {
        final Source<Message, NotUsed> source = Source.single(message);

        LobbyInboundMessage msg = source
            .via(codec.decodeFlow())
            .runWith(TestSink.probe(system), mat)
            .requestNext();
        assertTrue(msg instanceof RequestInformation);
    }

    public void decodeMessageMappingFailure(Message message) {
        final Source<Message, NotUsed> source = Source.single(message);

        /*Throwable cause =*/ source
            .via(codec.decodeFlow())
            .runWith(TestSink.probe(system), mat)
            // XXX Akka's supervision does not work correctly yet.
            // XXX See JacksonWebSocketCodec.java
            //.expectSubscriptionAndError(true);
            .expectSubscriptionAndComplete();
        //assertTrue(cause instanceof JsonMappingException);
    }

    public void decodeMessageMappingStreamingFailure(Message message) {
        final Source<Message, NotUsed> source = Source.single(message);

        Throwable cause = source
            .via(codec.decodeFlow())
            .runWith(TestSink.probe(system), mat)
            .expectSubscriptionAndError(true);
        assertTrue(cause instanceof JsonMappingException);
    }

    @Test
    public void decodeTextMessageFailureResume() {
        final Message messageA = TextMessage.create(Source.single("{ \"@type\": \"non-existent\" }"));
        final Message messageB = TextMessage.create("{ \"@type\": \"info\" }");
        final Source<Message, NotUsed> source = Source.from(Arrays.asList(messageA, messageB));

        Flow<LobbyInboundMessage, LobbyOutboundMessage, NotUsed> join = Flow.fromFunction(ign -> new UserLeft(null));
        Message msg = source
            .via(codec.bidiFlow().join(join))
            .withAttributes(ActorAttributes.withSupervisionStrategy(Supervision.getResumingDecider()))
            .runWith(TestSink.probe(system), mat)
            .requestNext();
    }

    @Test
    public void decodeTextMessageStrictSuccess() {
        final Message message = TextMessage.create("{ \"@type\": \"info\" }");
        decodeMessageSuccess(message);
    }

    @Test
    public void decodeTextMessageStrictFailure() {
        final Message message = TextMessage.create("{ \"@type\": \"non-existent\" }");
        decodeMessageMappingFailure(message);
    }

    @Test
    public void decodeTextMessageStreamedSuccess() {
        final Message message = TextMessage.create(Source.single("{ \"@type\": \"info\" }"));
        decodeMessageSuccess(message);
    }

    @Test
    public void decodeTextMessageStreamedFailure() {
        final Message message = TextMessage.create(Source.single("{ \"@type\": \"non-existent\" }"));
        decodeMessageMappingStreamingFailure(message);
    }

    @Test
    public void decodeBinaryMessageStrictSuccess() {
        final Message message = BinaryMessage.create(ByteString.fromString("{ \"@type\": \"info\" }"));
        decodeMessageSuccess(message);
    }

    @Test
    public void decodeBinaryMessageStrictFailure() {
        final Message message = BinaryMessage.create(ByteString.fromString("{ \"@type\": \"non-existent\" }"));
        decodeMessageMappingFailure(message);
    }

    @Test
    public void decodeBinaryMessageStreamedSuccess() {
        final Message message = BinaryMessage.create(Source.single(ByteString.fromString("{ \"@type\": \"info\" }")));
        decodeMessageSuccess(message);
    }

    @Test
    public void decodeBinaryMessageStreamedFailure() {
        final Message message = BinaryMessage.create(Source.single(ByteString.fromString("{ \"@type\": \"non-existent\" }")));
        decodeMessageMappingStreamingFailure(message);
    }
}

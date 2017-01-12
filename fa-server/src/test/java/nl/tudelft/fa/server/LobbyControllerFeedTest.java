package nl.tudelft.fa.server;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.ws.Message;
import akka.http.javadsl.model.ws.TextMessage;
import akka.stream.ActorAttributes;
import akka.stream.ActorMaterializer;
import akka.stream.Supervision;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Source;
import akka.stream.testkit.javadsl.TestSink;
import akka.testkit.JavaTestKit;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.fa.core.lobby.LobbyConfiguration;
import nl.tudelft.fa.core.lobby.actor.LobbyActor;
import nl.tudelft.fa.core.lobby.actor.LobbyBalancerActor;
import nl.tudelft.fa.core.lobby.message.LobbyInboundMessage;
import nl.tudelft.fa.core.lobby.message.LobbyOutboundMessage;
import nl.tudelft.fa.core.lobby.message.RequestInformation;
import nl.tudelft.fa.core.lobby.message.UserLeft;
import nl.tudelft.fa.server.controller.LobbyController;
import nl.tudelft.fa.server.helper.jackson.LobbyModule;
import nl.tudelft.fa.server.net.codec.jackson.JacksonWebSocketCodec;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class LobbyControllerFeedTest {
    private static ActorSystem system;
    private static ActorMaterializer mat;
    private LobbyConfiguration configuration;
    private ActorRef lobby;
    private LobbyController controller;

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
        configuration = new LobbyConfiguration(11, java.time.Duration.ofMinutes(5));
        lobby = system.actorOf(LobbyActor.props(configuration));
        controller = new LobbyController(system, null);
    }
}

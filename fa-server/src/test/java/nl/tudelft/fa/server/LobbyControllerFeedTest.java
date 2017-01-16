package nl.tudelft.fa.server;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.ws.Message;
import akka.http.javadsl.model.ws.TextMessage;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Source;
import akka.stream.javadsl.Sink;
import akka.testkit.JavaTestKit;
import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.lobby.LobbyConfiguration;
import nl.tudelft.fa.core.lobby.actor.LobbyActor;
import nl.tudelft.fa.core.lobby.schedule.StaticLobbyScheduleFactory;
import nl.tudelft.fa.core.user.User;
import nl.tudelft.fa.server.controller.LobbyController;
import nl.tudelft.fa.server.net.AuthorizedSessionStage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

public class LobbyControllerFeedTest {
    private static ActorSystem system;
    private static ActorMaterializer mat;
    private LobbyConfiguration configuration;
    private User user;
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
        user = new User(UUID.randomUUID(), new Credentials("fabianishere", "test"));
        configuration = new LobbyConfiguration(11, java.time.Duration.ofMinutes(5), Duration.ZERO, new StaticLobbyScheduleFactory(Collections.emptyList()));
        lobby = system.actorOf(LobbyActor.props(configuration));
        controller = new LobbyController(system, null,null, null);
    }

    public void testTeamConfigurationSubmission() throws Exception {
        Flow<Message, Message, NotUsed> flow = controller.feedHandler(lobby, new AuthorizedSessionStage(user));

        Source<Message, NotUsed> source = Source.single(TextMessage.create("{ \"@type\": \"team\" }"));
        source
            .via(flow)
            .runWith(Sink.foreach(msg -> System.out.println(msg.asTextMessage().getStrictText())), mat)
            .toCompletableFuture().get();
    }
}

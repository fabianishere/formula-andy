package nl.tudelft.fa.client.lobby.controller;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.model.Uri;
import akka.http.javadsl.model.ws.Message;
import akka.http.javadsl.model.ws.TextMessage;
import akka.http.javadsl.model.ws.WebSocketUpgradeResponse;
import akka.japi.Pair;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Source;
import akka.stream.javadsl.Sink;
import nl.tudelft.fa.client.Client;
import nl.tudelft.fa.client.lobby.Lobby;
import nl.tudelft.fa.client.lobby.message.Join;
import nl.tudelft.fa.client.lobby.message.LobbyInboundMessage;
import nl.tudelft.fa.client.lobby.message.LobbyOutboundMessage;
import nl.tudelft.fa.client.net.message.Ping;
import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.auth.actor.Authenticator;
import nl.tudelft.fa.core.lobby.LobbyConfiguration;
import nl.tudelft.fa.core.lobby.actor.LobbyBalancerActor;
import nl.tudelft.fa.core.lobby.schedule.StaticLobbyScheduleFactory;
import nl.tudelft.fa.core.user.User;
import nl.tudelft.fa.server.RestService;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.duration.FiniteDuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.Duration;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class LobbyControllerTest {
    private static ActorSystem system;
    private static ActorMaterializer materializer;
    private static EntityManagerFactory factory;
    private static ActorRef authenticator;
    private static ActorRef balancer;

    private Client client;
    private Lobby lobby;
    private LobbyController controller;

    @BeforeClass
    public static void setUpClass() {
        system = ActorSystem.create();
        materializer = ActorMaterializer.create(system);
        factory = Persistence.createEntityManagerFactory( "formula-andy-test" );
        final EntityManager manager = factory.createEntityManager();

        // Create simple user
        manager.getTransaction().begin();
        manager.persist(new User(UUID.randomUUID(), new Credentials("fabianishere", "test")));
        manager.getTransaction().commit();

        authenticator = system.actorOf(Authenticator.props(manager));
        balancer = system.actorOf(LobbyBalancerActor.props(new LobbyConfiguration(11, Duration.ofMinutes(6), Duration.ZERO, new StaticLobbyScheduleFactory(Collections.emptyList())), 2, 4));
    }

    @Before
    public void setUp() throws Exception {
        final RestService app = new RestService(system, authenticator, balancer);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
        client = new Client(new HttpMock(routeFlow), materializer, Uri.create("http://localhost:8080"));
        lobby = client.balancer()
            .lobbies()
            .thenApply(controllers -> controllers.iterator().next())
            .toCompletableFuture()
            .get();
        controller = client.balancer()
            .controller(lobby.getId())
            .toCompletableFuture()
            .get();
    }

    @AfterClass
    public static void tearDownClass() {
        system.terminate();
        factory.close();
    }

    @Test
    public void get() throws Exception {
        assertEquals(lobby, controller.get().toCompletableFuture().get());
    }

    public void feed() throws Exception {
        // TODO mock WebSockets
        client = new Client(Http.get(system), materializer, Uri.create("http://localhost:8080"));
        controller = client.balancer()
            .find()
            .toCompletableFuture()
            .get();

        nl.tudelft.fa.client.auth.Credentials credentials = new nl.tudelft.fa.client.auth.Credentials("fabianishere", "test");
        final Source<LobbyInboundMessage, Cancellable> source = Source.tick(FiniteDuration.Zero(),
                FiniteDuration.create(500, TimeUnit.MILLISECONDS), Ping.INSTANCE)
            .merge(Source.single(Join.INSTANCE));
        Flow<LobbyOutboundMessage, LobbyInboundMessage, CompletionStage<Done>> flow = Flow.fromSinkAndSourceMat(Sink.foreach(System.out::println), source, Keep.left());
        Pair<CompletionStage<WebSocketUpgradeResponse>, CompletionStage<Done>> pair = controller.feed(credentials, flow);


        // The first value in the pair is a CompletionStage<WebSocketUpgradeResponse> that
        // completes when the WebSocket request has connected successfully (or failed)
        CompletionStage<WebSocketUpgradeResponse> upgradeCompletion = pair.first();

        // the second value is the completion of the sink from above
        // in other words, it completes when the WebSocket disconnects
        CompletionStage<Done> closed = pair.second();
        CompletionStage<Done> connected = upgradeCompletion.thenApply(upgrade->
        {
            // just like a regular http request we can access response status which is available via upgrade.response.status
            // status code 101 (Switching Protocols) indicates that server support WebSockets
            if (upgrade.response().status().equals(StatusCodes.SWITCHING_PROTOCOLS)) {
                return Done.getInstance();
            } else {
                throw new RuntimeException(("Connection failed: " + upgrade.response().status()));
            }
        });

        connected.thenAccept(done -> System.out.println("Connected"));
        closed.thenAccept(done -> System.out.println("Connection closed")).toCompletableFuture().get();
    }
}

package nl.tudelft.fa.client.lobby.controller;

import static org.junit.Assert.*;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Uri;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import nl.tudelft.fa.client.AbstractClient;
import nl.tudelft.fa.client.AnonymousClient;
import nl.tudelft.fa.client.Client;
import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.auth.actor.Authenticator;
import nl.tudelft.fa.core.lobby.Lobby;
import nl.tudelft.fa.core.lobby.LobbyBalancer;
import nl.tudelft.fa.core.lobby.LobbyConfiguration;
import nl.tudelft.fa.core.lobby.actor.LobbyBalancerActor;
import nl.tudelft.fa.core.lobby.message.RequestInformation;
import nl.tudelft.fa.core.lobby.schedule.StaticLobbyScheduleFactory;
import nl.tudelft.fa.core.user.User;
import nl.tudelft.fa.server.RestService;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.duration.FiniteDuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class LobbyBalancerControllerTest {
    private static ActorSystem system;
    private static ActorMaterializer materializer;
    private static EntityManagerFactory factory;
    private static ActorRef authenticator;
    private static ActorRef balancer;

    private AbstractClient client;
    private LobbyBalancerController controller;

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
        balancer = system.actorOf(LobbyBalancerActor.props(new LobbyConfiguration(11, Duration.ofMinutes(5), Duration.ZERO, new StaticLobbyScheduleFactory(Collections.emptyList())), 2, 4));
    }

    @Before
    public void setUp() {
        final RestService app = new RestService(system, authenticator, balancer, null);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
        client = new AnonymousClient(new HttpMock(routeFlow), materializer, Uri.create("http://localhost:8080"));
        controller = client.balancer();
    }

    @AfterClass
    public static void tearDownClass() {
        system.terminate();
        factory.close();
    }

    @Test
    public void getLobbies() throws Exception {
        LobbyBalancer info = (LobbyBalancer) Await.result(Patterns.ask(balancer, RequestInformation.INSTANCE, 1000), FiniteDuration.create("1 second"));

        assertEquals(info.getLobbies().size(), controller.lobbies().toCompletableFuture().get().size());
    }


    @Test
    public void getLobby() throws Exception {
        LobbyBalancer info = (LobbyBalancer) Await.result(Patterns.ask(balancer, RequestInformation.INSTANCE, 1000), FiniteDuration.create("1 second"));

        for (Map.Entry<ActorRef, Lobby> entry : info.getLobbies().entrySet()) {
            assertEquals(entry.getValue().getId(), controller.lobby(entry.getValue().getId()).toCompletableFuture().get().getId());
        }
    }

    @Test
    public void controller() throws Exception {
        LobbyBalancer info = (LobbyBalancer) Await.result(Patterns.ask(balancer, RequestInformation.INSTANCE, 1000), FiniteDuration.create("1 second"));

        for (Map.Entry<ActorRef, Lobby> entry : info.getLobbies().entrySet()) {
            controller.controller(entry.getValue().getId()).toCompletableFuture().get();
        }
    }

    @Test
    public void controllers() throws Exception {
        controller.controllers().toCompletableFuture().get();
    }

    @Test
    public void find() throws Exception {
        controller.find();
    }
}

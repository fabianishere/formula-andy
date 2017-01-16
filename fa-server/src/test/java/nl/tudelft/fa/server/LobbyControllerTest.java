package nl.tudelft.fa.server;

import static org.junit.Assert.*;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.model.headers.BasicHttpCredentials;
import akka.http.javadsl.model.headers.HttpCredentials;
import akka.http.javadsl.testkit.JUnitRouteTest;
import akka.http.javadsl.testkit.TestRoute;
import akka.pattern.Patterns;
import akka.testkit.JavaTestKit;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.auth.actor.Authenticator;
import nl.tudelft.fa.core.lobby.Lobby;
import nl.tudelft.fa.core.lobby.LobbyBalancer;
import nl.tudelft.fa.core.lobby.LobbyConfiguration;
import nl.tudelft.fa.core.lobby.actor.LobbyBalancerActor;
import nl.tudelft.fa.core.lobby.message.RequestInformation;
import nl.tudelft.fa.core.lobby.schedule.StaticLobbyScheduleFactory;
import nl.tudelft.fa.core.user.User;
import nl.tudelft.fa.server.controller.LobbyController;
import nl.tudelft.fa.server.helper.jackson.LobbyModule;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Collections;
import java.util.UUID;


public class LobbyControllerTest extends JUnitRouteTest {
    private static EntityManagerFactory factory;
    private static EntityManager manager;
    private LobbyConfiguration configuration;
    private ActorRef authenticator;
    private ActorRef balancer;
    private TestRoute route;
    private ObjectMapper mapper;

    @BeforeClass
    public static void setUpClass() {
        factory = Persistence.createEntityManagerFactory( "formula-andy-test" );
        manager = factory.createEntityManager();
        manager.getTransaction().begin();
        manager.persist(new User(UUID.randomUUID(), new Credentials("fabianishere", "test")));
        manager.getTransaction().commit();
    }


    @AfterClass
    public static void tearDownClass() {
        factory.close();
        factory = null;
    }

    @Before
    public void setUp() {
        configuration = new LobbyConfiguration(11, java.time.Duration.ofMinutes(5), java.time.Duration.ofMinutes(3), new StaticLobbyScheduleFactory(Collections.emptyList()));
        authenticator = system().actorOf(Authenticator.props(manager));
        balancer = system().actorOf(LobbyBalancerActor.props(configuration, 2, 10));
        route = testRoute(new LobbyController(system(), authenticator, balancer).createRoute());
        mapper = new ObjectMapper();
        mapper.registerModule(new LobbyModule());
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testLobbies() throws Exception {
        String content = route.run(HttpRequest.GET("/"))
            .assertStatusCode(StatusCodes.OK)
            .assertContentType(ContentTypes.APPLICATION_JSON)
            .entityString();
        JsonNode node = mapper.readTree(content).findPath("lobbies");
        Lobby[] lobbies = mapper.convertValue(node, Lobby[].class);
        assertEquals(2, lobbies.length);
    }

    @Test
    public void testLobbyMiddlewareFound() throws Exception {
        LobbyBalancer info = (LobbyBalancer) Await.result(Patterns.ask(balancer, RequestInformation.INSTANCE, 1000), Duration.apply("1 second"));
        Lobby expected = info.getLobbies().values().iterator().next();

        route.run(HttpRequest.GET(String.format("/%s", expected.getId())))
            .assertStatusCode(StatusCodes.OK)
            .assertContentType(ContentTypes.APPLICATION_JSON);
    }

    @Test
    public void testLobbyMiddlewareNotFound() throws Exception {
        route.run(HttpRequest.GET(String.format("/%s", UUID.randomUUID())))
            .assertStatusCode(StatusCodes.NOT_FOUND);
    }

    @Test
    public void testLobbyGet() throws Exception {
        LobbyBalancer info = (LobbyBalancer) Await.result(Patterns.ask(balancer, RequestInformation.INSTANCE, 1000), Duration.apply("1 second"));
        Lobby expected = info.getLobbies().values().iterator().next();

        route.run(HttpRequest.GET(String.format("/%s", expected.getId())))
            .assertStatusCode(StatusCodes.OK)
            .assertContentType(ContentTypes.APPLICATION_JSON)
            .assertEntityAs(Jackson.unmarshaller(mapper, Lobby.class), expected);
    }

    @Test
    public void testLobbyFeedAnonymous() throws Exception {
        LobbyBalancer info = (LobbyBalancer) Await.result(Patterns.ask(balancer, RequestInformation.INSTANCE, 1000), Duration.apply("1 second"));
        Lobby expected = info.getLobbies().values().iterator().next();

        route.run(HttpRequest.GET(String.format("/%s/feed", expected.getId())))
            .assertStatusCode(StatusCodes.BAD_REQUEST);
    }

    @Test
    public void testLobbyFeedInvalidCredentials() throws Exception {
        final HttpCredentials credentials =
            BasicHttpCredentials.createBasicHttpCredentials("Peter", "pan");
        LobbyBalancer info = (LobbyBalancer) Await.result(Patterns.ask(balancer, RequestInformation.INSTANCE, 1000), Duration.apply("1 second"));
        Lobby expected = info.getLobbies().values().iterator().next();

        route.run(HttpRequest.GET(String.format("/%s/feed", expected.getId())).addCredentials(credentials))
            .assertStatusCode(StatusCodes.UNAUTHORIZED)
            .assertEntity("The supplied authentication is invalid")
            .assertHeaderExists("WWW-Authenticate", "Basic realm=\"fa\"");
    }

    @Test
    public void testLobbyFeedValidCredentials() throws Exception {
        final HttpCredentials credentials =
            BasicHttpCredentials.createBasicHttpCredentials("fabianishere", "test");
        LobbyBalancer info = (LobbyBalancer) Await.result(Patterns.ask(balancer, RequestInformation.INSTANCE, 1000), Duration.apply("1 second"));
        Lobby expected = info.getLobbies().values().iterator().next();

        route.run(HttpRequest.GET(String.format("/%s/feed", expected.getId())).addCredentials(credentials))
            .assertStatusCode(StatusCodes.BAD_REQUEST);
    }
}

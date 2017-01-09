package nl.tudelft.fa.server;

import static org.junit.Assert.*;

import akka.actor.ActorRef;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.testkit.JUnitRouteTest;
import akka.http.javadsl.testkit.TestRoute;
import akka.pattern.Patterns;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.tudelft.fa.core.lobby.Lobby;
import nl.tudelft.fa.core.lobby.LobbyBalancer;
import nl.tudelft.fa.core.lobby.LobbyConfiguration;
import nl.tudelft.fa.core.lobby.actor.LobbyBalancerActor;
import nl.tudelft.fa.core.lobby.message.RequestInformation;
import nl.tudelft.fa.server.controller.LobbyController;
import nl.tudelft.fa.server.helper.jackson.LobbyModule;
import org.junit.Before;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import java.util.UUID;


public class LobbyControllerTest extends JUnitRouteTest {
    private LobbyConfiguration configuration;
    private ActorRef balancer;
    private TestRoute route;
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        configuration = new LobbyConfiguration(11, java.time.Duration.ofMinutes(5));
        balancer = system().actorOf(LobbyBalancerActor.props(configuration, 2, 10));
        route = testRoute(new LobbyController(system(), balancer).createRoute());
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
    public void testLobbyFeed() throws Exception {
        LobbyBalancer info = (LobbyBalancer) Await.result(Patterns.ask(balancer, RequestInformation.INSTANCE, 1000), Duration.apply("1 second"));
        Lobby expected = info.getLobbies().values().iterator().next();

        route.run(HttpRequest.GET(String.format("/%s/feed", expected.getId())))
            .assertStatusCode(StatusCodes.NOT_IMPLEMENTED);
    }
}

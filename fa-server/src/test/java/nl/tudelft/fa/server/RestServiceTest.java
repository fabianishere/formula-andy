package nl.tudelft.fa.server;

import static org.junit.Assert.*;

import akka.actor.ActorRef;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.testkit.JUnitRouteTest;
import akka.http.javadsl.testkit.TestRoute;
import nl.tudelft.fa.core.lobby.LobbyConfiguration;
import nl.tudelft.fa.core.lobby.actor.LobbyBalancerActor;
import nl.tudelft.fa.core.lobby.schedule.StaticLobbyScheduleFactory;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.Collections;

public class RestServiceTest extends JUnitRouteTest {
    private LobbyConfiguration configuration;
    private ActorRef balancer;
    private TestRoute route;

    @Before
    public void setUp() {
        configuration = new LobbyConfiguration(11, Duration.ofMinutes(5), Duration.ofMinutes(5), new StaticLobbyScheduleFactory(Collections.emptyList()));
        balancer = system().actorOf(LobbyBalancerActor.props(configuration));
        route = testRoute(new RestService(system(), null, balancer, null).createRoute());
    }

    @Test
    public void testInformation() {
        String content = route.run(HttpRequest.GET("/information"))
            .assertStatusCode(StatusCodes.OK)
            .assertContentType(ContentTypes.APPLICATION_JSON)
            .entityString();
        assertTrue(content.contains("engine"));
    }

    @Test
    public void testLobbies() {
        route.run(HttpRequest.GET("/lobbies"))
            .assertStatusCode(StatusCodes.OK)
            .assertContentType(ContentTypes.APPLICATION_JSON);
    }
}

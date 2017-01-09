package nl.tudelft.fa.server;

import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.testkit.JUnitRouteTest;
import akka.http.javadsl.testkit.TestRoute;
import org.junit.Test;

import static org.junit.Assert.*;

public class RestServiceTest extends JUnitRouteTest {
    final TestRoute appRoute = testRoute(new RestService(system()).createRoute());

    @Test
    public void testInformation() {
        appRoute.run(HttpRequest.GET("/information"))
            .assertStatusCode(200)
            .assertMediaType("application/json");
    }
}

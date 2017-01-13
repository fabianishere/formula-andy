package nl.tudelft.fa.server;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.testkit.JavaTestKit;
import nl.tudelft.fa.core.lobby.LobbyConfiguration;
import nl.tudelft.fa.core.lobby.actor.LobbyActor;
import nl.tudelft.fa.server.controller.LobbyController;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.time.Duration;

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
        configuration = new LobbyConfiguration(11, java.time.Duration.ofMinutes(5), Duration.ZERO);
        lobby = system.actorOf(LobbyActor.props(configuration));
        controller = new LobbyController(system, null);
    }
}

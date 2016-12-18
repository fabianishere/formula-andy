package nl.tudelft.fa.core.lobby;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.*;

public class LobbyTest {
    private static ActorSystem system;
    private UUID id;
    private LobbyConfiguration configuration;

    @BeforeClass
    public static void setUpClass() {
        system = ActorSystem.create();
    }

    @Before
    public void setUp() {
        id = UUID.randomUUID();
        configuration = new LobbyConfiguration(11, Duration.ofMinutes(2));
    }


    @AfterClass
    public static void tearDownClass() {
        JavaTestKit.shutdownActorSystem(system);
    }

    @Test
    public void testInform() {
        new JavaTestKit(system) {
            {
                final Props props = Lobby.props(configuration);
                final ActorRef subject = system.actorOf(props, id.toString());
                final Inform req = new Inform();

                subject.tell(req, getRef());

                // await the correct response
                expectMsgEquals(duration("1 second"), new LobbyInformation(id,
                    LobbyStatus.PREPARATION, configuration, Collections.emptyList()));
            }
        };
    }
}

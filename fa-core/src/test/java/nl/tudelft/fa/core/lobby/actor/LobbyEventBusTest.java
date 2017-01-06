package nl.tudelft.fa.core.lobby.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.dispatch.sysmsg.Terminate;
import akka.testkit.JavaTestKit;
import nl.tudelft.fa.core.lobby.LobbyInformation;
import nl.tudelft.fa.core.lobby.LobbyStatus;
import nl.tudelft.fa.core.lobby.message.InformationRequest;
import nl.tudelft.fa.core.lobby.message.SubscribeRequest;
import nl.tudelft.fa.core.lobby.message.UnsubscribeRequest;
import org.junit.*;

import java.util.Collections;

public class LobbyEventBusTest {
    private static ActorSystem system;
    private Props props;
    private ActorRef bus;

    @BeforeClass
    public static void setUpClass() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void tearDownClass() {
        JavaTestKit.shutdownActorSystem(system);
    }

    @Before
    public void setUp() {
        props = LobbyEventBus.props();
        bus = system.actorOf(props);
    }

    @After
    public void tearDown() {
        system.stop(bus);
    }

    @Test
    public void testSubscribe() {
        new JavaTestKit(system) {
            {
                bus.tell(new SubscribeRequest(getRef()), getRef());
                bus.tell("Hello World", getRef());
                // await the correct response
                expectMsgEquals(duration("1 second"), "Hello World");
            }
        };
    }

    @Test
    public void testUnsubscribe() {
        new JavaTestKit(system) {
            {
                bus.tell(new SubscribeRequest(getRef()), getRef());
                bus.tell(new UnsubscribeRequest(getRef()), getRef());
                bus.tell("Hello World", getRef());
                expectNoMsg();
            }
        };
    }

    @Test
    public void testKill() {
        new JavaTestKit(system) {
            {
                JavaTestKit probe = new JavaTestKit(system);

                watch(probe.getRef());
                bus.tell(new SubscribeRequest(probe.getRef()), probe.getRef());
                system.stop(probe.getRef());
                expectMsgClass(duration("1 second"), Terminated.class);
                bus.tell("Hello World", getRef());
                probe.expectNoMsg();
            }
        };
    }
}

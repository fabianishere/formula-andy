package nl.tudelft.fa.core.lobby.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.testkit.JavaTestKit;
import nl.tudelft.fa.core.lobby.message.Subscribe;
import nl.tudelft.fa.core.lobby.message.Unsubscribe;
import org.junit.*;

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
                bus.tell(new Subscribe(getRef()), getRef());
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
                bus.tell(new Subscribe(getRef()), getRef());
                bus.tell(new Unsubscribe(getRef()), getRef());
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
                bus.tell(new Subscribe(probe.getRef()), probe.getRef());
                system.stop(probe.getRef());
                expectMsgClass(duration("1 second"), Terminated.class);
                bus.tell("Hello World", getRef());
                probe.expectNoMsg();
            }
        };
    }
}

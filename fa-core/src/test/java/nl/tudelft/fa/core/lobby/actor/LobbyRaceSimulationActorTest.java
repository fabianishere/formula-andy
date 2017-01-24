package nl.tudelft.fa.core.lobby.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.lobby.message.CarParametersSubmission;
import nl.tudelft.fa.core.lobby.message.RequestInformation;
import nl.tudelft.fa.core.lobby.message.TeamConfigurationSubmission;
import nl.tudelft.fa.core.lobby.message.TeamConfigurationSubmitted;
import nl.tudelft.fa.core.race.CarConfiguration;
import nl.tudelft.fa.core.race.CarParameters;
import nl.tudelft.fa.core.race.Circuit;
import nl.tudelft.fa.core.race.GrandPrix;
import nl.tudelft.fa.core.team.inventory.Tire;
import nl.tudelft.fa.core.team.inventory.TireType;
import nl.tudelft.fa.core.user.User;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Instant;
import java.util.HashSet;
import java.util.UUID;

public class LobbyRaceSimulationActorTest {
    private static ActorSystem system;
    private GrandPrix grandprix;
    private User user;

    @BeforeClass
    public static void setUpClass() {
        system = ActorSystem.create();
    }

    @Before
    public void setUp() {
        grandprix = new GrandPrix(UUID.randomUUID(), new Circuit(UUID.randomUUID(), "Monza", "Italy", 700), Instant.now(), 10, 1);
        user = new User(UUID.randomUUID(), new Credentials("test", "test"));
    }


    @AfterClass
    public static void tearDownClass() {
        JavaTestKit.shutdownActorSystem(system);
    }

    @Test
    public void testConfigurationCarNull() {
        new JavaTestKit(system) {
            {
                final Props props = LobbyRaceSimulationActor.props(getRef(), grandprix);
                final ActorRef subject = system.actorOf(props);
                final RequestInformation req = RequestInformation.INSTANCE;

                subject.tell(req, getRef());

                final TeamConfigurationSubmission msg = new TeamConfigurationSubmission(user, new HashSet<CarConfiguration>() {{
                    add(new CarConfiguration(null, null, null, null, null, null));
                }});

                subject.tell(msg, getRef());
                expectMsgClass(duration("1 second"), TeamConfigurationSubmitted.class);
            }
        };
    }

    @Test
    public void testParametersCarNull() {
        new JavaTestKit(system) {
            {
                final Props props = LobbyRaceSimulationActor.props(getRef(), grandprix);
                final ActorRef subject = system.actorOf(props);
                final RequestInformation req = RequestInformation.INSTANCE;

                subject.tell(req, getRef());

                final CarParametersSubmission msg = new CarParametersSubmission(user, null,
                    new CarParameters(1, 1, 1, new Tire(UUID.randomUUID(), "", TireType.HARD, 1, 1)));


                subject.tell(msg, getRef());
                expectNoMsg(duration("1 second"));
            }
        };
    }

}

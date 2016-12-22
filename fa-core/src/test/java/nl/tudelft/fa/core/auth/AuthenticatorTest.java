package nl.tudelft.fa.core.auth;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import nl.tudelft.fa.core.auth.message.AuthenticationRequest;
import nl.tudelft.fa.core.auth.message.AuthenticationSuccess;
import nl.tudelft.fa.core.auth.message.InvalidCredentials;
import nl.tudelft.fa.core.user.User;
import org.junit.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.UUID;

public class AuthenticatorTest {

    private static ActorSystem system;
    private static EntityManagerFactory factory;
    private static EntityManager manager;

    @BeforeClass
    public static void setUp() {
        system = ActorSystem.create();
        factory = Persistence.createEntityManagerFactory( "formula-andy-test" );
        manager = factory.createEntityManager();
        manager.getTransaction().begin();
        manager.persist(new User(UUID.randomUUID(), new Credentials("fabianishere", "test")));
        manager.getTransaction().commit();
    }


    @AfterClass
    public static void tearDown() {
        JavaTestKit.shutdownActorSystem(system);
        factory.close();
        system = null;
        factory = null;
    }

    @Test
    public void authenticateSuccess() {
        new JavaTestKit(system) {
            {
                final Props props = Authenticator.props(manager);
                final ActorRef subject = system.actorOf(props);
                final AuthenticationRequest req = new AuthenticationRequest(new Credentials("fabianishere", "test"));

                subject.tell(req, getRef());

                // await the correct response
                expectMsgClass(duration("1 second"), AuthenticationSuccess.class);
            }
        };
    }

    @Test
    public void authenticateInvalidInvalidUsername() {
        new JavaTestKit(system) {
            {
                final Props props = Authenticator.props(manager);
                final ActorRef subject = system.actorOf(props);
                final AuthenticationRequest req = new AuthenticationRequest(new Credentials("unknown", "test"));

                subject.tell(req, getRef());

                // await the correct response
                expectMsgClass(duration("1 second"), InvalidCredentials.class);
            }
        };
    }

    @Test
    public void authenticateInvalidInvalidPassword() {
        new JavaTestKit(system) {
            {
                final Props props = Authenticator.props(manager);
                final ActorRef subject = system.actorOf(props);
                final AuthenticationRequest req = new AuthenticationRequest(new Credentials("fabianishere", "incorrect"));

                subject.tell(req, getRef());

                // await the correct response
                expectMsgClass(duration("1 second"), InvalidCredentials.class);
            }
        };
    }
}

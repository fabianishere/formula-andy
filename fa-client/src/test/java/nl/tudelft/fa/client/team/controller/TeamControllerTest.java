package nl.tudelft.fa.client.team.controller;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Uri;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import nl.tudelft.fa.client.Client;
import nl.tudelft.fa.client.lobby.controller.HttpMock;
import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.auth.actor.Authenticator;
import nl.tudelft.fa.core.team.*;
import nl.tudelft.fa.core.team.inventory.*;
import nl.tudelft.fa.core.user.User;
import nl.tudelft.fa.server.RestService;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class TeamControllerTest {
    private static ActorSystem system;
    private static ActorMaterializer materializer;
    private static EntityManagerFactory factory;
    private static ActorRef authenticator;
    private static ActorRef balancer;
    private static EntityManager manager;

    private Client client;
    private TeamController controller;
    private nl.tudelft.fa.client.auth.Credentials credentials;

    @BeforeClass
    public static void setUpClass() {
        system = ActorSystem.create();
        materializer = ActorMaterializer.create(system);
        factory = Persistence.createEntityManagerFactory("formula-andy-test");
        manager = factory.createEntityManager();

        // Create simple user
        User user = new User(UUID.randomUUID(), new Credentials("fabianishere", "test"));
        List<InventoryItem> inventory = new ArrayList<InventoryItem>();
        List<Member> staff = new ArrayList<>();
        final Team team = new Team(UUID.randomUUID(), "test", 30000, user, staff, inventory);
        final Car car = new Car(UUID.randomUUID());
        final Engine engine = new Engine(UUID.randomUUID(), "Ferrari", "X", 1, 2, 3);
        final Tire tire = new Tire(UUID.randomUUID(), "Pirelli", TireType.HARD, 1, 1);
        final Driver driver = new Driver(UUID.randomUUID(), "Louis", 1000, 1, 1, 1);
        final Aerodynamicist aerodynamicist = new Aerodynamicist(UUID.randomUUID(), "A", 100, 1);
        final Mechanic mechanic = new Mechanic(UUID.randomUUID(), "B", 100, 1);
        final Strategist strategist = new Strategist(UUID.randomUUID(), "C", 100, 1);
        inventory.add(car);
        inventory.add(engine);
        inventory.add(tire);
        staff.add(driver);
        staff.add(aerodynamicist);
        staff.add(mechanic);
        staff.add(strategist);
        manager.getTransaction().begin();
        manager.persist(user);
        manager.persist(team);
        manager.persist(driver);
        manager.persist(aerodynamicist);
        manager.persist(mechanic);
        manager.persist(strategist);
        manager.persist(car);
        manager.persist(engine);
        manager.persist(tire);
        manager.getTransaction().commit();
        authenticator = system.actorOf(Authenticator.props(manager));
    }

    @Before
    public void setUp() {
        credentials = new nl.tudelft.fa.client.auth.Credentials("fabianishere", "test");
        final RestService app = new RestService(system, authenticator, balancer, manager);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
        client = new Client(new HttpMock(routeFlow), materializer, Uri.create("http://localhost:8080"), credentials);
        controller = client.teams();
    }

    @AfterClass
    public static void tearDownClass() {
        system.terminate();
        factory.close();
    }

    @Test
    public void list() throws Exception {
        System.out.println(controller.list().toCompletableFuture().get());
    }
}

package nl.tudelft.fa.server;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import nl.tudelft.fa.core.auth.Credentials;
import nl.tudelft.fa.core.auth.actor.Authenticator;
import nl.tudelft.fa.core.lobby.LobbyConfiguration;
import nl.tudelft.fa.core.lobby.actor.LobbyBalancerActor;
import nl.tudelft.fa.core.lobby.schedule.StaticLobbyScheduleFactory;
import nl.tudelft.fa.core.race.Circuit;
import nl.tudelft.fa.core.race.GrandPrix;
import nl.tudelft.fa.core.team.*;
import nl.tudelft.fa.core.team.inventory.*;
import nl.tudelft.fa.core.user.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletionStage;

public class ServerTest {
    public static void main(String[] args) throws IOException {
        // boot up server using the route as defined below
        ActorSystem system = ActorSystem.create();

        final EntityManagerFactory factory = Persistence.createEntityManagerFactory( "formula-andy-test" );
        final EntityManager manager = factory.createEntityManager();

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

        final ActorRef authenticator = system.actorOf(Authenticator.props(manager));
        final ActorRef balancer = system.actorOf(LobbyBalancerActor.props(new LobbyConfiguration(11, Duration.ofMinutes(1), Duration.ofMinutes(1), new StaticLobbyScheduleFactory(Collections.singletonList(new GrandPrix(UUID.randomUUID(), new Circuit(UUID.randomUUID(), "Monza", "Italy", 700), Instant.now(), 10, 1))))));

        // HttpApp.bindRoute expects a route being provided by HttpApp.createRoute
        final RestService app = new RestService(system, authenticator, balancer, manager);

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow, ConnectHttp.toHost("localhost", 8080), materializer);

        System.out.println("Type RETURN to exit");
        System.in.read();

        binding
            .thenCompose(ServerBinding::unbind)
            .thenAccept(unbound -> system.terminate());
    }
}

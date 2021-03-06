package nl.tudelft.fa.server;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ServerTest {
    public static void main(String[] args) throws IOException {
        // boot up server using the route as defined below
        ActorSystem system = ActorSystem.create();

        final EntityManagerFactory factory = Persistence.createEntityManagerFactory( "formula-andy-test" );
        final EntityManager manager = factory.createEntityManager();

        setupUser(new Credentials("fabianishere", "test"), manager);
        setupUser2(new Credentials("christov",  "test"), manager);

        final ActorRef authenticator = system.actorOf(Authenticator.props(manager));
        final ActorRef balancer = system.actorOf(LobbyBalancerActor.props(new LobbyConfiguration(11, Duration.ofMinutes(1), Duration.ofMinutes(1), new StaticLobbyScheduleFactory(Collections.singletonList(new GrandPrix(UUID.randomUUID(), new Circuit(UUID.randomUUID(), "Monza", "Italy", 10000), Instant.now(), 10, 1))))));

        // HttpApp.bindRoute expects a route being provided by HttpApp.createRoute
        final RestService app = new RestService(system, authenticator, balancer, manager);

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
        http.bindAndHandle(routeFlow, ConnectHttp.toHost("localhost", 8080), materializer);
    }

    public static void setupUser(Credentials credentials, EntityManager manager) {
        User user = new User(UUID.randomUUID(), credentials);
        List<InventoryItem> inventory = new ArrayList<InventoryItem>();
        List<Member> staff = new ArrayList<>();
        final nl.tudelft.fa.core.team.Team team = new nl.tudelft.fa.core.team.Team(UUID.randomUUID(), "Redbull Racing", 30000, user, staff, inventory);
        final Car car = new Car(UUID.randomUUID());
        final Engine engine = new Engine(UUID.randomUUID(), "Mercedes", "F1 W05 Hybrid (1)", 100, 80, 85);
        final Tire tire = new Tire(UUID.randomUUID(), "Pirelli", TireType.SUPER_SOFT, 7, 1);
        final Driver driver = new Driver(UUID.randomUUID(), "Max Verstappen", 1000, 80, 90, 70);
        final Aerodynamicist aerodynamicist = new Aerodynamicist(UUID.randomUUID(), "Fred", 100, 80);
        final Mechanic mechanic = new Mechanic(UUID.randomUUID(), "Harry", 35, 80);
        final Strategist strategist = new Strategist(UUID.randomUUID(),"Louis", 100, 80);

        final Car car2 = new Car(UUID.randomUUID());
        final Engine engine2 = new Engine(UUID.randomUUID(), "Mercedes", "F1 W05 Hybrid (2)", 100, 80, 85);
        final Tire tire2 = new Tire(UUID.randomUUID(), "Pirelli", TireType.SUPER_SOFT, 7, 1);
        final Driver driver2 = new Driver(UUID.randomUUID(), "Daniel Ricciardo ", 1000, 80, 90, 70);
        final Aerodynamicist aerodynamicist2 = new Aerodynamicist(UUID.randomUUID(), "Fred", 100, 80);
        final Mechanic mechanic2 = new Mechanic(UUID.randomUUID(), "Steve", 35, 80);
        final Strategist strategist2 = new Strategist(UUID.randomUUID(),"Hank", 100, 80);

        inventory.add(car);
        inventory.add(engine);
        inventory.add(tire);
        staff.add(driver);
        staff.add(aerodynamicist);
        staff.add(mechanic);
        staff.add(strategist);

        inventory.add(car2);
        inventory.add(engine2);
        inventory.add(tire2);
        staff.add(driver2);
        staff.add(aerodynamicist2);
        staff.add(mechanic2);
        staff.add(strategist2);

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

        manager.persist(driver2);
        manager.persist(aerodynamicist2);
        manager.persist(mechanic2);
        manager.persist(strategist2);
        manager.persist(car2);
        manager.persist(engine2);
        manager.persist(tire2);
        manager.getTransaction().commit();
    }


    public static void setupUser2(Credentials credentials, EntityManager manager) {
        User user = new User(UUID.randomUUID(), credentials);
        List<InventoryItem> inventory = new ArrayList<InventoryItem>();
        List<Member> staff = new ArrayList<>();
        final nl.tudelft.fa.core.team.Team team = new nl.tudelft.fa.core.team.Team(UUID.randomUUID(), "Ferrari Formula 1", 30000, user, staff, inventory);
        final Car car = new Car(UUID.randomUUID());
        final Engine engine = new Engine(UUID.randomUUID(), "Mercedes", "F1 W05 Hybrid", 100, 80, 85);
        final Tire tire = new Tire(UUID.randomUUID(), "Pirelli", TireType.SUPER_SOFT, 7, 1);
        final Driver driver = new Driver(UUID.randomUUID(), "Max Verstappen", 1000, 80, 90, 70);
        final Aerodynamicist aerodynamicist = new Aerodynamicist(UUID.randomUUID(), "Fred", 100, 80);
        final Mechanic mechanic = new Mechanic(UUID.randomUUID(), "Harry", 35, 80);
        final Strategist strategist = new Strategist(UUID.randomUUID(),"Louis", 100, 80);

        final Car car2 = new Car(UUID.randomUUID());
        final Engine engine2 = new Engine(UUID.randomUUID(), "Renault", "Energy F1-2016", 100, 80, 85);
        final Tire tire2 = new Tire(UUID.randomUUID(), "Pirelli", TireType.SUPER_SOFT, 7, 1);
        final Driver driver2 = new Driver(UUID.randomUUID(), "Sonic BOOM", 1000, 80, 90, 70);
        final Aerodynamicist aerodynamicist2 = new Aerodynamicist(UUID.randomUUID(), "Fred", 100, 80);
        final Mechanic mechanic2 = new Mechanic(UUID.randomUUID(), "Harry", 35, 80);
        final Strategist strategist2 = new Strategist(UUID.randomUUID(),"Louis", 100, 80);

        inventory.add(car);
        inventory.add(engine);
        inventory.add(tire);
        staff.add(driver);
        staff.add(aerodynamicist);
        staff.add(mechanic);
        staff.add(strategist);

        inventory.add(car2);
        inventory.add(engine2);
        inventory.add(tire2);
        staff.add(driver2);
        staff.add(aerodynamicist2);
        staff.add(mechanic2);
        staff.add(strategist2);

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

        manager.persist(driver2);
        manager.persist(aerodynamicist2);
        manager.persist(mechanic2);
        manager.persist(strategist2);
        manager.persist(car2);
        manager.persist(engine2);
        manager.persist(tire2);
        manager.getTransaction().commit();
    }
}

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
import nl.tudelft.fa.core.team.Team;
import nl.tudelft.fa.core.team.inventory.Car;
import nl.tudelft.fa.core.team.inventory.InventoryItem;
import nl.tudelft.fa.core.user.User;
import sun.misc.UUDecoder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

public class ServerTest {
    public static void main(String[] args) throws IOException {
        // boot up server using the route as defined below
        ActorSystem system = ActorSystem.create();

        final EntityManagerFactory factory = Persistence.createEntityManagerFactory( "formula-andy-test" );
        final EntityManager manager = factory.createEntityManager();

        User user = new User(UUID.randomUUID(), new Credentials("fabianishere", "test"));
        List<InventoryItem> inventory = new ArrayList<InventoryItem>();
        final Team team = new Team(UUID.randomUUID(), "test", 30000, user, Collections.emptyList(), inventory);
        final Car car = new Car(UUID.randomUUID(), team);
        inventory.add(car);
        manager.getTransaction().begin();
        manager.persist(user);
        manager.persist(team);
        manager.persist(car);
        manager.getTransaction().commit();

        final ActorRef authenticator = system.actorOf(Authenticator.props(manager));
        final ActorRef balancer = system.actorOf(LobbyBalancerActor.props(new LobbyConfiguration(11, Duration.ofMinutes(1), Duration.ofMinutes(3), new StaticLobbyScheduleFactory(Collections.emptyList()))));

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

package nl.tudelft.fa.frontend.javafx.controller;

import akka.actor.ActorSystem;
import akka.http.javadsl.model.Uri;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import nl.tudelft.fa.client.Client;
import nl.tudelft.fa.client.auth.Credentials;
import nl.tudelft.fa.frontend.javafx.Main;

/**
 * Created by laeti on 9-1-2017.
 */
public class LoginController {
    @FXML private TextField username;
    @FXML private PasswordField password;
    private Client client = new Client(ActorSystem.create(), Uri.create("http://localhost:8080"));

    /**
     * This method is invoked when the login game button is pressed and the user wants to start
     * playing the game.
     *
     * @param event The {@link ActionEvent} that occurred.
     */
    @FXML
    protected void login(ActionEvent event) throws Exception {
        Credentials credentials = new Credentials(username.getText(), password.getText());
        client.authorize(credentials).balancer().find().toCompletableFuture().get();
        Main.launchScreen(event, "start-screen.fxml");
    }

    @FXML
    protected void signupnow(ActionEvent event) throws Exception {
        Main.launchScreen(event, "signup-screen.fxml");
    }

}

package nl.tudelft.fa.frontend.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import nl.tudelft.fa.client.auth.Credentials;
import nl.tudelft.fa.frontend.javafx.Main;
import nl.tudelft.fa.frontend.javafx.service.ClientService;
import javax.inject.Inject;

/**
 *
 *@author Laetitia Molkenboer & Fabian Mastenbroek & Christian Slothouber.
 */
public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @Inject
    private ClientService service;

    /**
     * This method is invoked when the login game button is pressed and the user wants to start
     * playing the game.
     *
     * @param event The {@link ActionEvent} that occurred.
     */
    @FXML
    protected void login(ActionEvent event) throws Exception {
        Credentials credentials = new Credentials(username.getText(), password.getText());
        service.getClient().authorize(credentials)
            .balancer()
            .find()
            .toCompletableFuture()
            .get()
            .feed();
        Main.launchScreen(event, "start-screen.fxml");
    }

    @FXML
    protected void signupnow(ActionEvent event) throws Exception {
        Main.launchScreen(event, "signup-screen.fxml");
    }
}

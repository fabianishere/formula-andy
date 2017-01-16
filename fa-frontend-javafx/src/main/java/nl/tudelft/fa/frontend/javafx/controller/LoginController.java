package nl.tudelft.fa.frontend.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.fa.frontend.javafx.Main;

/**
 * Created by laeti on 9-1-2017.
 */
public class LoginController {
    /**
     * This method is invoked when the login game button is pressed and the user wants to start
     * playing the game.
     *
     * @param event The {@link ActionEvent} that occurred.
     */
    @FXML
    protected void login(ActionEvent event) throws Exception {
        Main.launchScreen(event, "setup-screen.fxml");
    }

    @FXML
    protected void signupnow(ActionEvent event) throws Exception {
        Main.launchScreen(event, "signup-screen.fxml");
    }

}

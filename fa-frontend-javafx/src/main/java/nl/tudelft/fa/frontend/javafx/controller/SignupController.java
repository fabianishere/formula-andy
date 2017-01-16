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
public class SignupController {

    /**
     * This method is invoked when the signup game button is pressed and the user wants to create a new account
     *
     * @param event The {@link ActionEvent} that occurred.
     */
    @FXML
    protected void signup(ActionEvent event) throws Exception {
        Main.launchScreen(event, "teamname-screen.fxml");
    }

    @FXML
    protected void back(ActionEvent event) throws Exception {
        Main.launchScreen(event, "login-screen.fxml");
    }
}

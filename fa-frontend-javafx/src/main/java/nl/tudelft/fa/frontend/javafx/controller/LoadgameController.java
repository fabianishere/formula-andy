package nl.tudelft.fa.frontend.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.fa.client.Client;
import nl.tudelft.fa.frontend.javafx.Main;

/**
 * Created by laeti on 9-1-2017.
 */
public class LoadgameController {
    private Client client;

    @FXML
    protected void back(ActionEvent event) throws Exception {
        Main.launchScreen(event, "start-screen.fxml");
    }

    @FXML
    protected void next(ActionEvent event) throws Exception {
        //client.authorize(credentials).teams().list().toCompletableFuture().get();
        Main.launchScreen(event, "setup-screen.fxml");
    }
}

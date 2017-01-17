package nl.tudelft.fa.frontend.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.fa.frontend.javafx.Main;
import nl.tudelft.fa.frontend.javafx.scene.SetupScreenScene;

/**
 * Created by fchri on 15-1-2017.
 */
public class StoreController {
    @FXML
    protected void back(ActionEvent event) throws Exception {
        Main.launchScreen(event, "setup-screen.fxml");
    }
}


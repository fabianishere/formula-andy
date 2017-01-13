package nl.tudelft.fa.frontend.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by laeti on 9-1-2017.
 */
public class StartScreenController {

    /**
     * This method is invoked when the signup game button is pressed and the user wants to create a new account
     *
     * @param event The {@link ActionEvent} that occurred.
     */
    @FXML
    protected void loadGame(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../load-screen.fxml"));

        Scene scene = new Scene(root);
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();

        stage.setTitle("Formula Andy!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    @FXML
    protected void newGame(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../teamname-screen.fxml"));

        Scene scene = new Scene(root);
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();

        stage.setTitle("Formula Andy!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }
}

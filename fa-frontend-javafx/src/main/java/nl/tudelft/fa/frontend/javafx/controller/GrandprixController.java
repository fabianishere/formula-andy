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
public class GrandprixController {
    /**
     * This method is invoked when the login game button is pressed and the user wants to start
     * playing the game.
     *
     * @param event The {@link ActionEvent} that occurred.
     */
    @FXML
    protected void back(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../load-screen.fxml"));

        Scene scene = new Scene(root);
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();

        stage.setTitle("Formula Andy!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }


}

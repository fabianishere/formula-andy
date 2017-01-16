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
public class SetupScreenController {

    @FXML
    protected void back(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../teamname-screen.fxml"));

        Scene scene = new Scene(root);
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();

        stage.setTitle("Formula Andy!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    @FXML
    protected void store(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../store-screen.fxml"));

        Scene scene = new Scene(root);
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();

        stage.setTitle("Formula Andy!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    @FXML
    protected void next(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../grandprix-screen.fxml"));

        Scene scene = new Scene(root);
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();

        stage.setTitle("Formula Andy!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    @FXML
    protected void start(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../game-screen.fxml"));

        Scene scene = new Scene(root);
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();

        stage.setTitle("Formula Andy!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }


}

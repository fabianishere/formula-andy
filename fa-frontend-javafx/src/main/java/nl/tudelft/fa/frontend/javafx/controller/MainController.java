package nl.tudelft.fa.frontend.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainController {
    @FXML
    protected void newGame(ActionEvent event) {
        System.out.println("NEW GAME!!");
    }

    @FXML
    protected void loadGame(ActionEvent event) {
        System.out.println("Loading.....");
    }

    @FXML
    protected void openSettings(ActionEvent event) {
        System.out.println("SETTINGS yeey");
    }
}

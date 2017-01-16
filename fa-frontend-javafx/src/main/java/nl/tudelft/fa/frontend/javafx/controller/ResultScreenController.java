package nl.tudelft.fa.frontend.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import nl.tudelft.fa.frontend.javafx.Main;

/**
 * Created by fchri on 16-1-2017.
 */
public class ResultScreenController {

    @FXML
    protected void next(ActionEvent event) throws Exception {
        Main.launchScreen(event, "setup-screen.fxml");
    }
}

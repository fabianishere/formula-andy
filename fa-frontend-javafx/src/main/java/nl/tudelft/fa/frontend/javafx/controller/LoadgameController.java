package nl.tudelft.fa.frontend.javafx.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import nl.tudelft.fa.client.auth.Credentials;
import nl.tudelft.fa.client.team.Team;
import nl.tudelft.fa.frontend.javafx.Main;
import nl.tudelft.fa.frontend.javafx.service.ClientService;
import javax.inject.Inject;
import java.util.List;

/**
 *
 * @author Laetitia Molkenboer & Christian Slothouber.
 */
public class LoadgameController {
    @FXML
    private ComboBox<Team> saves;

    private List<Team> teams;

    @Inject
    private ClientService service;

    @FXML
    private void initialize() {
        try {
            teams = service.getClient().authorize(new Credentials("fabianishere", "test")).teams().list().toCompletableFuture().get();
            saves.setItems(FXCollections.observableArrayList(teams));
        } catch (Exception e) {
            System.out.println("Failed to load teams");
        }
    }

    @FXML
    protected void back(ActionEvent event) throws Exception {
        Main.launchScreen(event, "start-screen.fxml");
    }

    @FXML
    protected void next(ActionEvent event) throws Exception {
        //currentTeam = saves.getValue();
        Main.launchScreen(event, "setup-screen.fxml");
    }
}

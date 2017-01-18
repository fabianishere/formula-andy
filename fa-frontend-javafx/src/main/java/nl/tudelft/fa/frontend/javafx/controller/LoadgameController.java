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

    /**
     *
     * The Combobox where the saves will be displayed and
     * will be chosen to play.
     */
    @FXML
    private ComboBox<Team> saves;

    /**
     *
     * The current connection with the server.
     */
    @Inject
    private ClientService service;

    /**
     *
     * This method is called when the screen is loaded.
     */
    @FXML
    private void initialize() {
        try {
            List<Team> teams = service.getClient().authorize(new Credentials("fabianishere", "test")).teams().list().toCompletableFuture().get();
            saves.setItems(FXCollections.observableArrayList(teams));
        } catch (Exception e) {
            System.out.println("Failed to load teams");
        }
    }

    /**
     *
     * This method is called when the back-button is pressed.
     *
     * @param event The event
     * @throws Exception If it fails the method throws an Exception.
     */
    @FXML
    protected void back(ActionEvent event) throws Exception {
        Main.launchScreen(event, "start-screen.fxml");
    }

    /**
     *
     * This method is called when the next-button is pressed.
     *
     * @param event The event
     * @throws Exception If it fails the method throws an Exception.
     */
    @FXML
    protected void next(ActionEvent event) throws Exception {
        Main.setCurrentTeam(saves.getValue());
        Main.launchScreen(event, "setup-screen.fxml");
    }
}

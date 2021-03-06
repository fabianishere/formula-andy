/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Fabian Mastenbroek, Christian Slothouber,
 * Laetitia Molkenboer, Nikki Bouman, Nils de Beukelaar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package nl.tudelft.fa.frontend.javafx.controller.user;

import akka.Done;
import akka.stream.StreamTcpException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import nl.tudelft.fa.client.auth.Credentials;
import nl.tudelft.fa.frontend.javafx.Main;
import nl.tudelft.fa.frontend.javafx.controller.AbstractController;
import nl.tudelft.fa.frontend.javafx.controller.lobby.LobbySelectionController;
import nl.tudelft.fa.frontend.javafx.dispatch.JavaFxExecutorService;
import nl.tudelft.fa.frontend.javafx.service.ClientService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.inject.Inject;

/**
 * A controller for the login screen of the game.
 *
 * @author Fabian Mastenbroek
 * @author Christian Slothouber
 * @author Laetitia Molkenboer
 */
public class LoginController extends AbstractController {
    /**
     * The reference to the location of the view of this controller.
     */
    public static final URL VIEW = Main.class.getResource("view/user/login.fxml");

    /**
     * The username field of the view.
     */
    @FXML
    private TextField username;

    /**
     * The password field of the view.
     */
    @FXML
    private PasswordField password;

    /**
     * The alert to show if the credentials are invalid.
     */
    @FXML
    private Node alert;

    /**
     * The alert label where the error message is contained.
     */
    @FXML
    private Label alertLabel;

    /**
     * The injected client service.
     */
    @Inject
    private ClientService service;

    /**
     * This method is invoked when the login game button is pressed and the user wants to start
     * playing the game.
     *
     * @param event The {@link ActionEvent} that occurred.
     */
    @FXML
    protected void login(ActionEvent event) throws Exception {
        Credentials credentials = new Credentials(username.getText(), password.getText());
        logger.info("User logging in with credentials {}", credentials);

        // Authorize the user
        service.authorize(credentials);
        service.test().whenCompleteAsync(this::login, JavaFxExecutorService.INSTANCE);
    }

    /**
     * Handle the login event of a user.
     *
     * @param done A parameter to indicate the user successfully logged in.
     * @param throwable An exception that occurred when authenticating.
     */
    private void login(Done done, Throwable throwable) {
        if (throwable != null) {
            String msg = throwable.getCause().getMessage();

            if (throwable.getCause() instanceof StreamTcpException) {
                msg = "The server is currently not available";
            }

            alert.setVisible(true);
            alertLabel.setText(msg);
            return;
        }

        // Show the start screen
        try {
            alert.setVisible(false);
            show(LobbySelectionController.VIEW);
        } catch (IOException exc) {
            logger.error("Failed to load start screen for user", exc);

            alert.setVisible(true);
            alertLabel.setText(exc.getMessage());
        }
    }

    /**
     * This method is invoked when the register button is pressed.
     *
     * @param event The event that occurred.
     * @throws Exception if the view failed to load.
     */
    @FXML
    protected void register(ActionEvent event) throws Exception {
        push(RegisterController.VIEW);
    }

    /**
     * This method is called when the screen is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        alert.setVisible(false);
        alert.managedProperty().bind(alert.visibleProperty());
    }
}

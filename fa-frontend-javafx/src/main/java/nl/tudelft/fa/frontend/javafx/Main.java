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

package nl.tudelft.fa.frontend.javafx;

import com.gluonhq.ignite.guice.GuiceContext;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.fa.client.team.Team;
import nl.tudelft.fa.frontend.javafx.inject.UIModule;
import javax.inject.Inject;
import java.util.Arrays;

/**
 * The main JavaFX {@link Application} class.
 *
 * @author Fabian Mastenbroek
 */
public class Main extends Application {

    /**
     *
     * The current played Team.
     */
    private static Team currentTeam;

    /**
     * The {@link com.gluonhq.ignite.guice.GuiceContext} to use.
     */
    private GuiceContext context = new GuiceContext(this, () ->
        Arrays.asList(new UIModule()));

    /**
     * The {@link FXMLLoader} to load the views with.
     */
    @Inject
    private FXMLLoader loader;

    /**
     * This method is called when the application is started.
     *
     * {@inheritDoc}
     */
    @Override
    public void start(Stage stage) throws Exception {
        context.init();
        loader.setLocation(getClass().getResource("loadgame-screen.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setTitle("Formula Andy!");
        stage.setScene(scene);
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);

        stage.show();
    }

    /**
     *
     * When this method is invoked it shows the screen with the provided resource.
     *
     * @param event The event that occurred.
     * @param resourceURL The URL to the resource that needs to be shown.
     */
    public static void launchScreen(Event event, String resourceURL) {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource(resourceURL));

            Scene scene = new Scene(root);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            stage.setTitle("Formula Andy!");
            stage.setScene(scene);
            stage.setFullScreenExitHint("");
            stage.setFullScreen(true);
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * Get the currently played Team.
     *
     * @return The currently played Team.
     */
    public static Team getCurrentTeam() {
        return currentTeam;
    }

    /**
     *
     * Set the currently played Team.
     *
     * @param currentTeam The currently played Team.
     */
    public static void setCurrentTeam(Team currentTeam) {
        Main.currentTeam = currentTeam;
    }
}

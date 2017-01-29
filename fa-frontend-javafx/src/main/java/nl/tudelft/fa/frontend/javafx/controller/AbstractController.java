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

package nl.tudelft.fa.frontend.javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.inject.Inject;


/**
 * This class implemented by all controllers in this package.
 *
 * @author Fabian Mastenbroek
 */
public abstract class AbstractController implements Initializable {
    /**
     * The {@link FXMLLoader} that loads the views.
     */
    @Inject
    protected FXMLLoader loader;

    /**
     * The {@link Logger} instance to use.
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * The root node of the view.
     */
    @FXML
    protected Parent root;

    /**
     * Load the view located at the given url into the given stage disregarding the view stack.
     *
     * @param stage The stage to show the view into.
     * @param location The relative location of the view.
     * @throws IOException if the view fails to show.
     */
    public void show(Stage stage, URL location) throws IOException {
        loader.setLocation(location);
        loader.setRoot(null);
        loader.setController(null);

        StackPane stack = new StackPane();
        stack.getChildren().add(loader.load());

        // replace root of scene so the window does not resize
        stage.getScene().setRoot(stack);
    }

    /**
     * Load the view located at the given url disregarding the view stack.
     *
     * @param location The relative location of the view.
     * @throws IOException if the view fails to show.
     */
    public void show(URL location) throws IOException {
        show((Stage) root.getScene().getWindow(), location);
    }

    /**
     * Push the view located at the given url to the view stack.
     *
     * @param stage The stage to show the view into.
     * @param location The relative location of the view.
     * @throws IOException if the view fails to show.
     */
    public void push(Stage stage, URL location) throws IOException {
        loader.setLocation(location);
        loader.setRoot(null);
        loader.setController(null);

        Parent root = stage.getScene().getRoot();
        StackPane stack;

        if (root instanceof StackPane)  {
            stack = (StackPane) root;
        } else {
            stack = new StackPane();
            stage.getScene().setRoot(stack);
            stack.getChildren().add(root);
        }

        stack.getChildren().add(loader.load());
    }

    /**
     * Push the view located at the given url to the view stack.
     *
     * @param location The relative location of the view.
     * @throws IOException if the view fails to show.
     */
    public void push(URL location) throws IOException {
        push((Stage) root.getScene().getWindow(), location);
    }

    /**
     * Pop the top view in the view stack.
     *
     * @param stage The stage to pop the view of.
     */
    public void pop(Stage stage) {
        StackPane root = (StackPane) stage.getScene().getRoot();
        int index = root.getChildren().size() - 1;
        if (index >= 0) {
            root.getChildren().remove(index);
        }
    }

    /**
     * Pop the view located at the given url to the view stack.
     */
    public void pop() {
        pop((Stage) root.getScene().getWindow());
    }

    /**
     * This method is called when the screen is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root = loader.getRoot();
    }
}

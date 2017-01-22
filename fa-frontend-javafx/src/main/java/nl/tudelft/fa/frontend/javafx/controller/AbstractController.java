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

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;

/**
 * This class implemented by all controllers in this package.
 *
 * @author Fabian Mastenbroek
 */
public abstract class AbstractController {
    /**
     * The {@link FXMLLoader} that loads the views.
     */
    @Inject
    private FXMLLoader loader;

    /**
     * The {@link Logger} instance to use.
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

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

        stage.setScene(new Scene(loader.load()));
    }

    /**
     * Load the view located at the given url given an {@link Event} disregarding the view stack.
     *
     * @param event The event that has occurred.
     * @param location The relative location of the view.
     */
    public void show(Event event, URL location) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        show(stage, location);
    }

    /**
     * Load the view located at the given url disregarding the view stack.
     *
     * @param location The relative location of the view.
     * @throws IOException if the view fails to show.
     */
    public void show(URL location) throws IOException {
        Node node = loader.getRoot();
        show((Stage) node.getScene().getWindow(), location);
    }
}

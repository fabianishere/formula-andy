/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Fabian Mastenbroek, Christian Slothouber,
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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * The controller for the start screen.
 *
 * @author Nikki Bouman
 */
public class StartScreenController {
    /**
     * This method is invoked when the new game button is pressed and the user wants to start
     * a new game.
     *
     * @param event The {@link ActionEvent} that occurred.
     */
    @FXML
    protected void newGame(ActionEvent event) {
        System.out.println("NEW GAME!!");
    }

    /**
     * This method is invoked when the load game button is pressed and the user wants to load
     * an existing game.
     *
     * @param event The {@link ActionEvent} that occurred.
     */
    @FXML
    protected void loadGame(ActionEvent event) {
        System.out.println("Loading.....");
    }

    /**
     * This method is invoked when the settings button is pressed and the user wants to open
     * the game settings screen.
     *
     * @param event The {@link ActionEvent} that occurred.
     */
    @FXML
    protected void openSettings(ActionEvent event) {
        System.out.println("SETTINGS yeey");
    }
}
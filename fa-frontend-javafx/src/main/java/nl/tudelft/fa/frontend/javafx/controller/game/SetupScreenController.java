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

package nl.tudelft.fa.frontend.javafx.controller.game;

import akka.actor.ActorRef;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import nl.tudelft.fa.client.team.Team;
import nl.tudelft.fa.frontend.javafx.Main;
import nl.tudelft.fa.frontend.javafx.controller.AbstractController;
import nl.tudelft.fa.frontend.javafx.controller.StoreController;
import nl.tudelft.fa.frontend.javafx.service.ClientService;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for the setup screen.
 *
 * @author Fabian Mastenbroek
 * @author Christian Slothouber
 * @author Laetitia Molkenboer
 */
public class SetupScreenController extends AbstractController implements Initializable {
    /**
     * The reference to the location of the view of this controller.
     */
    public static final URL VIEW = Main.class.getResource("view/game/setup.fxml");

    /**
     * The injected client service.
     */
    @Inject
    private ClientService service;

    /**
     * The actor of this controller.
     */
    private ActorRef actor;

    /**
     * The controller for the first car configuration.
     */
    @FXML
    private CarConfigurationController firstController;

    /**
     * The controller for the second car configuration.
     */
    @FXML
    private CarConfigurationController secondController;

    @FXML
    protected void store(ActionEvent event) throws Exception {
        show(event, StoreController.VIEW);
    }

    /**
     * Set the team for this controller.
     *
     * @param team The team to set.
     */
    public void setTeam(Team team) {
        firstController.setTeam(team);
        secondController.setTeam(team);
    }

    /**
     * This method is called when the screen is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        actor = service.system().actorOf(SetupActor.props(this)
            .withDispatcher("javafx-dispatcher"));
        service.session().tell(actor, actor);
    }
}

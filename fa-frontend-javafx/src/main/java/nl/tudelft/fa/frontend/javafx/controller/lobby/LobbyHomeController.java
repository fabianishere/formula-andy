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

package nl.tudelft.fa.frontend.javafx.controller.lobby;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import nl.tudelft.fa.client.lobby.Lobby;
import nl.tudelft.fa.client.lobby.LobbyStatus;
import nl.tudelft.fa.client.lobby.message.*;
import nl.tudelft.fa.client.team.Team;
import nl.tudelft.fa.frontend.javafx.Main;
import nl.tudelft.fa.frontend.javafx.controller.AbstractController;
import nl.tudelft.fa.frontend.javafx.service.ClientService;
import nl.tudelft.fa.frontend.javafx.service.TeamService;
import scala.PartialFunction;
import scala.concurrent.duration.FiniteDuration;
import scala.runtime.BoxedUnit;

import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * A controller for the home lobby view.
 *
 * @author Fabian Mastenbroek
 * @author Christian Slothouber
 * @author Laetitia Molkenboer
 */
public class LobbyHomeController extends AbstractController {
    /**
     * The reference to the location of the view of this controller.
     */
    public static final URL VIEW = Main.class.getResource("view/lobby/home.fxml");

    /**
     * The injected client service.
     */
    @Inject
    private ClientService client;

    /**
     * The injected team service.
     */
    @Inject
    private TeamService teamService;

    /**
     * The chat list view.
     */
    @FXML
    private ListView<ChatEvent> chat;

    /**
     * The text field containing the chat message.
     */
    @FXML
    private TextField input;

    /**
     * The label that contains the remaining time.
     */
    @FXML
    private Label remaining;

    /**
     * This method is called when the screen is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        // Create a handler for the server events
        ActorRef ref = client.system().actorOf(Props.create(LobbyHomeActor.class,
            LobbyHomeActor::new).withDispatcher("javafx-dispatcher"));
        // Tell the session about the handler
        client.session().tell(ref, ref);
        // Ask for the current lobby information
        client.session().tell(RequestInformation.INSTANCE, ref);
        // Welcome the user
        sentMessage(ref, "Welcome in this lobby!");

        // Cell factory of the chat list
        chat.setCellFactory(view -> new ListCell<ChatEvent>() {
            @Override
            protected void updateItem(ChatEvent event, boolean empty) {
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(toNode(event));
                }
            }
        });
    }

    /**
     * Handle the given chat event.
     *
     * @param event The chat event to handle.
     */
    @FXML
    private void chat(KeyEvent event) {
        String message = input.getText();
        if (event.getCode().equals(KeyCode.ENTER) && !message.isEmpty()) {
            client.session().tell(new Chat(teamService.teamProperty().get(), message),
                ActorRef.noSender());
            input.clear();
            root.requestFocus();
        }
    }

    /**
     * Convert the given {@link ChatEvent} to a node.
     *
     * @param event The event to convert.
     * @return The node that has been created.
     */
    private Node toNode(ChatEvent event) {
        Text user = new Text();
        user.getStyleClass().add("user");
        user.setText(String.format("%s: ", event.getTeam().getName()));

        Text message = new Text();
        message.getStyleClass().add("message");
        message.setText(event.getMessage());

        TextFlow flow = new TextFlow();
        flow.getChildren().addAll(user, message);
        return flow;
    }

    /**
     * Sent a message from the lobby.
     *
     * @param ref The reference to the actor we sent the messages to.
     * @param message The message to sent.
     */
    private void sentMessage(ActorRef ref, String message) {
        // Sent a message from the lobby
        ref.tell(new ChatEvent(new Team(null, "Lobby", 0, null, null),
            message), ActorRef.noSender());
    }

    /**
     * Set the time remaining of the view.
     *
     * @param status The status of the lobby.
     * @param duration The remaining duration.
     */
    private void setRemaining(LobbyStatus status, Duration duration) {
        String text = duration.toString()
            .substring(2)
            .replaceAll("(\\d[HMS])(?!$)", "$1 ")
            .toLowerCase();

        String message = text;

        switch (status) {
            case INTERMISSION:
                message = String.format("Game preparation in %s", text);
                break;
            case PREPARATION:
                message = String.format("Game starting in %s", text);
                break;
            default:
                break;
        }

        remaining.setText(message.toUpperCase());
    }

    /**
     * The actor that handles the logic for the lobby home screen.
     *
     * @author Fabian Mastenbroek
     */
    public class LobbyHomeActor extends AbstractActor {
        /**
         * The {@link LoggingAdapter} of this class.
         */
        private final LoggingAdapter log = Logging.getLogger(context().system(), this);

        /**
         * The time remaining.
         */
        private Duration remaining;

        /**
         * The status of the lobby.
         */
        private LobbyStatus status = LobbyStatus.INTERMISSION;

        /**
         * This method is invoked on the start of this actor.
         */
        @Override
        public void preStart() {
            // initialize the countdown
            context().system().scheduler().schedule(FiniteDuration.Zero(),
                FiniteDuration.create(1, TimeUnit.SECONDS), self(), "tick",
                context().dispatcher(), self());
        }

        /**
         * This method defines the initial actor behavior, it must return a partial function with
         * the actor logic.
         *
         * @return The initial actor behavior as a partial function.
         */
        @Override
        public PartialFunction<Object, BoxedUnit> receive() {
            return ReceiveBuilder
                .match(ChatEvent.class, msg -> {
                    chat.getItems().add(msg);
                    chat.scrollTo(chat.getItems().size() - 1);
                })
                .match(Lobby.class, msg -> {
                    if (remaining == null) {
                        remaining = msg.getStatus().equals(LobbyStatus.INTERMISSION)
                            ? msg.getConfiguration().getIntermission()
                            : msg.getConfiguration().getPreparation();
                    }
                })
                .match(TimeRemaining.class, msg -> {
                    remaining = msg.getRemaining();

                    if (!remaining.isNegative()) {
                        setRemaining(status, remaining);
                    }
                })
                .matchEquals("tick", msg -> {
                    if (remaining == null) {
                        return;
                    }

                    remaining = remaining.minusSeconds(1);

                    if (!remaining.isNegative()) {
                        setRemaining(status, remaining);
                    }
                })
                .match(LobbyStatusChanged.class, msg -> status = msg.getStatus())
                .match(TeamJoined.class, msg -> sentMessage(self(),
                    String.format("%s has joined the lobby", msg.getTeam().getName())))
                .match(TeamLeft.class, msg -> sentMessage(self(),
                    String.format("%s has left the lobby", msg.getTeam().getName())))
                .build();
        }
    }
}

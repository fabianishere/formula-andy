package nl.tudelft.fa.frontend.javafx.scene;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;


public class SetupScreenScene extends Scene {

    /**Setup for all toggle buttons to act like radio buttons.
     * @param root the parent root
     */
    public SetupScreenScene(Parent root) {
        super(root);

        ToggleButton mechanical11 = (ToggleButton) this.lookup("#mechanical11");
        ToggleButton mechanical12 = (ToggleButton) this.lookup("#mechanical12");
        ToggleButton mechanical13 = (ToggleButton) this.lookup("#mechanical13");
        ToggleButton mechanical21 = (ToggleButton) this.lookup("#mechanical21");
        ToggleButton mechanical22 = (ToggleButton) this.lookup("#mechanical22");
        ToggleButton mechanical23 = (ToggleButton) this.lookup("#mechanical23");
        ToggleButton aerodynamic11 = (ToggleButton) this.lookup("#aerodynamic11");
        ToggleButton aerodynamic12 = (ToggleButton) this.lookup("#aerodynamic12");
        ToggleButton aerodynamic13 = (ToggleButton) this.lookup("#aerodynamic13");
        ToggleButton aerodynamic21 = (ToggleButton) this.lookup("#aerodynamic21");
        ToggleButton aerodynamic22 = (ToggleButton) this.lookup("#aerodynamic22");
        ToggleButton aerodynamic23 = (ToggleButton) this.lookup("#aerodynamic23");
        ToggleButton strategy11 = (ToggleButton) this.lookup("#strategy11");
        ToggleButton strategy12 = (ToggleButton) this.lookup("#strategy12");
        ToggleButton strategy13 = (ToggleButton) this.lookup("#strategy13");
        ToggleButton strategy21 = (ToggleButton) this.lookup("#strategy21");
        ToggleButton strategy22 = (ToggleButton) this.lookup("#strategy22");
        ToggleButton strategy23 = (ToggleButton) this.lookup("#strategy23");

        //Mechanical one toggle group
        mechanical11.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(mechanical11, mechanical12, mechanical13, event);
            }
        });

        mechanical12.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(mechanical12, mechanical13, mechanical11, event);
            }
        });

        mechanical13.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(mechanical13, mechanical11, mechanical12, event);
            }
        });


        //Mechanical two toggle group
        mechanical21.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(mechanical21, mechanical22, mechanical23, event);
            }
        });

        mechanical22.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(mechanical22, mechanical23, mechanical21, event);
            }
        });

        mechanical23.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(mechanical23, mechanical21, mechanical22, event);
            }
        });



        //Aarodynamic one toggle group
        aerodynamic11.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(aerodynamic11, aerodynamic12, aerodynamic13, event);
            }
        });

        aerodynamic12.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(aerodynamic12, aerodynamic13, aerodynamic11, event);
            }
        });

        aerodynamic13.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(aerodynamic13, aerodynamic11, aerodynamic12, event);
            }
        });


        //Aarodynamic two toggle group
        aerodynamic21.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(aerodynamic21, aerodynamic22, aerodynamic23, event);
            }
        });

        aerodynamic22.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(aerodynamic22, aerodynamic23, aerodynamic21, event);
            }
        });

        aerodynamic23.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(aerodynamic23, aerodynamic21, aerodynamic22, event);
            }
        });


        //Strategy one toggle group
        strategy11.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(strategy11, strategy12, strategy13, event);
            }
        });

        strategy12.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(strategy12, strategy13, strategy11, event);
            }
        });

        strategy13.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(strategy13, strategy11, strategy12, event);
            }
        });


        //Strategy two toggle group
        strategy21.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(strategy21, strategy22, strategy23, event);
            }
        });

        strategy22.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(strategy22, strategy23, strategy21, event);
            }
        });

        strategy23.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle(strategy23, strategy21, strategy22, event);
            }
        });
    }

    /**
     * The actual toggle void: toggles the color between the buttons. Selected is red
     * @param t1 the toggle id of the first button
     * @param t2 the toggle id of the second button
     * @param t3 the toggle id of the second button
     * @param event the action event for the toggling
     */
    private void toggle(ToggleButton t1, ToggleButton t2, ToggleButton t3, Event event) {
        if (!t1.isSelected()) {
            t1.setSelected(true);
            t1.setStyle("-fx-background-color: grey;");
            t2.setStyle("-fx-background-color: red;");
            t3.setStyle("-fx-background-color: red;");
        } else {
            t1.setSelected(false);
            t1.setStyle("-fx-background-color: red;");
            t2.setStyle("-fx-background-color: grey;");
            t3.setStyle("-fx-background-color: grey;");
        }
        event.consume();
    }
}




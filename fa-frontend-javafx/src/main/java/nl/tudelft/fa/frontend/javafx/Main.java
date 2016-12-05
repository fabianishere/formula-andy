package nl.tudelft.fa.frontend.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;

/**
 * The main JavaFX {@link Application} class.
 *
 * @author Fabian Mastenbroek
 */
public class Main extends Application {
	/** {@inheritDoc} */
	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("loading-screen.fxml"));
		Scene scene = new Scene(root, 1920, 1080);
		stage.setTitle("Formula Andy!");
		stage.setScene(scene);
		stage.show();
	}
}

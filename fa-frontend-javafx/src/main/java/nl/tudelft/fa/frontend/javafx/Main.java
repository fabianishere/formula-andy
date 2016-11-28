package nl.tudelft.fa.frontend.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main JavaFX {@link Application} class.
 *
 * @author Fabian Mastenbroek
 */
public class Main extends Application {
	/** {@inheritDoc} */
	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

		Scene scene = new Scene(root, 300, 275);

		stage.setTitle("Formula Andy!");
		stage.setScene(scene);
		stage.show();
	}
}

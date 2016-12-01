package nl.tudelft.fa.frontend.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainController {


	@FXML
	protected void NewGame (ActionEvent event) {
		System.out.println("NEW GAME!!");
	}

	@FXML
	protected void LoadGame (ActionEvent event) {
		System.out.println("Loading.....");
	}

	@FXML
	protected void Settings (ActionEvent event) {
		System.out.println("SETTINGS yeey");
	}

}

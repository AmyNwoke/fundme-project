package com.bptn.fundmeproject;

import java.io.IOException;

import com.bptn.fundmeproject_01_modelling.UserManager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class LoginController {
	@FXML
	private TextField userEmail;

	@FXML
	private PasswordField userPassword;

	@FXML
	private void loginButtonOnAction() {
		String email = userEmail.getText().trim();
		String password = userPassword.getText();
		boolean user = UserManager.validateUser(email, password);
		if(!user) {
			showErrorAlert("Username or pasword is incorrect!");
		} else {
			try {
				App.setRoot("dashboard");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@FXML
	private void switchToSignupOnAction() throws IOException {
		App.setRoot("signup");
	}

	private void showErrorAlert(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
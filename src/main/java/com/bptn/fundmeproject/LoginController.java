package com.bptn.fundmeproject;

import java.io.IOException;

import com.bptn.fundmeproject_01_modelling.User;
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
		
		// Fetch the full User object using the email and password
		User user = UserManager.getUserByEmailAndPassword(email, password);

		// If user is null, it means no matching user was found
		if (user == null) {
			showErrorAlert("Username or password is incorrect!");
		} else {
			// Set the logged-in user and switch to dashboard
			try {
				App.loggedInUser = user; // Use the fetched User object
				App.setRoot("dashboard");
			} catch (IOException e) {
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

package com.bptn.fundmeproject.controller;

import java.io.IOException;
import com.bptn.fundmeproject.App;

import com.bptn.fundmeproject.model.User;
import com.bptn.fundmeproject.manager.UserManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignupController {
	@FXML
	private TextField fullName;

	@FXML
	private TextField userEmail;

	@FXML
	private PasswordField userPassword;

	@FXML
	private PasswordField userPasswordConfirm;

	@FXML
	private Label errorLabel;

	@FXML
	void switchToLoginOnAction(ActionEvent event) throws IOException {
		App.setRoot("login");
	}

	// set fields and validate user input
	@FXML
	void signUpButtonOnAction(ActionEvent event) {
		String name = fullName.getText().trim();
		String email = userEmail.getText().trim();
		String password = userPassword.getText();
		String password2 = userPasswordConfirm.getText();
		if (!User.isValidEmail(email)) {
			showErrorAlert("Enter a valid email.");
		} else if (UserManager.emailExists(email)) {
			showErrorAlert("User already exists, proceed to login.");
		} else if (!User.confirmPassword(password, password2)) {
			showErrorAlert("Your password fields must match!");
		} else if (!User.passwordValid(password)) {
			showErrorAlert("Your password should have more than 7 char.");
		} else if (name.isEmpty()) {
			showErrorAlert("Please fill all the fields!");
		} else {
			User user = new User(name, email, password);
			UserManager.saveUserToFile(user);
			try {
				App.setRoot("login");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void showErrorAlert(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}

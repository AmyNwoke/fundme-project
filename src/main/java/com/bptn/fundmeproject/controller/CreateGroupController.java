package com.bptn.fundmeproject.controller;

import java.io.IOException;
import com.bptn.fundmeproject.App;
import java.time.LocalDate;

import com.bptn.fundmeproject.model.Group;
import com.bptn.fundmeproject.manager.GroupManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;

public class CreateGroupController {

	@FXML
	private TextField groupNameField;

	@FXML
	private TextField memberCountField;

	@FXML
	private TextField savingsTargetField;

	@FXML
	private TextField savingsPeriodField;

	@FXML
	private DatePicker startDateField;

	@FXML
	private TextField purposeOfSavingField;

	// Group Manager to manage the group
	private GroupManager groupManager = GroupManager.getInstance();

	@FXML
	void createGroupButtonOnAction(ActionEvent event) {
		// Check if loggedInUser is available in App class
		if (App.loggedInUser == null) {
			showErrorAlert("User not logged in!");
			return;
		}

		// Retrieve the form inputs
		String groupName = groupNameField.getText().trim();
		String memberCountStr = memberCountField.getText().trim();
		String savingsTargetStr = savingsTargetField.getText().trim();
		String savingsPeriodStr = savingsPeriodField.getText().trim();
		LocalDate startDate = startDateField.getValue(); // Get date from DatePicker
		String purpose = purposeOfSavingField.getText().trim();

		String savingsFrequency = "Monthly"; // Fixed value as mentioned

		// Input validation
		if (groupName.isEmpty() || memberCountStr.isEmpty() || savingsTargetStr.isEmpty() || savingsPeriodStr.isEmpty()
				|| startDate == null || purpose.isEmpty()) {
			showErrorAlert("Please fill out all the fields.");
			return;
		}

		// Parse numbers from the fields & ensure the required input is received from
		// user
		int memberCount;
		double savingsTarget;
		int savingsPeriod;
		try {
			memberCount = Integer.parseInt(memberCountStr);
			savingsTarget = Double.parseDouble(savingsTargetStr);
			savingsPeriod = Integer.parseInt(savingsPeriodStr);
		} catch (NumberFormatException e) {
			showErrorAlert("Please ensure Member Count, Savings Target, and Savings Period are valid numbers.");
			return;
		}

		// check if the number is negative

		if (isNumberNegative(memberCount, savingsTarget, savingsPeriod)) {
			showErrorAlert("Please ensure Member Count, Savings Target, and Savings Period are all positive numbers.");
			return;
		}

		// Create new group and call addGroup method in group manager
		String groupCode = generateGroupCode(); // Call method to generate group code
		Group newGroup = new Group(groupName, memberCount, savingsTarget, savingsPeriod, savingsFrequency,
				startDate.toString(), purpose, groupCode, (savingsTarget / savingsPeriod / memberCount));

		// call addGroup method in group manager and include the creator as the first
		// member
		try {
			groupManager.addGroup(newGroup, App.loggedInUser.getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			showErrorAlert(e.getMessage());
			return;
		}

		// Show success message without the fund button
		showSuccessAlert(groupCode);

		// Clear the form after creation
		clearForm();
	}

	// Method to show a simple success alert after group creation
	private void showSuccessAlert(String groupCode) {
		Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
		confirmationAlert.setTitle("Group Created Successfully");
		confirmationAlert.setHeaderText("Group Code: " + groupCode);
		confirmationAlert.setContentText(
				"Your group has been created. Please share the group code with others so they can join.");
		confirmationAlert.showAndWait();
	}

	private void showErrorAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void clearForm() {
		groupNameField.clear();
		memberCountField.clear();
		savingsTargetField.clear();
		savingsPeriodField.clear();
		startDateField.setValue(null);
		purposeOfSavingField.clear();
	}

	// Method to generate a random group code
	private String generateGroupCode() {
		return String.valueOf((int) (Math.random() * 900000) + 100000); // 6-digit random number
	}

	// Method to validate only positive number entries
	private boolean isNumberNegative(int member, double target, int period) {
		if (member <= 0 || target <= 0 || period <= 0) {
			return true;
		} // 6-digit random number
		return false;
	}

	@FXML
	void switchTojoinGroupOnAction(ActionEvent event) throws IOException {
		App.setRoot("joingroup");
	}

	@FXML
	void switchToFundSavingsOnAction(ActionEvent event) throws IOException {
		App.setRoot("fundsavings");
	}

	@FXML
	void switchToSignupOnAction(ActionEvent event) throws IOException {
		App.setRoot("signup");
	}
}

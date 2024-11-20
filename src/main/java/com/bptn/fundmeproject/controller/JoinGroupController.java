/*
 * package com.bptn.fundmeproject.controller;
 * 
 * import java.io.IOException; import com.bptn.fundmeproject.App;
 * 
 * import com.bptn.fundmeproject.model.Group; import
 * com.bptn.fundmeproject.manager.EmailManager; import
 * com.bptn.fundmeproject.manager.GroupManager; import javafx.fxml.FXML; import
 * javafx.scene.control.Alert; import javafx.scene.control.Label; import
 * javafx.scene.control.TextField; import javafx.event.ActionEvent;
 * 
 * public class JoinGroupController {
 * 
 * @FXML private TextField groupCodeField; // Text field to enter group code
 * 
 * @FXML private Label groupNameLabel;
 * 
 * @FXML private Label memberCountLabel;
 * 
 * @FXML private Label savingsTargetLabel;
 * 
 * @FXML private Label savingsPeriodLabel;
 * 
 * @FXML private Label startDateLabel;
 * 
 * @FXML private Label purposeOfSavingLabel;
 * 
 * @FXML private Label individualContributionLabel;
 * 
 * // Group manager instance to fetch the groups private GroupManager
 * groupManager = GroupManager.getInstance(); // Create an object of
 * EmailManager to send email private EmailManager emailManager = new
 * EmailManager();
 * 
 * // Method to handle Join Group button click
 * 
 * @FXML void joinGroupButtonOnAction(ActionEvent event) { String groupCode =
 * groupCodeField.getText().trim(); // Get group code input if
 * (groupCode.isEmpty()) { showErrorAlert("Please enter a valid group code.");
 * return; }
 * 
 * // Find the group by group code using GroupManager Group group =
 * groupManager.findGroupByCode(groupCode); if (group == null) {
 * showErrorAlert("Group not found. Please check the code and try again.");
 * return; }
 * 
 * // Check if loggedInUser is available in App class if (App.loggedInUser ==
 * null) { showErrorAlert("User not logged in!"); return; }
 * 
 * // Add the logged-in user to the group try {
 * groupManager.addMemberToGroup(group.getGroupCode(),
 * App.loggedInUser.getName());
 * 
 * // Send confirmation email String userName = App.loggedInUser.getName();
 * String userEmail = App.loggedInUser.getEmail();
 * emailManager.sendJoinGroupEmail(userName, userEmail, group.getGroupCode(),
 * group.getMonthlySavingsPerMember());
 * 
 * } catch (Exception ex) { showErrorAlert(ex.getMessage()); return; }
 * 
 * // Update the labels with group details
 * groupNameLabel.setText(group.getGroupName());
 * memberCountLabel.setText(String.valueOf(group.getMembers().size()));
 * savingsTargetLabel.setText(String.format("$%.2f", group.getSavingsTarget()));
 * savingsPeriodLabel.setText(String.valueOf(group.getSavingsPeriod()) +
 * " months"); startDateLabel.setText(group.getStartDate());
 * purposeOfSavingLabel.setText(group.getSavingFor());
 * individualContributionLabel.setText(String.format("$%.2f",
 * group.getMonthlySavingsPerMember()));
 * 
 * // Update the labels with group details, including custom prefixes
 * groupNameLabel.setText("Group Name: " + group.getGroupName());
 * memberCountLabel.setText("Member Count: " + group.getMembers().size());
 * savingsTargetLabel.setText("Savings Target: " + String.format("$%.2f",
 * group.getSavingsTarget())); savingsPeriodLabel.setText("Savings Period: " +
 * group.getSavingsPeriod() + " months"); startDateLabel.setText("Start Date: "
 * + group.getStartDate()); purposeOfSavingLabel.setText("Purpose of Saving: " +
 * group.getSavingFor());
 * individualContributionLabel.setText("Individual Contribution: " +
 * String.format("$%.2f", group.getMonthlySavingsPerMember()));
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * // Show success message
 * showSuccessAlert("You have successfully joined the group!"); }
 * 
 * // Show success alert private void showSuccessAlert(String message) { Alert
 * alert = new Alert(Alert.AlertType.INFORMATION); alert.setTitle("Success");
 * alert.setHeaderText(null); alert.setContentText(message);
 * alert.showAndWait(); }
 * 
 * // showing an error alert private void showErrorAlert(String message) { Alert
 * alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
 * alert.setHeaderText(null); alert.setContentText(message);
 * alert.showAndWait(); }
 * 
 * @FXML void switchToCreateGroupOnAction(ActionEvent event) throws IOException
 * { App.setRoot("creategroup"); }
 * 
 * @FXML void switchToFundSavingsOnAction(ActionEvent event) throws IOException
 * { App.setRoot("fundsavings"); }
 * 
 * @FXML void switchToSignupOnAction(ActionEvent event) throws IOException {
 * App.setRoot("signup"); } }
 * 
 * 
 */

package com.bptn.fundmeproject.controller;

import java.io.IOException;
import com.bptn.fundmeproject.App;
import com.bptn.fundmeproject.model.Group;
import com.bptn.fundmeproject.manager.EmailManager;
import com.bptn.fundmeproject.manager.GroupManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class JoinGroupController {

	@FXML
	private TextField groupCodeField; // Text field to enter group code
	@FXML
	private Label groupNameLabel;
	@FXML
	private Label memberCountLabel;
	@FXML
	private Label savingsTargetLabel;
	@FXML
	private Label savingsPeriodLabel;
	@FXML
	private Label startDateLabel;
	@FXML
	private Label purposeOfSavingLabel;
	@FXML
	private Label individualContributionLabel;

	// Group manager instance to fetch the groups
	private GroupManager groupManager = GroupManager.getInstance();
	// Create an object of EmailManager to send email
	private EmailManager emailManager = new EmailManager();

	private Group group; // Store the group details after checking

	// Method to handle Check button click
	@FXML
	void checkGroupButtonOnAction(ActionEvent event) {
		String groupCode = groupCodeField.getText().trim(); // Get group code input
		if (groupCode.isEmpty()) {
			showErrorAlert("Please enter a valid group code.");
			return;
		}

		// Find the group by group code using GroupManager
		group = groupManager.findGroupByCode(groupCode);
		if (group == null) {
			showErrorAlert("Group not found. Please check the code and try again.");
			return;
		}

		// Update the labels with group details
		groupNameLabel.setText("Group Name: " + group.getGroupName());
		memberCountLabel.setText("Member Count: " + group.getMembers().size());
		savingsTargetLabel.setText("Savings Target: " + String.format("$%.2f", group.getSavingsTarget()));
		savingsPeriodLabel.setText("Savings Period: " + group.getSavingsPeriod() + " months");
		startDateLabel.setText("Start Date: " + group.getStartDate());
		purposeOfSavingLabel.setText("Purpose of Saving: " + group.getSavingFor());
		individualContributionLabel
				.setText("Individual Contribution: " + String.format("$%.2f", group.getMonthlySavingsPerMember()));
	}

	// Method to handle Join Group button click
	@FXML
	void joinGroupButtonOnAction(ActionEvent event) {
		if (group == null) {
			showErrorAlert("Please check the group code first.");
			return;
		}

		// Check if loggedInUser is available in App class
		if (App.loggedInUser == null) {
			showErrorAlert("User not logged in!");
			return;
		}

		// Add the logged-in user to the group
		try {
			groupManager.addMemberToGroup(group.getGroupCode(), App.loggedInUser.getName());

			// Send confirmation email
			String userName = App.loggedInUser.getName();
			String userEmail = App.loggedInUser.getEmail();
			emailManager.sendJoinGroupEmail(userName, userEmail, group.getGroupCode(),
					group.getMonthlySavingsPerMember());

			// Show success message
			showSuccessAlert("You have successfully joined the group!");

		} catch (Exception ex) {
			showErrorAlert(ex.getMessage());
		}
	}

	// Show success alert
	private void showSuccessAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	// showing an error alert
	private void showErrorAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	@FXML
	void switchToCreateGroupOnAction(ActionEvent event) throws IOException {
		App.setRoot("creategroup");
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

package com.bptn.fundmeproject;

import java.io.IOException;

import com.bptn.fundmeproject_01_modelling.Group;
import com.bptn.fundmeproject_01_modelling.GroupManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class JoinGroupController {
    @FXML
    private TextField groupCodeField;  // Text field to enter group code
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

    // Method to handle Join Group button click
    @FXML
    void joinGroupButtonOnAction(ActionEvent event) {
        String groupCode = groupCodeField.getText().trim();  // Get group code input
        if (groupCode.isEmpty()) {
            showErrorAlert("Please enter a valid group code.");
            return;
        }

        // Find the group by group code using GroupManager
        Group group = groupManager.findGroupByCode(groupCode);
        if (group == null) {
            showErrorAlert("Group not found. Please check the code and try again.");
            return;
        }

        // Check if loggedInUser is available in App class
        if (App.loggedInUser == null) {
            showErrorAlert("User not logged in!");
            return;
        }

        // Add the logged-in user to the group
        String message = groupManager.addMemberToGroup(group.getGroupCode(), App.loggedInUser.getName());
        switch (message) {
	        case "err1": showErrorAlert(group.getGroupName() + " Group is alreay full, join another group.");
	        return;
	        case "err2": showErrorAlert(App.loggedInUser.getName() + " is already a member of this group.");
	        return;
	        case "err3": showErrorAlert("Group with code " + groupCode + " not found.");
	        return;
        }

        // Update the labels with group details
        groupNameLabel.setText(group.getGroupName());
        memberCountLabel.setText(String.valueOf(group.getMembers().size()));
        savingsTargetLabel.setText(String.format("$%.2f", group.getSavingsTarget()));
        savingsPeriodLabel.setText(String.valueOf(group.getSavingsPeriod()) + " months");
        startDateLabel.setText(group.getStartDate());
        purposeOfSavingLabel.setText(group.getSavingFor());
        individualContributionLabel.setText(String.format("$%.2f", group.getMonthlySavingsPerMember()));

        // Show success message
        showSuccessAlert("You have successfully joined the group!");
    }

    // Show success alert
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper method to show an error alert
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

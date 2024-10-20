package com.bptn.fundmeproject;

import java.io.IOException;

import com.bptn.fundmeproject_01_modelling.Group;
import com.bptn.fundmeproject_01_modelling.GroupManager;
import com.bptn.fundmeproject_01_modelling.User;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
    private GroupManager groupManager = new GroupManager(); 

    // User instance to represent the currently logged-in user (you will need to pass this)
    private User loggedInUser;

    // Constructor to pass the logged-in user (modify this based on how you're handling logged-in users)
    public JoinGroupController(User loggedInUser) {
       this.loggedInUser = loggedInUser;
    }

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

        // Update the labels with group details (but don't add the user yet)
        groupNameLabel.setText(group.getGroupName());
        memberCountLabel.setText(String.valueOf(group.getMembers()));
        savingsTargetLabel.setText(String.format("$%.2f", group.getSavingsTarget()));
        savingsPeriodLabel.setText(String.valueOf(group.getSavingsPeriod()) + " months");
        startDateLabel.setText(group.getStartDate().toString());
        purposeOfSavingLabel.setText(group.getSavingFor());
        individualContributionLabel.setText(String.format("$%.2f", group.getMonthlySavingsPerMember()));

        // Show the updated success alert with "Fund Savings" option
        showJoinGroupConfirmation(group);
    }

    // Show confirmation alert with "Fund Savings" and "Cancel" buttons
    private void showJoinGroupConfirmation(Group group) {
        Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
        confirmationAlert.setTitle("One Task Left");
        confirmationAlert.setHeaderText("Fund savings to successfully join the group: " + group.getGroupName());
        confirmationAlert.setContentText("Would you like to fund your savings now?");

        // Add custom buttons: "Fund Savings" and "Cancel"
        ButtonType fundButton = new ButtonType("Fund Savings");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());

        confirmationAlert.getButtonTypes().setAll(fundButton, cancelButton);

        // Handle the user's decision
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == fundButton) {
                // Only add the user to the group if they choose to fund the savings
                groupManager.addMemberToGroup(group.getGroupCode(), loggedInUser.getName());

                // After the user is added to the group, navigate to the Fund Savings screen
                try {
                    App.setRoot("fundsavings");  // Switch to fund savings scene
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (response == cancelButton) {
                // User cancelled the action, show a "Join Group Unsuccessful" message
                showErrorAlert("Join Group Unsuccessful. You must fund savings to complete the process.");
            }
        });
    }

    // Helper method to show an error alert
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper method to show success alert (if you need this separately)
    /*private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }*/
}

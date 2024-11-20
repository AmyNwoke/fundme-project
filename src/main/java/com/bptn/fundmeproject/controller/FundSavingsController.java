/*
 * package com.bptn.fundmeproject.controller;
 * 
 * import java.io.IOException; import com.bptn.fundmeproject.App; import
 * java.util.List;
 * 
 * import com.bptn.fundmeproject.manager.EmailManager; import
 * com.bptn.fundmeproject.model.Group; import
 * com.bptn.fundmeproject.manager.GroupManager; import javafx.fxml.FXML; import
 * javafx.scene.control.Alert; import javafx.scene.control.Button; import
 * javafx.scene.control.Label; import javafx.scene.control.TextField; import
 * javafx.event.ActionEvent;
 * 
 * public class FundSavingsController {
 * 
 * @FXML private TextField groupCodeField; // Group code input from user
 * 
 * @FXML private Label groupNameLabel; // Group name to display
 * 
 * @FXML private Label individualContributionLabel; // Contribution to display
 * 
 * @FXML private Button doneButton; // Done button
 * 
 * // making reference to GroupManager instance private GroupManager
 * groupManager = GroupManager.getInstance();
 * 
 * // Create an object of EmailManager to send email private EmailManager
 * emailManager = new EmailManager();
 * 
 * // Method to handle when user clicks "Done"
 * 
 * @FXML void doneButtonOnAction(ActionEvent event) { String groupCode =
 * groupCodeField.getText().trim();
 * 
 * // Find the group by code from the GroupManager Group group =
 * groupManager.findGroupByCode(groupCode);
 * 
 * if (group != null) { List<String> members = group.getMembers(); if
 * (!members.contains(App.loggedInUser.getName())) {
 * showErrorAlert("You are not a member of this group, try another group or create a new group"
 * ); return; } // Group found, display group name and individual contribution
 * groupNameLabel.setText(group.getGroupName()); double individualContribution =
 * group.getMonthlySavingsPerMember();
 * individualContributionLabel.setText(String.format("$%.2f",
 * individualContribution));
 * 
 * // Record user contribution using App.loggedInUser, assuming the user is
 * already // logged in try {
 * 
 * groupManager.recordContribution(groupCode, App.loggedInUser.getName(),
 * individualContribution); showConfirmationAlert(); // Send confirmation email
 * String userName = App.loggedInUser.getName(); String userEmail =
 * App.loggedInUser.getEmail(); emailManager.sendFundSavingsEmail(userName,
 * userEmail, individualContribution);
 * 
 * } catch (Exception ex) { showErrorAlert(ex.getMessage()); return; }
 * 
 * } else { // If group is not found, show error alert
 * showErrorAlert("Group not found! Please check the group code."); return; } }
 * 
 * // Show a confirmation alert when "Done" is clicked private void
 * showConfirmationAlert() { Alert alert = new
 * Alert(Alert.AlertType.INFORMATION); alert.setTitle("Success");
 * alert.setHeaderText("Deposit Successful"); alert.
 * setContentText("A confirmation email will be sent to you once the payment is received."
 * ); alert.showAndWait(); }
 * 
 * // Show an error alert for invalid group code private void
 * showErrorAlert(String message) { Alert alert = new
 * Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
 * alert.setHeaderText(null); alert.setContentText(message);
 * alert.showAndWait(); }
 * 
 * @FXML void switchToSignupButtonOnAction(ActionEvent event) throws IOException
 * { App.setRoot("signup"); }
 * 
 * @FXML void switchTojoinGroupOnAction(ActionEvent event) throws IOException {
 * App.setRoot("joingroup"); }
 * 
 * @FXML void switchToCreateGroupOnAction(ActionEvent event) throws IOException
 * { App.setRoot("creategroup"); } }
 */


package com.bptn.fundmeproject.controller;

import java.io.IOException;
import java.util.List;
import com.bptn.fundmeproject.App;
import com.bptn.fundmeproject.manager.EmailManager;
import com.bptn.fundmeproject.manager.GroupManager;
import com.bptn.fundmeproject.model.Group;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class FundSavingsController {

    @FXML
    private TextField groupCodeField; // Group code input from user

    @FXML
    private Label groupNameLabel; // Group name to display

    @FXML
    private Label individualContributionLabel; // Contribution to display

    @FXML
    private Button doneButton; // Done button

    // Group manager instance to fetch the groups
    private GroupManager groupManager = GroupManager.getInstance();

    // Email manager instance to send emails
    private EmailManager emailManager = new EmailManager();

    // Method to handle the Check button click
    @FXML
    void checkGroupButtonOnAction(ActionEvent event) {
        String groupCode = groupCodeField.getText().trim();
        if (groupCode.isEmpty()) {
            showErrorAlert("Please enter a valid group code.");
            return;
        }

        // Find the group by code
        Group group = groupManager.findGroupByCode(groupCode);
        if (group == null) {
            showErrorAlert("Group not found! Please check the group code.");
            return;
        }

        // Check if the logged-in user is a member of the group
        List<String> members = group.getMembers();
        if (!members.contains(App.loggedInUser.getName())) {
            showErrorAlert("You are not a member of this group, try another group or create a new group.");
            return;
        }

        // Populate the group details
        groupNameLabel.setText("Group Name: " + group.getGroupName());
        double individualContribution = group.getMonthlySavingsPerMember();
        individualContributionLabel.setText(String.format("Individual Contribution: $%.2f", individualContribution));

       // // Enable the Done button after successful check
        //doneButton.setDisable(false);
    }

    // Method to handle the Done button click
    @FXML
    void doneButtonOnAction(ActionEvent event) {
        String groupCode = groupCodeField.getText().trim();

        // Find the group by code
        Group group = groupManager.findGroupByCode(groupCode);
        if (group == null) {
            showErrorAlert("Please check the group code first.");
            return;
        }

        // Record user contribution
        double individualContribution = group.getMonthlySavingsPerMember();
        try {
            groupManager.recordContribution(group.getGroupCode(), App.loggedInUser.getName(), individualContribution);

            // Send confirmation email
            String userName = App.loggedInUser.getName();
            String userEmail = App.loggedInUser.getEmail();
            emailManager.sendFundSavingsEmail(userName, userEmail, individualContribution);

            // Show success message
            showConfirmationAlert();

        } catch (Exception ex) {
            showErrorAlert(ex.getMessage());
        }
    }

    // Show a confirmation alert when "Done" is clicked
    private void showConfirmationAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Deposit Successful");
        alert.setContentText("A confirmation email will be sent to you once the payment is received.");
        alert.showAndWait();
    }

    // Show an error alert
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void switchToSignupButtonOnAction(ActionEvent event) throws IOException {
        App.setRoot("signup");
    }

    @FXML
    void switchTojoinGroupOnAction(ActionEvent event) throws IOException {
        App.setRoot("joingroup");
    }

    @FXML
    void switchToCreateGroupOnAction(ActionEvent event) throws IOException {
        App.setRoot("creategroup");
    }
    
    @FXML
	void switchToWithdrawalOnAction(ActionEvent event) throws IOException {
		App.setRoot("withdrawal");
	}
    @FXML
	void switchToDashboardOnAction(ActionEvent event) throws IOException {
		App.setRoot("dashboard");
	}

}

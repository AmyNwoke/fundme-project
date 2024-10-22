package com.bptn.fundmeproject;

import java.io.IOException;

import com.bptn.fundmeproject_01_modelling.Group;
import com.bptn.fundmeproject_01_modelling.GroupManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class FundSavingsController {

    @FXML
    private TextField groupCodeField;  // Group code input from user

    @FXML
    private Label groupNameLabel;  // Group name to display

    @FXML
    private Label individualContributionLabel;  // Contribution to display

    @FXML
    private Button doneButton;  // Done button

    // Reference to GroupManager singleton (should be initialized with all groups)
    private GroupManager groupManager = GroupManager.getInstance();


    @FXML
    void initialize() {
        // Initialization code, if needed.
    }

    // Method to handle when user clicks "Done"
    @FXML
    void doneButtonOnAction(ActionEvent event) {
        String groupCode = groupCodeField.getText().trim();

        // Find the group by code from the GroupManager
        Group group = groupManager.findGroupByCode(groupCode);

        if (group != null) {
            // Group found, display group name and individual contribution
            groupNameLabel.setText(group.getGroupName());
            double individualContribution = group.getMonthlySavingsPerMember();
            individualContributionLabel.setText(String.format("$%.2f", individualContribution));

           
            
            // Record user contribution using App.loggedInUser, assuming the user is already logged in
            groupManager.recordContribution(groupCode, App.loggedInUser.getName(), individualContribution);

            // Simulate the fund process and show confirmation alert
            showConfirmationAlert();
        } else {
            // If group is not found, show error alert
            showErrorAlert("Group not found! Please check the group code.");
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

    // Show an error alert for invalid group code
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
}

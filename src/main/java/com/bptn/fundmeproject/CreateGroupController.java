package com.bptn.fundmeproject;

import java.io.IOException;
import java.time.LocalDate;

import com.bptn.fundmeproject_01_modelling.Group;
import com.bptn.fundmeproject_01_modelling.GroupManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
//import javafx.scene.control.DialogPane;


public class CreateGroupController {
	
	@FXML
    private TextField groupNameField;  // Match this with your SceneBuilder fx:id

    @FXML
    private TextField memberCountField;  // Match this with your SceneBuilder fx:id
    
    @FXML
    private TextField savingsTargetField;

    @FXML
    private TextField savingsPeriodField;

    @FXML
    private DatePicker startDateField;

      
    @FXML
    private TextField purposeOfSavingField;
    
   // @FXML
   // void createGroupButtonOnAction(ActionEvent event) {

 //   }

 // Group Manager to manage the group
    private GroupManager groupManager = new GroupManager(); // This will hold groups in memory

    @FXML
    void createGroupButtonOnAction(ActionEvent event) {
        // Retrieve the form inputs
        String groupName = groupNameField.getText().trim();
        String memberCountStr = memberCountField.getText().trim();
        String savingsTargetStr = savingsTargetField.getText().trim();
        String savingsPeriodStr = savingsPeriodField.getText().trim();
        LocalDate startDate = startDateField.getValue();  // Get date from DatePicker
        String purpose = purposeOfSavingField.getText().trim();


        String savingsFrequency = "Monthly"; // Fixed value as mentioned

        // Input validation
        if (groupName.isEmpty() || memberCountStr.isEmpty() || savingsTargetStr.isEmpty() ||
            savingsPeriodStr.isEmpty() || startDate == null || purpose.isEmpty()) {
            showErrorAlert("Please fill out all the fields.");
            return;
        }

        // Parse numbers from the fields
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

        // Create new group and add it to the group manager
        String groupCode = generateGroupCode();  // Call method to generate group code
        Group newGroup = new Group(groupName, memberCount, savingsTarget, savingsPeriod,
                savingsFrequency, startDate.toString(), purpose, groupCode,
                savingsTarget / savingsPeriod / memberCount);

        // Add group to GroupManager
        groupManager.addGroup(newGroup);

        // Show the custom confirmation with "Fund Savings" button
        showConfirmationWithFundOption(groupCode);

        // Clear the form after creation
        clearForm();
    }

    // This method shows the custom confirmation with the option to "Fund Savings"
    private void showConfirmationWithFundOption(String groupCode) {
        Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
        confirmationAlert.setTitle("Group Created Successfully");
        confirmationAlert.setHeaderText("Group Code: " + groupCode);
        confirmationAlert.setContentText("Your group has been created. Please share the group code with others so they can join. Would you like to fund the group's savings now?");

        // Add a "Fund Savings" button to the alert
        ButtonType fundButton = new ButtonType("Fund Savings");
        confirmationAlert.getButtonTypes().add(fundButton);

        // Get the dialog pane and customize it
       // DialogPane dialogPane = confirmationAlert.getDialogPane();
        
        // Add an event handler to the "Fund Savings" button
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == fundButton) {
                // If "Fund Savings" button is pressed, switch to the Fund Savings screen
                try {
                    App.setRoot("fundSavings");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
        return String.valueOf((int) (Math.random() * 900000) + 100000);  // 6-digit random number
    }
}




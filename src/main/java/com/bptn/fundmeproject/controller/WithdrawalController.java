
package com.bptn.fundmeproject.controller;

import java.io.IOException;

import java.util.List;
import com.bptn.fundmeproject.App;
import com.bptn.fundmeproject.manager.EmailManager;
import com.bptn.fundmeproject.manager.GroupManager;
import com.bptn.fundmeproject.model.Group;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class WithdrawalController {

    
    
    @FXML
    private TextField groupCodeField;

    @FXML
    private TextField interacEmailField;

   // @FXML
   // private Button checkButton, doneButton;

    private GroupManager groupManager = GroupManager.getInstance();
    private EmailManager emailManager = new EmailManager();

    @FXML
    void checkButtonOnAction(ActionEvent event) {
        String groupCode = groupCodeField.getText().trim();
        Group group = groupManager.findGroupByCode(groupCode);

        if (group == null) {
            showErrorAlert("Invalid code.");
            return;
        }

        if (!group.getMembers().contains(App.loggedInUser.getName())) {
            showErrorAlert("You are not a member of this group.");
            return;
        }

     // Check if the logged-in user is the group creator
        if (!groupManager.isGroupCreator(groupCode, App.loggedInUser.getName())) {
            showErrorAlert("Only the group creator can withdraw.");
            return;
        }

        if (!group.isWithdrawn() && groupManager.getTotalSavingsForGroup(groupCode) < group.getSavingsTarget()) {
            showErrorAlert("The savings target has not been met.");
            return;
        }

        //doneButton.setDisable(false);
    }

    @FXML
    void doneButtonOnAction(ActionEvent event) {
        String groupCode = groupCodeField.getText().trim();
        String interacEmail = interacEmailField.getText().trim();
      //  Group group = groupManager.findGroupByCode(groupCode);

        // Record withdrawal
        groupManager.recordWithdrawal(groupCode, interacEmail);

        // Update group status to withdrawn
        groupManager.updateWithdrawalStatus(groupCode, true);

        // Notify group members
        List<String> emails = groupManager.getGroupEmailsByCode(groupCode);
        emails.forEach(email -> emailManager.sendWithdrawalNotification(email));

        showConfirmationAlert("Withdrawal successful. Members notified.");
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showConfirmationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
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
	void switchToDashboardOnAction(ActionEvent event) throws IOException {
		App.setRoot("dashboard");
	}
	
	@FXML
	void switchToCreateGroupOnAction(ActionEvent event) throws IOException {
		App.setRoot("creategroup");
	}
	

	//@FXML
	//void switchToSignupOnAction(ActionEvent event) throws IOException {
		//App.setRoot("signup");
	//}


	@FXML
	void switchToWithdrawalOnAction(ActionEvent event) throws IOException {
		App.setRoot("withdrawal");
	}
	

}


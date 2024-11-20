package com.bptn.fundmeproject.controller;

import java.io.IOException;
import com.bptn.fundmeproject.App;
import java.util.List;

import com.bptn.fundmeproject.model.*;
import com.bptn.fundmeproject.manager.GroupManager;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DashboardController {

	// Labels to display group information
	@FXML
	private Label groupNameLabel;
	@FXML
	private Label groupCodeLabel;
	@FXML
	private Label targetSavingsLabel;
	@FXML
	private Label startDateLabel;
	@FXML
	private Label totalSavingsLabel;
	@FXML
	private Label fundsWithdrawnLabel;

	// TableView and its columns for member contributions
	@FXML
	private TableView<Contribution> contributionTable;
	@FXML
	private TableColumn<Contribution, String> nameColumnLabel;
	@FXML
	private TableColumn<Contribution, Double> amountColumnLabel;
	@FXML
	private TableColumn<Contribution, String> dateColumnLabel;

	private GroupManager groupManager;

	@FXML
	public void initialize() {
		groupManager = GroupManager.getInstance();

		// Setting up the table columns to match the Contribution class fields
		nameColumnLabel.setCellValueFactory(new PropertyValueFactory<>("member"));
		amountColumnLabel.setCellValueFactory(new PropertyValueFactory<>("amountContributed"));
		dateColumnLabel.setCellValueFactory(new PropertyValueFactory<>("currentDate"));

		// load group details of currently loggedin user if user belong to a group
		Group group = groupManager.findGroupForMember(App.loggedInUser.getName());
		if (group != null) {
			String groupCode = group.getGroupCode();
			loadGroupDetails(group);
			loadContributions(groupCode);
			loadGroupSavingsProgress(groupCode);
			checkWithdrawalStatus(group);
		}

	}

	// Method to load and display the group details
	// setting the labels to display the actual details
	private void loadGroupDetails(Group group) {
		System.out.println("group: " + group.getGroupCode());

		groupNameLabel.setText(group.getGroupName());
		groupCodeLabel.setText(group.getGroupCode());
		targetSavingsLabel.setText(String.format("%.2f", group.getSavingsTarget())); // Target Savings
		startDateLabel.setText(group.getStartDate());

	}

	// Method to load contributions for a group
	private void loadContributions(String groupCode) {
		List<Contribution> contributions = groupManager.getContributionList(groupCode);
		System.out.println("contribution list: " + contributions);
		if (contributions != null) {
			ObservableList<Contribution> contributionList = FXCollections.observableArrayList(contributions);
			contributionTable.setItems(contributionList);
		}
	}

	// Method to load and display total savings for a group
	private void loadGroupSavingsProgress(String groupCode) {
		double totalSavings = groupManager.getTotalSavingsForGroup(groupCode);

		System.out.println("savings progress: " + totalSavings);

		totalSavingsLabel.setText(String.format("%.2f", totalSavings));

	}

	// Method to check and display withdrawal status
	private void checkWithdrawalStatus(Group group) {
		if (group.isWithdrawn()) {
			fundsWithdrawnLabel.setText("Funds Withdrawn"); // Display if funds withdrawn
			fundsWithdrawnLabel.setStyle("-fx-text-fill: green;"); // Optional: Set color
		} else {
			fundsWithdrawnLabel.setText(""); // Clear label if not withdrawn
		}
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
	void switchToFundSavingsOnAction(ActionEvent event) throws IOException {
		App.setRoot("fundsavings");
	}

	@FXML
	void switchToWithdrawalOnAction(ActionEvent event) throws IOException {
		App.setRoot("withdrawal");
	}

}

package com.bptn.fundmeproject;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class DashboardController {
	
	
	
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
	
	

}


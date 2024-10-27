module com.bptn.demofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
	requires javafx.base;
	requires jakarta.mail;
	exports com.bptn.fundmeproject.model to javafx.fxml; 
	exports com.bptn.fundmeproject.manager to javafx.fxml;
	exports com.bptn.fundmeproject.controller to javafx.fxml;
	opens com.bptn.fundmeproject.controller to javafx.fxml;

	
	opens com.bptn.fundmeproject.model to javafx.base;
	opens com.bptn.fundmeproject.manager to javafx.base;


    opens com.bptn.fundmeproject to javafx.fxml;
    exports com.bptn.fundmeproject;
    
}

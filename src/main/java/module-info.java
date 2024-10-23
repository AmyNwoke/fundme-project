module com.bptn.demofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
	requires javafx.base;
	requires jakarta.mail;
	exports com.bptn.fundmeproject_01_modelling to javafx.fxml; 
	
	opens com.bptn.fundmeproject_01_modelling to javafx.base;

    opens com.bptn.fundmeproject to javafx.fxml;
    exports com.bptn.fundmeproject;
    
}

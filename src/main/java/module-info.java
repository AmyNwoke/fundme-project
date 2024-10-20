module com.bptn.demofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
	requires javafx.base;

    opens com.bptn.fundmeproject to javafx.fxml;
    exports com.bptn.fundmeproject;
}

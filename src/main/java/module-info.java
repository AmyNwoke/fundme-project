module com.bptn.demofx {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.bptn.fundmeproject to javafx.fxml;
    exports com.bptn.fundmeproject;
}

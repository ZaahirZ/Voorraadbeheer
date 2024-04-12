module com.app.voorraadbeheer {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.app.voorraadbeheer to javafx.fxml;
    exports com.app.voorraadbeheer;
}

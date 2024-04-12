module com.app.voorraadbeheer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.app.voorraadbeheer to javafx.fxml;
    opens com.app.voorraadbeheer.afbeelding to javafx.fxml;
    exports com.app.voorraadbeheer to javafx.graphics;
}
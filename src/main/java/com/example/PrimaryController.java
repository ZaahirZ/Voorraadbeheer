package com.example;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PrimaryController {

    @FXML
    private VBox Primary; // This should match the fx:id of the VBox in your FXML file

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    public Label createLabel(String text) {
        Label label = new Label(text);
        return label;
    }

    @FXML
    private void initialize() {
        Label label = createLabel("Upload Afbeelding");
        Primary.getChildren().add(label);
    }
}
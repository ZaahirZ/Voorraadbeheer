package com.example;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class PrimaryController {

    @FXML
    private AnchorPane Primary;

    @FXML
    private FlowPane imageDisplayContainer;

    public Label createLabel(String text) {
        Label label = new Label(text);
        return label;
    }

    public void openImageWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("imageWindow.fxml"));
        AnchorPane imageWindow = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(imageWindow));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openNewWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewWindow.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("New Window");

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

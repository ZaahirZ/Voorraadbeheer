package com.example;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.nio.file.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class PrimaryController {

    @FXML
    private AnchorPane Primary;
    private Path imageFilePath; 

    @FXML
    private FlowPane imageDisplayContainer;

    public Label createLabel(String text) {
        Label label = new Label(text);
        return label;
    }

    public void imageInitiator() {
        displaySavedImages();
    }

    public void displaySavedImages(){
        imageDisplayContainer.getChildren().clear(); // Clear existing images before adding new ones
        File imagesDir = Paths.get(System.getProperty("user.dir"), "voorraadbeheer", "images").toFile();
        if (!imagesDir.exists()) {
            imagesDir.mkdirs();
        }
        File[] imageFiles = imagesDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg") || name.toLowerCase().endsWith(".gif"));
         
        if (imageFiles != null) {
            for (File imageFile : imageFiles) {
                Image image = new Image(imageFile.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageDisplayContainer.getChildren().add(imageView);
            }
        }
    }

    public void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File imageFile = fileChooser.showOpenDialog(null);
        if (imageFile != null) {
            imageFilePath = Paths.get(System.getProperty("user.dir"), "voorraadbeheer", "images", imageFile.getName());
            System.out.println(imageFilePath);
            try {
                Files.createDirectories(imageFilePath.getParent()); 
                Files.copy(imageFile.toPath(), imageFilePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            displaySavedImages(); // Refresh the displayed images after uploading a new one
        }
    }

    public void openNewWindow() {
        try {
            // Load the FXML file for the new window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewWindow.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("New Window");

            // Set the scene for the new stage
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Show the new stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

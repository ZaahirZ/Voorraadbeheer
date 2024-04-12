package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;

import java.nio.file.Paths;
import java.util.Optional;
import java.io.File;
import javafx.scene.control.ContextMenu; // Import the ContextMenu class



public class ImageWindowController {

    @FXML
    private ListView<File> imageListView;

    public void initialize() {
        // Load existing images into the ListView
        File imagesDir = Paths.get(System.getProperty("user.dir"), "voorraadbeheer", "images").toFile();
        if (imagesDir.exists() && imagesDir.isDirectory()) {
            ObservableList<File> imageFiles = FXCollections.observableArrayList(imagesDir.listFiles());
            imageListView.setItems(imageFiles);
        }

        setCustomCellFactory();
    }

    public void setCustomCellFactory() {
        // Set custom cell factory
        imageListView.setCellFactory(param -> new ListCell<File>() {
            private final ContextMenu contextMenu = new ContextMenu();
    
            {
                MenuItem renameItem = new MenuItem("Rename");
                renameItem.setOnAction(event -> renameImage(getItem()));
                contextMenu.getItems().add(renameItem);
            }
    
            @Override
            protected void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);
    
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Image image = new Image(item.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(100); // Set the width of the image view
                    imageView.setFitHeight(100); // Set the height of the image view
                    setGraphic(imageView);
                    setText(item.getName()); // Optionally set the text to the file name
                    setContextMenu(contextMenu);
                }
            }
        });
    }
    
    private void renameImage(File imageFile) {
        if (imageFile != null) {
            // Create a TextInputDialog
            TextInputDialog dialog = new TextInputDialog(imageFile.getName());
            dialog.setTitle("Rename Image");
            dialog.setHeaderText("Enter the new name for the image:");
            dialog.setContentText("Name:");
    
            // Get the new name from the user
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newName -> {
                // Append the file extension to the new name
                newName += getFileExtension(imageFile);
    
                // Create a new File object with the new name
                File renamedFile = new File(imageFile.getParent() + File.separator + newName);
    
                // Rename the file
                boolean renamed = imageFile.renameTo(renamedFile);
                if (!renamed) {
                    System.out.println("Failed to rename the file");
                } else {
                    // Refresh the ListView to show the new name
                    imageListView.getItems().remove(imageFile);
                    imageListView.getItems().add(renamedFile);
                    imageListView.refresh();
                }
            });
        }
    }

    public void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File imageFile = fileChooser.showOpenDialog(null);
        if (imageFile != null) {
            // Handle image upload here (e.g., copy the file to the image directory)
    
            // Define the new name for the file
            String newName = "newName" + getFileExtension(imageFile);
    
            // Create a new File object with the new name
            File renamedFile = new File(imageFile.getParent() + File.separator + newName);
    
            // Rename the file
            boolean renamed = imageFile.renameTo(renamedFile);
            if (!renamed) {
                System.out.println("Failed to rename the file");
            }
        }
    }
    
    // Helper method to get the file extension
    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }
}

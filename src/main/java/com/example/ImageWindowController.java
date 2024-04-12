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
import javafx.scene.control.ContextMenu;



public class ImageWindowController {

    @FXML
    private ListView<File> imageListView;

    public void initialize() {
        File imagesDir = Paths.get(System.getProperty("user.dir"), "voorraadbeheer", "images").toFile();
        if (imagesDir.exists() && imagesDir.isDirectory()) {
            ObservableList<File> imageFiles = FXCollections.observableArrayList(imagesDir.listFiles());
            imageListView.setItems(imageFiles);
        }

        setCustomCellFactory();
    }

    public void setCustomCellFactory() {
 
        imageListView.setCellFactory(param -> new ListCell<File>() {
            private final ContextMenu contextMenu = new ContextMenu();
    
            {
                MenuItem renameItem = new MenuItem("Rename");
                renameItem.setOnAction(event -> renameImage(getItem()));

                MenuItem deleteItem = new MenuItem("Delete");
                deleteItem.setOnAction(event -> deleteImage(getItem()));

                
                contextMenu.getItems().addAll(renameItem, deleteItem);
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
                    imageView.setFitWidth(100); 
                    imageView.setFitHeight(100); 
                    setGraphic(imageView);
                    setText(item.getName()); 
                    setContextMenu(contextMenu);
                }
            }
        });
    }

    private void deleteImage(File imageFile) {
        if (imageFile != null && imageFile.delete()) {
            imageListView.getItems().remove(imageFile);
            imageListView.refresh();
        } else {
            System.out.println("Failed to delete the file");
        }
    }
    
    private void renameImage(File imageFile) {
        if (imageFile != null) {
            
            TextInputDialog dialog = new TextInputDialog(imageFile.getName());
            dialog.setTitle("Rename Image");
            dialog.setHeaderText("Enter the new name for the image:");
            dialog.setContentText("Name:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newName -> {
                
                newName += getFileExtension(imageFile);
    
                File renamedFile = new File(imageFile.getParent() + File.separator + newName);

                boolean renamed = imageFile.renameTo(renamedFile);
                if (!renamed) {
                    System.out.println("Failed to rename the file");
                } else {
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
            
            String newName = "newName" + getFileExtension(imageFile);
    
            File renamedFile = new File(imageFile.getParent() + File.separator + newName);
    
            boolean renamed = imageFile.renameTo(renamedFile);
            if (!renamed) {
                System.out.println("Failed to rename the file");
            }
        }
    }
    
    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }
}

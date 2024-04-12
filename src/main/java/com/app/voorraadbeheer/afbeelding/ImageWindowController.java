package com.app.voorraadbeheer.afbeelding;
import com.app.voorraadbeheer.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.io.File;
import java.io.IOException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;



public class ImageWindowController {

    @FXML
    private ListView<File> imageListView;

    
    private File selectedImage;

    public void initialize() {
        selectImageLoad();
        setCustomCellFactory();

        // Set the selected image when the user clicks on an item in the list view
        ClickListenerImage();
    }

    private void ClickListenerImage() {
        imageListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedImage = newValue;
        });
    }

    private void selectImageLoad(){
        File imagesDir = Paths.get(System.getProperty("user.dir"), "voorraadbeheer", "images").toFile();
        if (imagesDir.exists() && imagesDir.isDirectory()) {
            ObservableList<File> imageFiles = FXCollections.observableArrayList(imagesDir.listFiles());
            imageListView.setItems(imageFiles);
        }
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
            File imagesDir = Paths.get(System.getProperty("user.dir"), "voorraadbeheer", "images").toFile();
            File destFile = new File(imagesDir, newName);
    
            try {
                Files.copy(imageFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                selectImageLoad(); //refresh the window
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }

    public void submit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/voorraadbeheer/primary.fxml"));
            Parent root = loader.load();
            PrimaryController primaryController = loader.getController();
            if (selectedImage != null) {
                primaryController.setImage(selectedImage);
            }
            Scene scene = new Scene(root);
            Stage stage = (Stage) imageListView.getScene().getWindow(); 
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}    


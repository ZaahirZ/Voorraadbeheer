package com.app.voorraadbeheer;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;

public class ProductController {



    public void initialize() {
        deleteImage.setVisible(false);
    }

    @FXML
    private AnchorPane Primary;
    @FXML
    private ImageView imageView;
    @FXML
    private FlowPane imageDisplayContainer;
    @FXML
    private Button deleteImage;

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

    public void setImage(File imageFile) {
        if (imageFile != null) {
            Image image = new Image(imageFile.toURI().toString());
            imageView.setImage(image);
            deleteImage.setVisible(true);
        }else{
            imageView.setImage(null);
            deleteImage.setVisible(false);
        }
    }

    public void deleteImage() {
        imageView.setImage(null);
        deleteImage.setVisible(false);
    }

    public ImageView getImageView() {
        return imageView;
    }
    
    public Button getDeleteImage() {
        return deleteImage;
    }
}

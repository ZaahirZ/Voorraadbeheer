package org.voorraadbeheer.PageController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.voorraadbeheer.Classes.Product;
import org.voorraadbeheer.Util.PageLoader;
import org.voorraadbeheer.Util.SQLiteDatabase;

import java.io.File;
import java.util.List;

public class AllPageController {

    private static final String DEFAULT_IMAGE_PATH = "product_images/defaultImage.png";

    @FXML
    private GridPane gridPane;

    private final SQLiteDatabase database = new SQLiteDatabase();

    @FXML
    public void initialize() {
        loadProducts();
    }

    private void loadProducts() {
        List<Product> allProducts = database.getAllProducts();
        int column = 0;
        int row = 0;
        int maxColumns = 4; // Adjust this value based on your UI design

        for (Product product : allProducts) {
            VBox productBox = createProductBox(product);
            gridPane.add(productBox, column, row);

            column++;
            if (column == maxColumns) {
                column = 0;
                row++;
            }
        }

        gridPane.setHgap(100);
        gridPane.setVgap(100); // Adding vertical gap as well
    }


    private VBox createProductBox(Product product) {
        VBox productBox = new VBox(20);
        ImageView productImageView = createProductImageView(product.getImagePath());
        Label nameLabel = createProductNameLabel(product.getName());
        nameLabel.getStyleClass().addAll("label", getProductQuantityStyle(product.getQuantity()));

        productBox.getChildren().addAll(productImageView, nameLabel);
        productBox.setOnMouseClicked(event -> PageLoader.loadProductPage(product));

        return productBox;
    }

    private ImageView createProductImageView(String imagePath) {
        Image image = loadImage(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(170);
        imageView.setFitWidth(170);
        return imageView;
    }

    private Label createProductNameLabel(String name) {
        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("label");
        return nameLabel;
    }

    private String getProductQuantityStyle(int quantity) {
        if (quantity > 10) {
            return "label-green";
        } else {
            return "label-red";
        }
    }

    private Image loadImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            imagePath = DEFAULT_IMAGE_PATH;
        }
        File imageFile = new File(imagePath);

        if (!imageFile.exists()) {
            imageFile = new File(DEFAULT_IMAGE_PATH);
        }

        if (imageFile.exists()) {
            return new Image("file:" + imageFile.getAbsolutePath());
        }
        return null;
    }

    @FXML
    private void loadmainMenu() {
        PageLoader.loadMainPage();
    }
}

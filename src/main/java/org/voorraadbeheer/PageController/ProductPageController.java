package org.voorraadbeheer.PageController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.voorraadbeheer.Classes.Product;
import org.voorraadbeheer.Util.SQLiteDatabase;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ProductPageController {
    private static final String IMAGE_DIR = "product_images";
    @FXML
    private ImageView imageView;
    @FXML
    private TextField aantalField;
    @FXML
    private Label productName;
    @FXML
    private Label prijsVeld;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private final SQLiteDatabase database;
    private Product currentProduct;

    public ProductPageController() {
        this.database = new SQLiteDatabase();
    }

    @FXML
    public void initialize() {
    }

    public void setProduct(Product product) {
        this.currentProduct = product;
        if (product != null) {
            productName.setText(product.getName().toUpperCase());
            aantalField.setText(String.valueOf(product.getQuantity()));

            NumberFormat euroFormat = DecimalFormat.getCurrencyInstance(Locale.GERMANY);
            String priceText = euroFormat.format(product.getPrice());
            prijsVeld.setText(priceText);

            loadImage(product.getImagePath());
        }
    }

    private void loadImage(String imagePath) {
        Image image;
        if (imagePath != null && !imagePath.isEmpty()) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                image = new Image("file:" + imageFile.getAbsolutePath());
            } else {
                image = new Image("file:" + IMAGE_DIR + "/defaultImage.png");
            }
        } else {
            image = new Image("file:" + IMAGE_DIR + "/defaultImage.png");
        }
        imageView.setImage(image);
    }

    @FXML
    private void saveQuantity() {
        int quantity = Integer.parseInt(aantalField.getText());
        currentProduct.setQuantity(quantity);
        database.updateProduct(currentProduct);
        saveButton.getScene().getWindow().hide();
    }

    @FXML
    private void cancel() {
        cancelButton.getScene().getWindow().hide();
    }
}

package org.voorraadbeheer.PageController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.voorraadbeheer.Classes.Product;
import org.voorraadbeheer.Util.SQLiteDatabase;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

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
    @FXML
    private VBox customFieldsContainer;

    private final SQLiteDatabase database;
    private Product currentProduct;
    private final Map<String, TextField> customFieldsMap = new HashMap<>();

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

            loadCustomFields(product.getId());
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

    private void loadCustomFields(int productId) {
        customFieldsMap.clear();
        customFieldsContainer.getChildren().clear();

        Map<String, String> customFields = database.getCustomFields(productId);
        customFields.forEach((fieldName, fieldValue) -> {
            HBox fieldBox = createCustomFieldBox(fieldName, fieldValue);
            customFieldsContainer.getChildren().add(fieldBox);
        });
    }

    @FXML
    private void saveQuantity() {
        int quantity = Integer.parseInt(aantalField.getText());
        currentProduct.setQuantity(quantity);
        database.updateProduct(currentProduct);
        saveCustomFields();
        saveButton.getScene().getWindow().hide();
    }

    @FXML
    private void cancel() {
        cancelButton.getScene().getWindow().hide();
    }

    @FXML
    private void addCustomField() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Voeg aanpasbare veld toe");
        dialog.setHeaderText(null);
        dialog.setContentText("Typ aanpasbare veld naam");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if (!name.trim().isEmpty()) { // Check if the field name is not empty
                HBox fieldBox = createCustomFieldBox(name, "");
                customFieldsContainer.getChildren().add(fieldBox);
            }
        });
    }

    private HBox createCustomFieldBox(String fieldName, String fieldValue) {
        Label fieldLabel = new Label(fieldName);
        fieldLabel.getStyleClass().add("aanpasbare-veld-label");

        TextField field = new TextField(fieldValue);
        field.setPromptText(fieldName);
        field.getStyleClass().add("aanpasbare-veld-input");

        customFieldsMap.put(fieldName, field);

        HBox fieldBox = new HBox(10, fieldLabel, field);
        fieldBox.getStyleClass().add("custom-field-box");

        Button deleteButton = new Button("Delete");
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setText("-");
        deleteButton.setOnAction(e -> {
            customFieldsContainer.getChildren().remove(fieldBox);
            customFieldsMap.remove(fieldName);
        });

        fieldBox.getChildren().add(deleteButton);

        return fieldBox;
    }

    @FXML
    private void saveCustomFields() {
        for (Map.Entry<String, TextField> entry : customFieldsMap.entrySet()) {
            String fieldName = entry.getKey();
            TextField field = entry.getValue();
            String value = field.getText();
            database.saveCustomField(currentProduct.getId(), fieldName, value);
        }
    }
}

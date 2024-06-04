package org.voorraadbeheer.PageController;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.voorraadbeheer.Classes.Product;
import org.voorraadbeheer.Util.SQLiteDatabase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.function.UnaryOperator;

public class ProductController {

    @FXML
    private TextField naamField;

    @FXML
    private TextField aantalField;

    @FXML
    private TextField prijsField;

    @FXML
    private ImageView productImageView;

    private File selectedImageFile;
    private Product product;

    private static final String IMAGE_DIR = "product_images";

    @FXML
    public void initialize() {
        configureAantalField();
        configurePrijsField();
    }

    private void configureAantalField() {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            return (newText.matches("\\d*")) ? change : null;
        };
        aantalField.setTextFormatter(new TextFormatter<>(integerFilter));
    }

    private void configurePrijsField() {
        DecimalFormat format = new DecimalFormat("#,##0.00");
        UnaryOperator<TextFormatter.Change> doubleFilter = change -> {
            String newText = change.getControlNewText();
            return (newText.matches("\\d*(,\\d{0,2})?")) ? change : null;
        };
        TextFormatter<Double> priceFormatter = new TextFormatter<>(change -> {
            if (doubleFilter.apply(change) == null) {
                return null;
            }

            try {
                String text = change.getControlNewText();
                if (!text.isEmpty()) {
                    format.parse(text.replace(",", "."));
                }
                return change;
            } catch (ParseException e) {
                return null;
            }
        });

        prijsField.setTextFormatter(priceFormatter);
    }

    @FXML
    private void voegProductaanDatabase() {
        String name = naamField.getText();
        Integer quantity = parseQuantity(aantalField.getText());
        Double price = parsePrice(prijsField.getText());

        if (isInputValid(name, quantity, price)) {
            String imagePath = (selectedImageFile != null) ? copyImageToDirectory(selectedImageFile) :
                    (product != null ? product.getImagePath() : null);

            if (product == null) {
                SQLiteDatabase.insertProduct(name, quantity, price, imagePath);
                showAlert(Alert.AlertType.INFORMATION, "Product Toegevoegd", "Product is succesvol toegevoegd.");
            } else {
                product.setName(name);
                product.setQuantity(quantity);
                product.setPrice(price);
                product.setImagePath(imagePath);
                SQLiteDatabase.updateProduct(product);
                showAlert(Alert.AlertType.INFORMATION, "Product Gewijzigd", "Product is succesvol gewijzigd.");
            }
            closeWindow();
        }
    }

    private String copyImageToDirectory(File imageFile) {
        try {
            Path targetDirectory = Paths.get(IMAGE_DIR);
            if (!Files.exists(targetDirectory)) {
                Files.createDirectories(targetDirectory);
            }

            String newFileName = System.currentTimeMillis() + "_" + imageFile.getName();
            Path targetPath = targetDirectory.resolve(newFileName);
            Files.copy(imageFile.toPath(), targetPath);

            return targetPath.toString();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Fout bij kopiëren", "Kon afbeelding niet kopiëren: " + e.getMessage());
            return null;
        }
    }

    private Integer parseQuantity(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Ongeldige Invoer", "Aantal moet een geldig geheel getal zijn.");
            return null;
        }
    }

    private Double parsePrice(String text) {
        try {
            String priceText = text.replace(",", ".");
            return Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Ongeldige Invoer", "Prijs moet een geldig nummer zijn.");
            return null;
        }
    }

    private boolean isInputValid(String name, Integer quantity, Double price) {
        if (name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Ongeldige Invoer", "Productnaam mag niet leeg zijn.");
            return false;
        }
        if (quantity == null || price == null) {
            return false;
        }
        return true;
    }

    private void closeWindow() {
        Stage stage = (Stage) naamField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            naamField.setText(product.getName());
            aantalField.setText(String.valueOf(product.getQuantity()));
            prijsField.setText(formatPriceForDisplay(product.getPrice()));
            loadImage(product.getImagePath());
        }
    }

    private void loadImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                productImageView.setImage(new Image("file:" + imageFile.getAbsolutePath()));
            }
        }
    }

    private String formatPriceForDisplay(Double price) {
        DecimalFormat format = new DecimalFormat("#,##0.00");
        String formattedPrice = format.format(price);
        return formattedPrice.replace(".", ",");
    }

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        selectedImageFile = fileChooser.showOpenDialog(productImageView.getScene().getWindow());

        if (selectedImageFile != null) {
            productImageView.setImage(new Image("file:" + selectedImageFile.getAbsolutePath()));
        }
    }
}

package org.voorraadbeheer.PageController;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import org.voorraadbeheer.Util.PageLoader;
import org.voorraadbeheer.Util.SQLiteDatabase;
import org.voorraadbeheer.Classes.Product;

import java.util.Optional;

public class MainController {

    public Button editProduct;
    public Button deleteProduct;
    public Button voegProduct;

    @FXML
    public void initialize() {
        toolTip();
    }

    @FXML
    public void addProduct() {
        PageLoader.loadProductPage(null); // Pass null to indicate adding a new product
    }

    private Tooltip createInstantTooltip(String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setShowDelay(Duration.ZERO);
        tooltip.setShowDuration(Duration.INDEFINITE);
        tooltip.setHideDelay(Duration.ZERO);
        return tooltip;
    }

    public void toolTip() {
        Tooltip voegProductTooltip = createInstantTooltip("Voeg je product toe");
        Tooltip.install(voegProduct, voegProductTooltip);

        Tooltip deleteProductTooltip = createInstantTooltip("Verwijder je product");
        Tooltip.install(deleteProduct, deleteProductTooltip);

        Tooltip editProductTooltip = createInstantTooltip("Wijzig je product");
        Tooltip.install(editProduct, editProductTooltip);
    }

    @FXML
    public void editProduct() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Wijzig Product");
        dialog.setHeaderText(null);
        dialog.setContentText("Voer de naam van het product in dat je wilt wijzigen:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String productName = result.get();
            Product product = SQLiteDatabase.getProductByName(productName);
            if (product != null) {
                PageLoader.loadProductPage(product);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fout");
                alert.setHeaderText(null);
                alert.setContentText("Product niet gevonden.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void deleteProduct() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Verwijder Product");
        dialog.setHeaderText(null);
        dialog.setContentText("Voer de naam van het product in dat je wilt verwijderen:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String productName = result.get();
            boolean isDeleted = SQLiteDatabase.deleteProductByName(productName);

            Alert alert;
            if (isDeleted) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succes");
                alert.setHeaderText(null);
                alert.setContentText("Product succesvol verwijderd.");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fout");
                alert.setHeaderText(null);
                alert.setContentText("Verwijderen van product mislukt.");
            }
            alert.showAndWait();
        }
    }
}

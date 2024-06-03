package org.voorraadbeheer.PageController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.voorraadbeheer.Classes.Product;
import org.voorraadbeheer.Util.PageLoader;
import org.voorraadbeheer.Util.SQLiteDatabase;

import java.util.List;
import java.util.Optional;

public class MainController {

    public Button editProduct;
    public Button deleteProduct;
    public Button voegProduct;
    public TextField zoekProduct;
    public TableView<Product> searchResultsTable;
    @FXML
    private AnchorPane root;

    @FXML
    public void initialize() {
        toolTip();
        zoekProductListener();
    }

    public void searchProduct(String searchText) {
        List<Product> searchResults = SQLiteDatabase.searchProductByName(searchText);
        ObservableList<Product> productList = FXCollections.observableArrayList(searchResults);
        searchResultsTable.setItems(productList);

        double rowHeight = 24;
        double headerHeight = 26;
        double requiredHeight = (productList.size() * rowHeight) + headerHeight;

        double maxHeight = 400;
        double minHeight = 110;
        requiredHeight = Math.min(Math.max(requiredHeight, minHeight), maxHeight);

        searchResultsTable.setPrefHeight(requiredHeight);

        searchResultsTable.requestLayout();
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

    public void zoekProductListener(){
        searchResultsTable.setVisible(false);

        zoekProduct.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    searchResultsTable.setVisible(false);
                }
            }
        });

        zoekProduct.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {

                searchResultsTable.setVisible(true);
                searchProduct(newValue);
            } else {
                searchResultsTable.setVisible(false);
            }
        });

        root.setOnMouseClicked(event -> {
            Node source = (Node) event.getTarget();

            if (!source.equals(zoekProduct)) {
                zoekProduct.getParent().requestFocus();
            }
        });
    }
}

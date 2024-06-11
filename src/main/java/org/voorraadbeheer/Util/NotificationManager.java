package org.voorraadbeheer.Util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.StageStyle;
import org.voorraadbeheer.Classes.Product;

import java.util.List;

public class NotificationManager {

    private static final int LOW_STOCK_THRESHOLD = 5; // Example threshold for low stock

    public static void showLowStockNotification(List<Product> lowStockProducts) {
        if (lowStockProducts == null || lowStockProducts.isEmpty()) {
            return;
        }

        StringBuilder content = new StringBuilder("De volgende producten zijn laag op voorraad:\n");
        for (Product product : lowStockProducts) {
            content.append(String.format("Product: %s, Aantal: %d\n", product.getName(), product.getQuantity()));
        }

        showAlert(content.toString());
    }

    private static void showAlert(String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Lage Voorraad!!");
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    public static void checkAndNotifyLowStockProducts(List<Product> allProducts) {
        List<Product> lowStockProducts = allProducts.stream()
                .filter(product -> product.getQuantity() < LOW_STOCK_THRESHOLD)
                .toList();
        showLowStockNotification(lowStockProducts);
    }
}

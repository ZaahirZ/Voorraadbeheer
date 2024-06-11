package org.voorraadbeheer.Util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.voorraadbeheer.Classes.Product;
import org.voorraadbeheer.Main;
import org.voorraadbeheer.PageController.ProductController;
import org.voorraadbeheer.PageController.ProductPageController;
import org.voorraadbeheer.Patterns.Database;

import java.io.IOException;

public class PageLoader {

    private static Pane rootLayout;
    private static Stage stage;
    private static Database database;

    public static void setRootLayout(Pane rootLayout) {
        PageLoader.rootLayout = rootLayout;
    }

    public static void setDatabase(Database db) {
        database = db;
    }

    public static void setStage(Stage stage) {
        PageLoader.stage = stage;
    }

    private static void initializeDatabase() {
        database = new SQLiteDatabase();
        database.createTable();
    }

    public static void startApplication() {
        initializeDatabase();
        Stage primaryStage = new Stage();
        setStage(primaryStage);
        setRootLayout(new Pane());
        loadMainPage();
    }

    public static void loadPage(String fxmlName, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlName));
            Pane rootLayout = fxmlLoader.load();
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadMainPage() {
        loadPage("Main.fxml", "Voorraadbeheer - Startscherm");
    }

    public static void loadProductController(Product product) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ProductToevoegen.fxml"));
            Parent root = fxmlLoader.load();
            ProductController controller = fxmlLoader.getController();
            controller.setProduct(product);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle(product == null ? "Voorraadbeheer - Product Toevoegen" : "Voorraadbeheer - Product Wijzigen");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadProductPage(Product product) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ProductPage.fxml"));
            Parent root = fxmlLoader.load();
            ProductPageController controller = fxmlLoader.getController();
            controller.setProduct(product);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Voorraadbeheer - Product");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadAllProductPage() {
        loadPage("AllProductPage.fxml", "Voorraadbeheer - Alle Producten");
    }
}

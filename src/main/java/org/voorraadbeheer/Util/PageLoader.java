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

import java.io.IOException;

public class PageLoader {

    private static Pane rootLayout;
    private static Stage stage;

    public static void setRootLayout(Pane rootLayout) {
        PageLoader.rootLayout = rootLayout;
    }

    public static void setStage(Stage stage) {
        PageLoader.stage = stage;
    }

    public static void startApplication(){
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
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadpopupPage(String fxmlName, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlName));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadMainPage() {
        loadPage("Main.fxml", "Voorraadbeheer - Startscherm");
    }

    public static void loadProductPopUpPage() {
        loadpopupPage("ProductToevoegen.fxml", "Voorraadbeheer - Product Toevoegen");
    }

    public static void loadProductPage(Product product) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ProductToevoegen.fxml"));
            Parent root = fxmlLoader.load();
            ProductController controller = fxmlLoader.getController();
            controller.setProduct(product);
            Stage stage = new Stage();
            stage.setTitle(product == null ? "Voorraadbeheer - Product Toevoegen" : "Voorraadbeheer - Product Wijzigen");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

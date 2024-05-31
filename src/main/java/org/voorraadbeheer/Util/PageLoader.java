package org.voorraadbeheer.Util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.voorraadbeheer.Main;

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


    public static void loadMainPage() {
        loadPage("Main.fxml", "Voorraadbeheer - Startscherm");
    }
}

package org.voorraadbeheer.Util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.voorraadbeheer.Main;

import java.io.IOException;
import java.util.Objects;

public class PageLoader {

    public static final String STYLESHEET_PATH = "/org/vooraadbeheer/style.css";
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

            if (rootLayout == null) {
                throw new IOException("FXML file '" + fxmlName + "' could not be loaded.");
            }

            Scene scene = new Scene(rootLayout);
            if (stage != null) {
                stage.setScene(scene);
                stage.setTitle(title);
                stage.show();
            } else {
                System.err.println("Error: Stage is null. Cannot set scene.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void loadMainPage() {
        loadPage("Main.fxml", "Voorraadbeheer - Startscherm");
    }
}

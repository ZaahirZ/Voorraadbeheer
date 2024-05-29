package org.voorraadbeheer.Util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.voorraadbeheer.Main;

import java.io.IOException;

public class PageLoader {

    private static Scene scene;
    private static Pane pane;

    public static void startApplication(){
        Stage primaryStage = new Stage();
        setStage(primaryStage);
        setRootLayout(new Pane());
        loadPage();
    }

    public static void loadPage(String fxmlName, String title) {
        try{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlFile));
        rootLayout = fxmlLoader.load();
        Scene scene = new Scene(rootLayout);
        scene.getStylesheets().add(Main.class.getResource(STYLESHEET_PATH).toExternalForm());
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
        stage.setResizable(false);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}

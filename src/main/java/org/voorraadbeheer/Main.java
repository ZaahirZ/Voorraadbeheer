package org.voorraadbeheer;

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.voorraadbeheer.Util.PageLoader;
import org.voorraadbeheer.Util.SQLiteDatabase;

public class Main extends Application {

    @Override
    public void start(Stage stage){
        Pane rootLayout = new Pane();
        PageLoader.setRootLayout(rootLayout);
        PageLoader.startApplication();
        SQLiteDatabase.createNewDatabase();
    }

    public static void main(String[] args) {
        launch();
    }

}
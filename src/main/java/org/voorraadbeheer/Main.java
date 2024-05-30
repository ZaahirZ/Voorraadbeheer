package org.voorraadbeheer;

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.voorraadbeheer.Util.PageLoader;

public class Main extends Application {

    @Override
    public void start(Stage stage){
        Pane rootLayout = new Pane();
        PageLoader.setRootLayout(rootLayout);
        PageLoader.startApplication();
    }

    public static void main(String[] args) {
        launch();
    }

}
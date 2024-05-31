package org.voorraadbeheer.PageController;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.voorraadbeheer.Util.PageLoader;

public class MainController {

    @FXML
    private Button voegProduct;

    @FXML
    public void addProduct() {
        PageLoader.loadProductPopUpPage();
    }
}
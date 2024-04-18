import com.app.voorraadbeheer.ProductController;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.File;

import static org.junit.Assert.*;

public class ProductControllerTest extends ApplicationTest {

    private ProductController controller;

    @Override
    public void start(Stage stage) throws Exception {
        controller = new ProductController();
        controller.initialize();
    }

    @Test
    public void testCreateLabel() {
        Label label = controller.createLabel("Test");
        assertNotNull(label);
        assertEquals("Test", label.getText());
    }

}
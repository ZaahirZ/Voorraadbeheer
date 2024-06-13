package Controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationTest;
import org.voorraadbeheer.Classes.Product;
import org.voorraadbeheer.PageController.ProductPageController;
import org.voorraadbeheer.Util.SQLiteDatabase;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductPageControllerTest extends ApplicationTest {

    @Mock
    private SQLiteDatabase mockDatabase;

    @InjectMocks
    private ProductPageController controller;

    private Stage stage;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/voorraadbeheer/ProductPage.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        controller.database = mockDatabase;

        Platform.runLater(() -> {
            stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        });
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
    }

    @Test
    void test_Case1() throws InterruptedException {
        Product testProduct = new Product(1, "", 0, 0.0);
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            controller.setProduct(testProduct);
            controller.getAantalField().setText("");
            controller.saveQuantity();
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);

        assertEquals("", controller.productName.getText());
        assertEquals(0, testProduct.getQuantity());
        verify(mockDatabase, never()).updateProduct(any());
    }

    @Test
    void test_Case2() throws InterruptedException {
        Product testProduct = new Product(1, "", 0, 0.0);
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            controller.setProduct(testProduct);
            controller.getAantalField().setText("10"); // Numeric value
            controller.saveQuantity();
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);

        assertEquals("", controller.productName.getText());
        assertEquals(10, testProduct.getQuantity());
        verify(mockDatabase, times(1)).updateProduct(testProduct);
    }

    @Test
    void test_Case3() throws InterruptedException {
        Product testProduct = new Product(1, "Test Product", 0, 0.0);
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            controller.setProduct(testProduct);
            controller.getAantalField().setText("");
            controller.saveQuantity();
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);

        assertEquals("TEST PRODUCT", controller.productName.getText());
        assertEquals(0, testProduct.getQuantity());
        verify(mockDatabase, never()).updateProduct(any());
    }

    @Test
    void test_Case4() throws InterruptedException {
        Product testProduct = new Product(1, "Test Product", 0, 0.0);
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            controller.setProduct(testProduct);
            controller.getAantalField().setText("15"); // Numeric value
            controller.saveQuantity();
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);

        assertEquals("TEST PRODUCT", controller.productName.getText());
        assertEquals(15, testProduct.getQuantity());
        verify(mockDatabase, times(1)).updateProduct(testProduct);
    }

    @Test
    void test_Case5() throws InterruptedException {
        Product testProduct = new Product(1, "", 0, 0.0);
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            controller.setProduct(testProduct);
            controller.getAantalField().setText("");
            controller.saveQuantity();
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);

        assertEquals("", controller.productName.getText());
        assertEquals(0, testProduct.getQuantity());
        verify(mockDatabase, never()).updateProduct(any());
    }

    @Test
    void test_Case6() throws InterruptedException {
        Product testProduct = new Product(1, "", 0, 0.0);
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            controller.setProduct(testProduct);
            controller.getAantalField().setText("10"); // Numeric value
            controller.saveQuantity();
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);

        assertEquals("", controller.productName.getText());
        assertEquals(10, testProduct.getQuantity());
        verify(mockDatabase, times(1)).updateProduct(testProduct);
    }

    @Test
    void test_Case7() throws InterruptedException {
        Product testProduct = new Product(1, "Test Product", 0, 0.0);
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            controller.setProduct(testProduct);
            controller.getAantalField().setText("");
            controller.saveQuantity();
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);

        assertEquals("TEST PRODUCT", controller.productName.getText());
        assertEquals(0, testProduct.getQuantity());
        verify(mockDatabase, never()).updateProduct(any());
    }

    @Test
    void test_Case8() throws InterruptedException {
        Product testProduct = new Product(1, "Test Product", 0, 0.0);
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            controller.setProduct(testProduct);
            controller.getAantalField().setText("15"); // Numeric value
            controller.saveQuantity();
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);

        assertEquals("TEST PRODUCT", controller.productName.getText());
        assertEquals(15, testProduct.getQuantity());
        verify(mockDatabase, times(1)).updateProduct(testProduct);
    }
}

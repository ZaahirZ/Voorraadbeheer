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
        // Scenario: Lege aantalveld, Lege Productnaam, Null, Lege Prijs
        Product testProduct = new Product(1, "", 0, 0.0);
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            controller.setProduct(testProduct);
            controller.getAantalField().setText("");
            controller.getProductNaamField().setText("");
            controller.setPrijsField("");
            controller.saveQuantity();
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);

        assertEquals("", controller.getProductNaamField().getText());
        assertEquals(0, testProduct.getQuantity());
        assertEquals(0.0, testProduct.getPrice());
        verify(mockDatabase, never()).updateProduct(any());
    }

    @Test
    void test_Case2() throws InterruptedException {
        // Scenario: Lege aantalveld, Lege productnaam, Geeft product, Wel prijs
        Product testProduct = new Product(1, "", 0, 10.0);
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            controller.setProduct(testProduct);
            controller.getAantalField().setText("");
            controller.getProductNaamField().setText("Test Product");
            controller.setPrijsField("10.0");
            controller.saveQuantity();
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);

        assertEquals("Test Product", controller.getProductNaamField().getText());
        assertEquals(0, testProduct.getQuantity());
        assertEquals(10.0, testProduct.getPrice());
        verify(mockDatabase, never()).updateProduct(any());
    }

    @Test
    void test_Case3() throws InterruptedException {
        // Scenario: Lege aantalveld, Productnaam, Null, Lege Prijs
        Product testProduct = new Product(1, "", 0, 0.0);
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            controller.setProduct(testProduct);
            controller.getAantalField().setText("");
            controller.getProductNaamField().setText("Test Product");
            controller.setPrijsField("");
            controller.saveQuantity();
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);

        assertEquals("Test Product", controller.getProductNaamField().getText());
        assertEquals(0, testProduct.getQuantity());
        assertEquals(0.0, testProduct.getPrice());
        verify(mockDatabase, never()).updateProduct(any());
    }

    @Test
    void test_Case4() throws InterruptedException {
        // Scenario: Lege aantalveld, Productnaam, Geeft product, Wel prijs
        Product testProduct = new Product(1, "", 0, 15.0);
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            controller.setProduct(testProduct);
            controller.getAantalField().setText("");
            controller.getProductNaamField().setText("Test Product");
            controller.setPrijsField("15.0");
            controller.saveQuantity();
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);

        assertEquals("Test Product", controller.getProductNaamField().getText());
        assertEquals(0, testProduct.getQuantity());
        assertEquals(15.0, testProduct.getPrice());
        verify(mockDatabase, never()).updateProduct(any());
    }

    @Test
    void test_Case5() throws InterruptedException {
        // Scenario: Nummers in aantalveld, Lege productnaam, Null, Lege prijs
        Product testProduct = new Product(1, "", 0, 0.0);
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            controller.setProduct(testProduct);
            controller.getAantalField().setText("5");
            controller.getProductNaamField().setText("");
            controller.setPrijsField("");
            controller.saveQuantity();
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);

        assertEquals("", controller.getProductNaamField().getText());
        assertEquals(5, testProduct.getQuantity());
        assertEquals(0.0, testProduct.getPrice());
        verify(mockDatabase).updateProduct(testProduct);
    }

    @Test
    void test_Case6() throws InterruptedException {
        // Scenario: Nummers in aantalveld, Lege productnaam, Geeft product, Wel prijs
        Product testProduct = new Product(1, "", 0, 10.0);
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            controller.setProduct(testProduct);
            controller.getAantalField().setText("5");
            controller.getProductNaamField().setText("Test Product");
            controller.setPrijsField("10.0");
            controller.saveQuantity();
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);

        assertEquals("Test Product", controller.getProductNaamField().getText());
        assertEquals(5, testProduct.getQuantity());
        assertEquals(10.0, testProduct.getPrice());
        verify(mockDatabase).updateProduct(testProduct);
    }

    @Test
    void test_Case7() throws InterruptedException {
        // Scenario: Nummers in aantalveld, Productnaam, Null, Lege prijs
        Product testProduct = new Product(1, "", 0, 0.0);
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            controller.setProduct(testProduct);
            controller.getAantalField().setText("5");
            controller.getProductNaamField().setText("Test Product");
            controller.setPrijsField("");
            controller.saveQuantity();
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);

        assertEquals("Test Product", controller.getProductNaamField().getText());
        assertEquals(5, testProduct.getQuantity());
        assertEquals(0.0, testProduct.getPrice());
        verify(mockDatabase).updateProduct(testProduct);
    }

    @Test
    void test_Case8() throws InterruptedException {
        // Scenario: Nummers in aantalveld, Productnaam, Geeft product, Wel prijs
        Product testProduct = new Product(1, "", 0, 15.0);
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            controller.setProduct(testProduct);
            controller.getAantalField().setText("5");
            controller.getProductNaamField().setText("Test Product");
            controller.setPrijsField("15.0");
            controller.saveQuantity();
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);

        assertEquals("Test Product", controller.getProductNaamField().getText());
        assertEquals(5, testProduct.getQuantity());
        assertEquals(15.0, testProduct.getPrice());
        verify(mockDatabase).updateProduct(testProduct);
    }


    }


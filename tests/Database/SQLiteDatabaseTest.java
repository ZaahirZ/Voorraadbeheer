package Database;

import org.junit.jupiter.api.*;
import org.voorraadbeheer.Classes.Product;
import org.voorraadbeheer.Util.SQLiteDatabase;

public class SQLiteDatabaseTest {
    SQLiteDatabase db;

    @BeforeEach
    public void setup() {
        db = new SQLiteDatabase();
        db.createTable(); // Ensure tables are created before each test
    }

    @AfterEach
    public void cleanup() {
        db.deleteProductByName("TestProduct"); // Clean up test data after each test
    }

    // Equivalence Class 1: Negative Price
    @Test
    public void testInsertProductWithHighNegativePrice() {
        String productName = "TestProduct";
        int quantity = 10;
        double negativePrice = -1000.0;
        String imagePath = "path/to/image";

        try {
            db.insertProduct(productName, quantity, negativePrice, imagePath);
            Assertions.fail("Expected IllegalArgumentException for negative price insertion");
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue(e.getMessage().contains("Price cannot be negative"),
                    "Exception message should contain 'Price cannot be negative'");
        }
    }

    @Test
    public void testInsertProductWithHLowNegativePrice() {
        String productName = "TestProduct";
        int quantity = 10;
        double negativePrice = -0.01;
        String imagePath = "path/to/image";

        try {
            db.insertProduct(productName, quantity, negativePrice, imagePath);
            Assertions.fail("Expected IllegalArgumentException for negative price insertion");
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue(e.getMessage().contains("Price cannot be negative"),
                    "Exception message should contain 'Price cannot be negative'");
        }
    }

    // Equivalence Class 2: Zero Price
    @Test
    public void testInsertProductWithZeroPrice() {
        String productName = "TestProduct";
        int quantity = 10;
        double zeroPrice = 0.0;
        String imagePath = "path/to/image";

        try {
            db.insertProduct(productName, quantity, zeroPrice, imagePath);

            Product insertedProduct = db.getProductByName(productName);

            Assertions.assertNotNull(insertedProduct, "Product should not be null");
            Assertions.assertEquals(productName, insertedProduct.getName(), "Product name should match");
            Assertions.assertEquals(quantity, insertedProduct.getQuantity(), "Product quantity should match");
            Assertions.assertEquals(zeroPrice, insertedProduct.getPrice(), 0.01, "Product price should match");
            Assertions.assertEquals(imagePath, insertedProduct.getImagePath(), "Product imagePath should match");

        } catch (IllegalArgumentException e) {
            Assertions.fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
    }

    // Equivalence Class 3: Positive Price (Low)
    @Test
    public void testInsertProductWithLowPositivePrice() {
        String productName = "TestProduct";
        int quantity = 10;
        double lowPositivePrice = 0.01; // Just above zero
        String imagePath = "path/to/image";

        try {
            db.insertProduct(productName, quantity, lowPositivePrice, imagePath);

            Product insertedProduct = db.getProductByName(productName);

            Assertions.assertNotNull(insertedProduct, "Product should not be null");
            Assertions.assertEquals(productName, insertedProduct.getName(), "Product name should match");
            Assertions.assertEquals(quantity, insertedProduct.getQuantity(), "Product quantity should match");
            Assertions.assertEquals(lowPositivePrice, insertedProduct.getPrice(), 0.01, "Product price should match");
            Assertions.assertEquals(imagePath, insertedProduct.getImagePath(), "Product imagePath should match");

        } catch (IllegalArgumentException e) {
            Assertions.fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
    }

    // Equivalence Class 3: Positive Price (High)
    @Test
    public void testInsertProductWithHighPrice() {
        String productName = "TestProduct";
        int quantity = 10;
        double highPrice = 150.0; // High price
        String imagePath = "path/to/image";

        try {
            db.insertProduct(productName, quantity, highPrice, imagePath);

            Product insertedProduct = db.getProductByName(productName);

            Assertions.assertNotNull(insertedProduct, "Product should not be null");
            Assertions.assertEquals(productName, insertedProduct.getName(), "Product name should match");
            Assertions.assertEquals(quantity, insertedProduct.getQuantity(), "Product quantity should match");
            Assertions.assertEquals(highPrice, insertedProduct.getPrice(), 0.01, "Product price should match");
            Assertions.assertEquals(imagePath, insertedProduct.getImagePath(), "Product imagePath should match");

        } catch (IllegalArgumentException e) {
            Assertions.fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
    }
}

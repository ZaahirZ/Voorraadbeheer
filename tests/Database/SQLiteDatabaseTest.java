package Database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.voorraadbeheer.Classes.Product;
import org.voorraadbeheer.Util.SQLiteDatabase;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SQLiteDatabaseTest {

    private SQLiteDatabase database;

    @BeforeEach
    public void setUp() throws Exception {
        database = new SQLiteDatabase();
        database.createTable();
    }

    @AfterEach
    public void tearDown() throws Exception {
        File dbFile = new File("voorraadbeheer.db");
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    @Test
    public void testCreateTable() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:voorraadbeheer.db")) {
            assertNotNull(conn);
            assertFalse(conn.isClosed());
        } catch (SQLException e) {
            fail("Failed to connect to the database: " + e.getMessage());
        }
    }

    @Test
    public void testInsertProduct() {
        database.insertProduct("Test Product", 10, 99.99, "test_image.jpg");
        Product product = database.getProductByName("Test Product");
        assertNotNull(product);
        assertEquals("Test Product", product.getName());
        assertEquals(10, product.getQuantity());
        assertEquals(99.99, product.getPrice(), 0.001);
        assertEquals("test_image.jpg", product.getImagePath());
    }

    @Test
    public void testUpdateProduct() {
        database.insertProduct("Test Product", 10, 99.99, "test_image.jpg");
        Product product = database.getProductByName("Test Product");
        assertNotNull(product);

        product.setName("Updated Product");
        product.setQuantity(20);
        product.setPrice(149.99);
        product.setImagePath("updated_image.jpg");
        database.updateProduct(product);

        Product updatedProduct = database.getProductByName("Updated Product");
        assertNotNull(updatedProduct);
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals(20, updatedProduct.getQuantity());
        assertEquals(149.99, updatedProduct.getPrice(), 0.001);
        assertEquals("updated_image.jpg", updatedProduct.getImagePath());
    }

    @Test
    public void testGetAllProducts() {
        database.insertProduct("Product 1", 5, 19.99, "image1.jpg");
        database.insertProduct("Product 2", 15, 29.99, "image2.jpg");
        List<Product> products = database.getAllProducts();
        assertNotNull(products);
        assertEquals(2, products.size());
    }

    @Test
    public void testGetProductByName() {
        database.insertProduct("Unique Product", 5, 19.99, "unique_image.jpg");
        Product product = database.getProductByName("Unique Product");
        assertNotNull(product);
        assertEquals("Unique Product", product.getName());
    }

    @Test
    public void testDeleteProductByName() {
        database.insertProduct("Product to Delete", 5, 19.99, "delete_image.jpg");
        boolean deleted = database.deleteProductByName("Product to Delete");
        assertTrue(deleted);
        Product product = database.getProductByName("Product to Delete");
        assertNull(product);
    }

    @Test
    public void testSearchProductByName() {
        database.insertProduct("Search Product 1", 5, 19.99, "search_image1.jpg");
        database.insertProduct("Search Product 2", 10, 39.99, "search_image2.jpg");
        List<Product> products = database.searchProductByName("Search");
        assertNotNull(products);
        assertEquals(2, products.size());
    }

    @Test
    public void testSearchProductByPartialName() {
        database.insertProduct("Apple", 50, 1.00, "apple.jpg");
        database.insertProduct("Apple Pie", 20, 5.00, "apple_pie.jpg");
        database.insertProduct("Banana", 100, 0.50, "banana.jpg");

        List<Product> products = database.searchProductByName("Apple");
        assertNotNull(products);
        assertEquals(2, products.size());
    }
}

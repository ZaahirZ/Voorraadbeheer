package Controllers;

import org.junit.jupiter.api.Test;
import org.voorraadbeheer.PageController.ProductController;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductControllerTest {

    @Test
    public void testAllConditionsTrue() {
        ProductController controller = new ProductController();
        boolean result = controller.isProductDataValid("Product", 10, 20.0);
        assertTrue(result);
    }

    @Test
    public void testProductEmptyName() {
        ProductController controller = new ProductController();
        boolean result = controller.isProductDataValid("", 10, 20.0);
        assertFalse(result);
    }

    @Test
    public void testProductNameNull() {
        ProductController controller = new ProductController();
        boolean result = controller.isProductDataValid(null, 10, 20.0);
        assertFalse(result);
    }

    @Test
    public void testQuantityNull() {
        ProductController controller = new ProductController();
        boolean result = controller.isProductDataValid("Product", null, 20.0);
        assertFalse(result);
    }

    @Test
    public void testPriceNull() {
        ProductController controller = new ProductController();
        boolean result = controller.isProductDataValid("Product", 10, null);
        assertFalse(result);
    }
}

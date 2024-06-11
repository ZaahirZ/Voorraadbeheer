package org.voorraadbeheer.Templates;

import org.voorraadbeheer.Classes.Product;

import java.sql.Connection;
import java.util.List;

public interface Database {
    Connection connect();

    void createTable();

    void insertProduct(String name, int quantity, double price, String imagePath);

    void updateProduct(Product product);

    List<Product> getAllProducts();

    Product getProductByName(String name);

    boolean deleteProductByName(String productName);

    List<Product> searchProductByName(String name);
}

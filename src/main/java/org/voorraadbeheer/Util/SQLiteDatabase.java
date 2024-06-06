package org.voorraadbeheer.Util;

import org.voorraadbeheer.Classes.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabase implements Database {
    private static final String URL = "jdbc:sqlite:voorraadbeheer.db";

    @Override
    public Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

    @Override
    public void createTable() {
        try (Connection conn = connect()) {
            String sql = "CREATE TABLE IF NOT EXISTS products (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "quantity INTEGER NOT NULL," +
                    "price REAL," +
                    "imagePath TEXT" +
                    ")";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("Table 'products' has been created or already exists.");
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error creating table 'products'");
        }
    }

    @Override
    public void insertProduct(String name, int quantity, double price, String imagePath) {
        String sql = "INSERT INTO products(name, quantity, price, imagePath) VALUES(?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, quantity);
            pstmt.setDouble(3, price);
            pstmt.setString(4, imagePath);
            pstmt.executeUpdate();
            System.out.println("Product inserted successfully.");
        } catch (SQLException e) {
            handleSQLException(e, "Error inserting product");
        }
    }

    @Override
    public void updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, quantity = ?, price = ?, imagePath = ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getName());
            pstmt.setInt(2, product.getQuantity());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setString(4, product.getImagePath());
            pstmt.setInt(5, product.getId());
            pstmt.executeUpdate();
            System.out.println("Product updated successfully.");
        } catch (SQLException e) {
            handleSQLException(e, "Error updating product");
        }
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                String imagePath = rs.getString("imagePath");
                productList.add(new Product(id, name, quantity, price, imagePath));
                System.out.println("ID: " + id + ", Name: " + name + ", Quantity: " + quantity + ", Price: " + price + ", Image Path: " + imagePath);
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error fetching products");
        }
        return productList;
    }

    @Override
    public Product getProductByName(String name) {
        String sql = "SELECT * FROM products WHERE name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                String imagePath = rs.getString("imagePath");
                return new Product(id, name, quantity, price, imagePath);
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error fetching product");
        }
        return null;
    }

    @Override
    public boolean deleteProductByName(String productName) {
        String sql = "DELETE FROM products WHERE name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productName);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            handleSQLException(e, "Error deleting product");
            return false;
        }
    }

    @Override
    public List<Product> searchProductByName(String name) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name LIKE ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String productName = rs.getString("name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                String imagePath = rs.getString("imagePath");
                Product product = new Product(id, productName, quantity, price, imagePath);
                products.add(product);
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error searching products by name");
        }
        return products;
    }

    private void handleSQLException(SQLException e, String message) {
        System.err.println(message + ": " + e.getMessage());
    }
}

package org.voorraadbeheer.Util;

import org.voorraadbeheer.Classes.Product;
import org.voorraadbeheer.Patterns.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            String createProductsTable = "CREATE TABLE IF NOT EXISTS products (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "quantity INTEGER NOT NULL," +
                    "price REAL," +
                    "imagePath TEXT" +
                    ")";
            String createCustomFieldsTable = "CREATE TABLE IF NOT EXISTS custom_fields (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "product_id INTEGER," +
                    "field_name TEXT NOT NULL," +
                    "field_value TEXT," +
                    "FOREIGN KEY (product_id) REFERENCES products(id)" +
                    ")";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createProductsTable);
                stmt.execute(createCustomFieldsTable);
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error creating tables");
        }
    }

    @Override
    public void insertProduct(String name, int quantity, double price, String imagePath) {
        String sql = "INSERT INTO products(name, quantity, price, imagePath) VALUES(?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, quantity);
            pstmt.setDouble(3, price);
            pstmt.setString(4, imagePath);
            pstmt.executeUpdate();
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

    public void saveCustomField(int productId, String fieldName, String fieldValue) {
        String sql = "INSERT INTO custom_fields(product_id, field_name, field_value) VALUES(?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            pstmt.setString(2, fieldName);
            pstmt.setString(3, fieldValue);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e, "Error saving custom field");
        }
    }

    public Map<String, String> getCustomFields(int productId) {
        Map<String, String> customFields = new HashMap<>();
        String sql = "SELECT field_name, field_value FROM custom_fields WHERE product_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                customFields.put(rs.getString("field_name"), rs.getString("field_value"));
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error fetching custom fields");
        }
        return customFields;
    }

    private void handleSQLException(SQLException e, String message) {
        System.err.println(message + ": " + e.getMessage());
    }


    public Product getProductById(int id) {
        // Create a new connection to your SQLite database
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:your_database.db")) {
            // Create a new SQL statement
            try (Statement stmt = conn.createStatement()) {
                // Execute the SQL query
                ResultSet rs = stmt.executeQuery("SELECT * FROM Products WHERE id = " + id);

                // If a result is returned
                if (rs.next()) {
                    // Create a new Product object from the result
                    String name = rs.getString("name");
                    int quantity = rs.getInt("quantity");
                    double price = rs.getDouble("price");
                    String imagePath = rs.getString("imagePath");

                    return new Product(id, name, quantity, price, imagePath);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // If no product was found, return null
        return null;
    }
}

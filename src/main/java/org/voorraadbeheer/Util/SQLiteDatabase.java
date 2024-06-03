package org.voorraadbeheer.Util;

import org.voorraadbeheer.Classes.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabase {
    private static final String URL = "jdbc:sqlite:voorraadbeheer.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void createNewDatabase() {
        try (Connection conn = connect()) {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS products (\n"
                        + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                        + " name TEXT NOT NULL,\n"
                        + " quantity INTEGER NOT NULL,\n"
                        + " price REAL\n"
                        + ");";
                stmt.execute(sql);
                System.out.println("Table 'products' has been created or already exists.");
            }
        } catch (SQLException e) {
            System.out.println("Error creating table 'products': " + e.getMessage());
        }
    }

    public static void insertProduct(String name, int quantity, double price) {
        String sql = "INSERT INTO products(name, quantity, price) VALUES(?,?,?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, quantity);
            pstmt.setDouble(3, price);
            pstmt.executeUpdate();
            System.out.println("Product inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting product: " + e.getMessage());
        }
    }

    public static void updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, quantity = ?, price = ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getName());
            pstmt.setInt(2, product.getQuantity());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getId());
            pstmt.executeUpdate();
            System.out.println("Product updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    public static List<Product> getAllProducts() {
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
                productList.add(new Product(id, name, quantity, price));
                System.out.println("ID: " + id + ", Name: " + name + ", Quantity: " + quantity + ", Price: " + price);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }
        return productList;
    }

    public static Product getProductByName(String name) {
        String sql = "SELECT * FROM products WHERE name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                return new Product(id, name, quantity, price);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching product: " + e.getMessage());
        }
        return null;
    }

    public static boolean deleteProductByName(String productName) {
        String sql = "DELETE FROM products WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productName);
            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}

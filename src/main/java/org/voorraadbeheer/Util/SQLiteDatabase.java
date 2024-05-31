package org.voorraadbeheer.Util;

import java.sql.*;

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
}
